package org.gsn.pandora.spawner;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsn.pandora.config.PandoraConfigService;
import org.gsn.pandora.mobs.MobManager;
import org.gsn.pandora.skills.SkillParser;
import org.gsn.pandora.util.FoliaScheduler;
import org.gsn.pandora.util.PandoraTask;
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
    private final Map<UUID, Long> entityFarAwayTime = new HashMap<>();

    private PandoraTask task;
    private boolean globalEnabled = true;
    private long checkInterval = 20L;
    private String worldFilterMode = "whitelist";
    private List<String> worldFilterList = new ArrayList<>();

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
        entityFarAwayTime.clear();

        // 读取全局配置
        FileConfiguration mainConfig = plugin.getConfig();
        globalEnabled = mainConfig.getBoolean("spawning.enabled", true);
        checkInterval = mainConfig.getLong("spawning.check-interval", 20L);

        // 读取世界过滤配置
        worldFilterMode = mainConfig.getString("spawning.worlds.mode", "whitelist").toLowerCase(Locale.ROOT);
        worldFilterList = mainConfig.getStringList("spawning.worlds.list");
        if (worldFilterList.isEmpty()) {
            worldFilterList.add("world");
        }

        // 默认值
        String defaultInterval = mainConfig.getString("spawning.default-interval", "10s");
        int defaultSpawnAmount = mainConfig.getInt("spawning.default-spawn-amount", 1);
        int defaultMaxMobs = mainConfig.getInt("spawning.default-max-mobs", 5);
        double defaultRadius = mainConfig.getDouble("spawning.default-radius", 5.0);

        double defaultDespawnDistance = mainConfig.getDouble("spawning.despawn.distance", 128.0);
        int defaultDespawnDelay = mainConfig.getInt("spawning.despawn.delay", 30);
        boolean defaultDespawnEnabled = mainConfig.getBoolean("spawning.despawn.enabled", true);

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

            // 检查是否启用
            boolean enabled = section.getBoolean("Enabled", true);
            if (!enabled) {
                plugin.getLogger().info("刷怪点 " + id + " 已禁用，跳过加载。");
                continue;
            }

            String mobId = section.getString("Mob");
            String worldName = section.getString("World", "world");
            double x = section.getDouble("X");
            double y = section.getDouble("Y");
            double z = section.getDouble("Z");
            double radius = section.getDouble("Radius", defaultRadius);
            int maxMobs = section.getInt("MaxMobs", defaultMaxMobs);
            long intervalTicks = parseInterval(section.getString("Interval", defaultInterval));
            int spawnAmount = section.getInt("SpawnAmount", defaultSpawnAmount);

            // 读取 Despawn 配置
            SpawnerDefinition.DespawnConfig despawnConfig;
            if (section.contains("Despawn")) {
                ConfigurationSection despawnSection = section.getConfigurationSection("Despawn");
                if (despawnSection != null) {
                    double distance = despawnSection.getDouble("Distance", defaultDespawnDistance);
                    int delay = despawnSection.getInt("Delay", defaultDespawnDelay);
                    boolean removeWhenFarAway = despawnSection.getBoolean("RemoveWhenFarAway", defaultDespawnEnabled);
                    despawnConfig = new SpawnerDefinition.DespawnConfig(distance, delay, removeWhenFarAway);
                } else {
                    despawnConfig = new SpawnerDefinition.DespawnConfig(defaultDespawnDistance, defaultDespawnDelay, defaultDespawnEnabled);
                }
            } else {
                despawnConfig = new SpawnerDefinition.DespawnConfig(defaultDespawnDistance, defaultDespawnDelay, defaultDespawnEnabled);
            }

            SpawnerDefinition def = new SpawnerDefinition(
                id,
                mobId,
                worldName,
                x,
                y,
                z,
                radius,
                maxMobs,
                intervalTicks,
                spawnAmount,
                enabled,
                despawnConfig
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
        // 使用配置的检查间隔
        task = FoliaScheduler.runGlobalTaskTimer(plugin, this::tick, checkInterval, checkInterval);
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
                    if (min < 0) min = 0;
                    final double r = radius;
                    final double rSq = r * r;
                    final int minPlayers = min;
                    result.add(center -> {
                        if (center == null) return false;
                        org.bukkit.World world = center.getWorld();
                        if (world == null) return false;
                        int count = 0;
                        for (Player player : world.getPlayers()) {
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
                        if (center == null) return false;
                        org.bukkit.World world = center.getWorld();
                        if (world == null) return false;
                        long time = world.getTime();
                        if (f <= t) return time >= f && time <= t;
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
                    // bindings map 在 lambda 内创建，避免跨调用共享状态
                    result.add(center -> {
                        if (center == null || center.getWorld() == null) return false;
                        Map<String, Object> bindings = new HashMap<>(2);
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
        if (!globalEnabled || spawners.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();

        // 检查刷怪
        for (SpawnerDefinition def : spawners.values()) {
            if (!def.isEnabled()) {
                continue;
            }
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

        // 检查实体消失
        checkDespawn();
    }

    private void trySpawn(SpawnerDefinition def) {
        Location loc = def.getLocation();
        if (loc == null || loc.getWorld() == null) {
            return;
        }

        // 检查世界过滤
        if (!isWorldAllowed(loc.getWorld().getName())) {
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

        // 根据 SpawnAmount 生成多个实体
        int toSpawn = Math.min(def.getSpawnAmount(), def.getMaxMobs() - count);
        if (mobManager != null) {
            for (int i = 0; i < toSpawn; i++) {
                LivingEntity spawned = mobManager.spawn(def.getMobId(), loc);
                if (spawned != null) {
                    spawned.getPersistentDataContainer().set(
                        spawnerTag.getKey(), PersistentDataType.STRING, def.getId()
                    );

                    // 应用 Despawn 配置
                    SpawnerDefinition.DespawnConfig despawnConfig = def.getDespawnConfig();
                    if (despawnConfig != null) {
                        spawned.setRemoveWhenFarAway(despawnConfig.isRemoveWhenFarAway());
                    }
                }
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

        // 使用默认值
        int spawnAmount = plugin.getConfig().getInt("spawning.default-spawn-amount", 1);
        double despawnDistance = plugin.getConfig().getDouble("spawning.despawn.distance", 128.0);
        int despawnDelay = plugin.getConfig().getInt("spawning.despawn.delay", 30);
        boolean despawnEnabled = plugin.getConfig().getBoolean("spawning.despawn.enabled", true);

        SpawnerDefinition.DespawnConfig despawnConfig = new SpawnerDefinition.DespawnConfig(
            despawnDistance, despawnDelay, despawnEnabled
        );

        SpawnerDefinition def = new SpawnerDefinition(
            id,
            mobId,
            worldName,
            location.getX(),
            location.getY(),
            location.getZ(),
            radius,
            maxMobs,
            intervalTicks,
            spawnAmount,
            true,
            despawnConfig
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

    /**
     * 检查并处理实体的自然消失
     */
    private void checkDespawn() {
        long now = System.currentTimeMillis();

        for (SpawnerDefinition def : spawners.values()) {
            if (!def.isEnabled()) {
                continue;
            }

            SpawnerDefinition.DespawnConfig despawnConfig = def.getDespawnConfig();
            if (despawnConfig == null || !despawnConfig.isRemoveWhenFarAway()) {
                continue;
            }

            Location loc = def.getLocation();
            if (loc == null || loc.getWorld() == null) {
                continue;
            }

            double r = def.getRadius();
            double rY = Math.max(3.0, r);
            double despawnDistance = despawnConfig.getDistance();
            long despawnDelayMs = despawnConfig.getDelaySeconds() * 1000L;

            for (Entity entity : loc.getWorld().getNearbyEntities(loc, despawnDistance, rY, despawnDistance)) {
                if (!(entity instanceof LivingEntity)) {
                    continue;
                }
                LivingEntity living = (LivingEntity) entity;
                String spawnerId = living.getPersistentDataContainer().get(spawnerTag.getKey(), PersistentDataType.STRING);
                if (spawnerId == null || !spawnerId.equalsIgnoreCase(def.getId())) {
                    continue;
                }

                // 检查是否有玩家在附近
                boolean hasNearbyPlayer = false;
                for (Player player : loc.getWorld().getPlayers()) {
                    if (player.getLocation().distance(living.getLocation()) <= despawnDistance) {
                        hasNearbyPlayer = true;
                        break;
                    }
                }

                UUID entityId = living.getUniqueId();
                if (hasNearbyPlayer) {
                    // 有玩家在附近，重置计时
                    entityFarAwayTime.remove(entityId);
                } else {
                    // 没有玩家在附近，开始计时
                    Long farAwayTime = entityFarAwayTime.get(entityId);
                    if (farAwayTime == null) {
                        entityFarAwayTime.put(entityId, now);
                    } else if (now - farAwayTime >= despawnDelayMs) {
                        // 超过延迟时间，移除实体
                        living.remove();
                        entityFarAwayTime.remove(entityId);
                    }
                }
            }
        }
    }

    /**
     * 检查世界是否允许刷新
     */
    private boolean isWorldAllowed(String worldName) {
        if (worldName == null) {
            return false;
        }

        boolean inList = worldFilterList.contains(worldName);

        if ("whitelist".equals(worldFilterMode)) {
            // 白名单模式：只有在列表中的世界才允许
            return inList;
        } else if ("blacklist".equals(worldFilterMode)) {
            // 黑名单模式：不在列表中的世界才允许
            return !inList;
        }

        // 默认允许
        return true;
    }
}
