package org.gsn.pandora.spawner;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.gsn.pandora.Pandora;
import org.gsn.pandora.mobs.MobType;
import org.gsn.pandora.skills.mechanics.SetLevelMechanic;
import org.gsn.pandora.util.FoliaScheduler;
import org.gsn.pandora.util.PandoraTask;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机生成管理器（对应 Wiki「随机生成」机制）。
 * 定期在玩家周围随机位置尝试按 RandomSpawn 配置生成自定义实体，
 * 支持群系过滤、世界过滤、数量上限、等级范围与消失策略。
 */
public class RandomSpawnManager {

    private final Pandora plugin;
    private final MobManagerHolder holder = new MobManagerHolder();
    private final Map<String, RandomSpawnDefinition> definitions = new HashMap<>();
    private final RandomSpawnerTag tag = new RandomSpawnerTag();
    private final ChunkSpawnCounter counter = new ChunkSpawnCounter();

    private PandoraTask task;
    private long intervalTicks = 200L;

    /** 延迟引用，避免构造循环 */
    private org.gsn.pandora.mobs.MobManager mobManager;

    public RandomSpawnManager(Pandora plugin) {
        this.plugin = plugin;
    }

    public void setMobManager(org.gsn.pandora.mobs.MobManager mobManager) {
        this.mobManager = mobManager;
    }

    private static final class MobManagerHolder { }
    private static final class RandomSpawnerTag {
        final org.bukkit.NamespacedKey key = new org.bukkit.NamespacedKey("pandora", "random-spawner");
    }
    private static final class ChunkSpawnCounter { }

    public void reload() {
        definitions.clear();
        FileConfigReader reader = new FileConfigReader(plugin);
        reader.load(definitions);
        intervalTicks = reader.getIntervalTicks();
        plugin.getLogger().info("已加载 " + definitions.size() + " 个随机生成配置。");
    }

    private static final class FileConfigReader {
        private final Pandora plugin;
        private long intervalTicks = 200L;

        FileConfigReader(Pandora plugin) { this.plugin = plugin; }

        long getIntervalTicks() { return intervalTicks; }

        void load(Map<String, RandomSpawnDefinition> out) {
            // 读取 random-spawners/ 目录下所有 yml
            java.io.File dir = new java.io.File(plugin.getDataFolder(), "random-spawners");
            if (!dir.exists()) {
                dir.mkdirs();
                return;
            }
            java.io.File[] files = dir.listFiles((d, name) -> name.endsWith(".yml"));
            if (files == null) return;
            for (java.io.File file : files) {
                try {
                    org.bukkit.configuration.file.YamlConfiguration config =
                        org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(file);
                    for (String id : config.getKeys(false)) {
                        ConfigurationSection section = config.getConfigurationSection(id);
                        if (section == null) continue;
                        RandomSpawnDefinition def = new RandomSpawnDefinition(
                            id,
                            section.getString("Mob"),
                            section.getStringList("Worlds"),
                            section.getStringList("Biomes"),
                            section.getInt("MinChunkDistance", 4),
                            section.getInt("MaxChunkDistance", 10),
                            section.getInt("MaxMobsPerWorld", 20),
                            section.getDouble("Chance", 0.1),
                            section.getInt("MinLevel", 0),
                            section.getInt("MaxLevel", 0),
                            section.getString("Despawn", "DESPAWN"),
                            section.getBoolean("Enabled", true)
                        );
                        out.put(id.toLowerCase(Locale.ROOT), def);
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("加载随机生成配置失败: " + file.getName());
                }
            }
        }
    }

    public void startTask() {
        stopTask();
        task = FoliaScheduler.runGlobalTaskTimer(plugin, this::tick, intervalTicks, intervalTicks);
    }

    public void stopTask() {
        if (task != null) { task.cancel(); task = null; }
    }

    public Collection<RandomSpawnDefinition> getAll() {
        return Collections.unmodifiableCollection(definitions.values());
    }

    private void tick() {
        if (definitions.isEmpty() || mobManager == null) return;
        for (RandomSpawnDefinition def : definitions.values()) {
            if (!def.isEnabled() || def.getMobId() == null) continue;
            trySpawn(def);
        }
    }

    private void trySpawn(RandomSpawnDefinition def) {
        List<Player> players = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            if (!def.getWorlds().isEmpty() && !def.getWorlds().contains(world.getName())) continue;
            players.addAll(world.getPlayers());
        }
        if (players.isEmpty()) return;

        Player anchor = players.get(ThreadLocalRandom.current().nextInt(players.size()));
        World world = anchor.getWorld();
        if (world == null) return;

        if (ThreadLocalRandom.current().nextDouble() > def.getChance()) return;

        // 数量上限
        int count = 0;
        for (LivingEntity e : world.getLivingEntities()) {
            String sid = e.getPersistentDataContainer().get(tag.key, PersistentDataType.STRING);
            if (sid != null && sid.equalsIgnoreCase(def.getId())) count++;
        }
        if (count >= def.getMaxMobsPerWorld()) return;

        // 随机区块距离
        int dist = ThreadLocalRandom.current().nextInt(def.getMinChunkDistance(), def.getMaxChunkDistance() + 1);
        int dx = ThreadLocalRandom.current().nextBoolean() ? dist : -dist;
        int dz = ThreadLocalRandom.current().nextBoolean() ? dist : -dist;
        Chunk chunk = world.getChunkAt(anchor.getLocation().clone().add(dx * 16, 0, dz * 16));
        if (!world.isChunkLoaded(chunk)) return; // 不强制加载区块

        // 群系过滤
        if (!def.getBiomes().isEmpty()) {
            Biome biome = world.getBiome(chunk.getBlock(7, 64, 7).getLocation());
            String biomeName = biome.getKey().getKey().toUpperCase(Locale.ROOT);
            boolean match = false;
            for (String allowed : def.getBiomes()) {
                if (biomeName.contains(allowed.toUpperCase(Locale.ROOT))) { match = true; break; }
            }
            if (!match) return;
        }

        // 随机列：找最高可生成位置
        int x = chunk.getX() * 16 + ThreadLocalRandom.current().nextInt(16);
        int z = chunk.getZ() * 16 + ThreadLocalRandom.current().nextInt(16);
        int highest = world.getHighestBlockYAt(x, z);
        Location loc = new Location(world, x + 0.5, highest + 1, z + 0.5);
        Material floor = world.getBlockAt(x, highest, z).getType();
        if (floor == Material.WATER || floor == Material.LAVA || !floor.isSolid()) return; // 不在水面/空中生成

        MobType mobType = mobManager.getMobType(def.getMobId());
        if (mobType == null) return;
        LivingEntity spawned = mobManager.spawn(mobType, loc);
        if (spawned == null) return;

        spawned.getPersistentDataContainer().set(tag.key, PersistentDataType.STRING, def.getId());

        // 等级
        if (def.getMaxLevel() > 0) {
            int lvl = ThreadLocalRandom.current().nextInt(def.getMinLevel(), def.getMaxLevel() + 1);
            if (lvl > 0) SetLevelMechanic.applyLevel(spawned, lvl);
        }

        // 消失策略
        switch (def.getDespawn()) {
            case "NEVER" -> spawned.setRemoveWhenFarAway(false);
            case "CHUNK" -> spawned.setRemoveWhenFarAway(true);
            default -> spawned.setRemoveWhenFarAway(true); // DESPAWN
        }
    }
}
