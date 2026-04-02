package org.gsn.pandora.spawner;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.gsn.pandora.config.PandoraConfigService;
import org.gsn.pandora.mobs.MobManager;
import org.gsn.pandora.skills.SkillParser;
import org.gsn.pandora.script.ScriptingService;

import java.util.*;

/**
 * 负责从 spawners.yml 加载配置，并定时检查刷怪点进行刷怪。
 */
public class SpawnerManager {

    private interface SpawnerCondition {
        boolean test(Location center);
    }

    private final JavaPlugin plugin;
    private final PandoraConfigService configService;
    private final MobManager mobManager;
    private final SpawnerTag spawnerTag;
    private final ScriptingService scriptingService;

    private final Map<String, SpawnerDefinition> spawners = new HashMap<>();
    private final Map<String, Long> lastSpawnTime = new HashMap<>();
    private final Map<String, List<SpawnerCondition>> conditions = new HashMap<>();

    private BukkitTask task;

    public SpawnerManager(JavaPlugin plugin,
                          PandoraConfigService configService,
                          MobManager mobManager,
                          ScriptingService scriptingService) {
        this.plugin = plugin;
        this.configService = configService;
        this.mobManager = mobManager;
        this.spawnerTag = new SpawnerTag(plugin);
        this.scriptingService = scriptingService;
    }

    public void reload() {
        spawners.clear();
        lastSpawnTime.clear();
        conditions.clear();

        FileConfiguration config = configService.getSpawnersConfig();
        if (config == null) {
            plugin.getLogger().warning("spawners.yml 配置尚未加载。");
            return;
        }

        for (String id : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(id);
            if (section == null) {
                continue;
            }

            String mobId = section.getString("Mob");
            String worldName = section.getString("World", "world");
            double x = section.getDouble("X");
            double y = section.getDouble("Y");
            double z = section.getDouble("Z");
            double radius = section.getDouble("Radius", 5.0);
            int maxMobs = section.getInt("MaxMobs", 5);
            long intervalTicks = parseInterval(section.getString("Interval", "10s"));

            SpawnerDefinition def = new SpawnerDefinition(
                id,
                mobId,
                worldName,
                x,
                y,
                z,
                radius,
                maxMobs,
                intervalTicks
            );
            String key = def.getKey();
            if (key != null) {
                spawners.put(key, def);

                // 解析 Conditions
                List<String> rawConditions = section.getStringList("Conditions");
                List<SpawnerCondition> parsed = parseConditions(id, rawConditions);
                if (!parsed.isEmpty()) {
                    conditions.put(key, parsed);
                }
            }
        }

        plugin.getLogger().info("已加载 " + spawners.size() + " 个刷怪点。");
    }

    public void startTask() {
        stopTask();
        // 每 20 tick（1 秒）检查一次所有刷怪点
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::tick, 20L, 20L);
    }

    public void stopTask() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public long parseInterval(String raw) {
        if (raw == null || raw.isEmpty()) {
            return 200L; // 默认 10 秒
        }
        String s = raw.trim().toLowerCase(Locale.ROOT);
        try {
            if (s.endsWith("ms")) {
                // 毫秒 -> tick
                long ms = Long.parseLong(s.substring(0, s.length() - 2));
                return Math.max(1L, ms / 50L);
            }
            if (s.endsWith("s")) {
                long seconds = Long.parseLong(s.substring(0, s.length() - 1));
                return Math.max(1L, seconds * 20L);
            }
            // 直接按 tick 数处理
            long ticks = Long.parseLong(s);
            return Math.max(1L, ticks);
        } catch (NumberFormatException ex) {
            return 200L;
        }
    }

    private double parseDouble(String raw, double def) {
        if (raw == null || raw.isEmpty()) {
            return def;
        }
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException ex) {
            return def;
        }
    }

    private List<SpawnerCondition> parseConditions(String id, List<String> rawList) {
        List<SpawnerCondition> result = new ArrayList<>();
        if (rawList == null || rawList.isEmpty()) {
            return result;
        }
        for (String raw : rawList) {
            if (raw == null) {
                continue;
            }
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            String name = line;
            Map<String, String> args = Collections.emptyMap();
            int brace = line.indexOf('{');
            if (brace >= 0 && line.endsWith("}")) {
                name = line.substring(0, brace).trim();
                String inside = line.substring(brace + 1, line.length() - 1);
                args = SkillParser.parseArgs(inside);
            }
            String lower = name.toLowerCase(Locale.ROOT);

            switch (lower) {
                case "playersinradius": {
                    double radius = parseDouble(args.get("r"), 32.0);
                    int min = (int) parseDouble(args.get("min"), 1.0);
                    if (min < 0) {
                        min = 0;
                    }
                    final double r = radius;
                    final int minPlayers = min;
                    result.add(center -> {
                        if (center == null || center.getWorld() == null) {
                            return false;
                        }
                        int count = 0;
                        double rSq = r * r;
                        for (Player player : center.getWorld().getPlayers()) {
                            if (!player.isOnline()) {
                                continue;
                            }
                            if (!player.getWorld().equals(center.getWorld())) {
                                continue;
                            }
                            if (player.getLocation().distanceSquared(center) <= rSq) {
                                count++;
                            }
                        }
                        return count >= minPlayers;
                    });
                    break;
                }
                case "time": {
                    long from = (long) parseDouble(args.get("from"), 0.0);
                    long to = (long) parseDouble(args.get("to"), 23999.0);
                    from = Math.max(0L, Math.min(23999L, from));
                    to = Math.max(0L, Math.min(23999L, to));
                    final long f = from;
                    final long t = to;
                    result.add(center -> {
                        if (center == null || center.getWorld() == null) {
                            return false;
                        }
                        long time = center.getWorld().getTime();
                        if (f <= t) {
                            return time >= f && time <= t;
                        }
                        // 跨越 0 点的区间，例如 23000-1000
                        return time >= f || time <= t;
                    });
                    break;
                }
                case "js": {
                    if (scriptingService == null || !scriptingService.isEnabled()) {
                        break;
                    }
                    String file = args.get("file");
                    if (file == null || file.isEmpty()) {
                        break;
                    }
                    String func = args.getOrDefault("func", "canSpawn");
                    final String scriptFile = file;
                    final String funcName = func;
                    final String spawnerId = id;
                    result.add(center -> {
                        if (center == null || center.getWorld() == null) {
                            return false;
                        }
                        Map<String, Object> bindings = new HashMap<>();
                        bindings.put("center", center);
                        bindings.put("spawnerId", spawnerId);
                        Object r = scriptingService.call("spawners/" + scriptFile, funcName, bindings);
                        return r instanceof Boolean && (Boolean) r;
                    });
                    break;
                }
                default:
                    plugin.getLogger().warning("未知 Spawner 条件 '" + name + "' 于 " + id + "，已忽略。");
                    break;
            }
        }
        return result;
    }

    private void tick() {
        if (spawners.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();
        for (SpawnerDefinition def : spawners.values()) {
            String key = def.getKey();
            if (key == null) {
                continue;
            }
            long intervalMs = def.getIntervalTicks() * 50L;
            long last = lastSpawnTime.getOrDefault(key, 0L);
            if (now - last < intervalMs) {
                continue;
            }
            trySpawn(def);
            lastSpawnTime.put(key, now);
        }
    }

    private void trySpawn(SpawnerDefinition def) {
        Location loc = def.getLocation();
        if (loc == null || loc.getWorld() == null) {
            return;
        }

        String key = def.getKey();
        if (key != null) {
            List<SpawnerCondition> conds = conditions.get(key);
            if (conds != null) {
                for (SpawnerCondition cond : conds) {
                    if (!cond.test(loc)) {
                        return;
                    }
                }
            }
        }

        // 附近由该 Spawner 生成的实体数量
        int count = 0;
        double r = def.getRadius();
        double rY = Math.max(3.0, r); // 垂直高度给一个合理宽度
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, r, rY, r)) {
            if (!(entity instanceof LivingEntity)) {
                continue;
            }
            LivingEntity living = (LivingEntity) entity;
            String spawnerId = living.getPersistentDataContainer().get(spawnerTag.getKey(), PersistentDataType.STRING);
            if (spawnerId != null && spawnerId.equalsIgnoreCase(def.getId())) {
                count++;
            }
        }
        if (count >= def.getMaxMobs()) {
            return;
        }

        if (def.getMobId() == null || def.getMobId().isEmpty()) {
            return;
        }

        if (mobManager != null) {
            LivingEntity spawned = mobManager.spawn(def.getMobId(), loc);
            if (spawned != null) {
                spawned.getPersistentDataContainer().set(
                    spawnerTag.getKey(), PersistentDataType.STRING, def.getId()
                );
            }
        }
    }

    public Collection<SpawnerDefinition> getAllSpawners() {
        return Collections.unmodifiableCollection(spawners.values());
    }

    public SpawnerDefinition getSpawner(String id) {
        if (id == null) {
            return null;
        }
        return spawners.get(id.toLowerCase(Locale.ROOT));
    }

    public SpawnerDefinition createSpawner(String id,
                                           String mobId,
                                           Location location,
                                           double radius,
                                           int maxMobs,
                                           long intervalTicks) {
        if (id == null || mobId == null || location == null || location.getWorld() == null) {
            return null;
        }
        String worldName = location.getWorld().getName();
        SpawnerDefinition def = new SpawnerDefinition(
            id,
            mobId,
            worldName,
            location.getX(),
            location.getY(),
            location.getZ(),
            radius,
            maxMobs,
            intervalTicks
        );
        String key = def.getKey();
        if (key == null) {
            return null;
        }
        spawners.put(key, def);
        return def;
    }

    public boolean removeSpawner(String id) {
        if (id == null) {
            return false;
        }
        String key = id.toLowerCase(Locale.ROOT);
        SpawnerDefinition removed = spawners.remove(key);
        lastSpawnTime.remove(key);
        conditions.remove(key);
        return removed != null;
    }
}
