package org.gsn.pandora.spawner;

import java.util.List;
import java.util.Locale;

/**
 * 一个随机生成配置（对应 Wiki「随机生成」机制）：
 * 在指定世界内按生态群系/条件随机生成自定义实体，类似原版怪物刷新。
 */
public class RandomSpawnDefinition {

    private final String id;
    private final String mobId;
    private final List<String> worlds;          // 允许的世界，空 = 所有世界
    private final List<String> biomes;          // 允许的群系（大写匹配），空 = 所有群系
    private final int minChunkDistance;         // 距玩家最小区块距离
    private final int maxChunkDistance;         // 距玩家最大区块距离
    private final int maxMobsPerWorld;          // 每世界此类型最大同时存在数
    private final double chance;                // 每次尝试生成概率 0-1
    private final int minLevel;                 // 生成的实体等级范围
    private final int maxLevel;
    private final String despawn;               // CHUNK(区块卸载消失)/DESPAWN(远离消失)/NEVER
    private final boolean enabled;

    public RandomSpawnDefinition(String id, String mobId, List<String> worlds, List<String> biomes,
                                 int minChunkDistance, int maxChunkDistance, int maxMobsPerWorld,
                                 double chance, int minLevel, int maxLevel, String despawn, boolean enabled) {
        this.id = id;
        this.mobId = mobId;
        this.worlds = worlds == null ? List.of() : worlds;
        this.biomes = biomes == null ? List.of() : biomes;
        this.minChunkDistance = Math.max(0, minChunkDistance);
        this.maxChunkDistance = Math.max(minChunkDistance, maxChunkDistance);
        this.maxMobsPerWorld = Math.max(1, maxMobsPerWorld);
        this.chance = Math.max(0.0, Math.min(1.0, chance));
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.despawn = despawn == null ? "DESPAWN" : despawn.toUpperCase(Locale.ROOT);
        this.enabled = enabled;
    }

    public String getId() { return id; }
    public String getMobId() { return mobId; }
    public List<String> getWorlds() { return worlds; }
    public List<String> getBiomes() { return biomes; }
    public int getMinChunkDistance() { return minChunkDistance; }
    public int getMaxChunkDistance() { return maxChunkDistance; }
    public int getMaxMobsPerWorld() { return maxMobsPerWorld; }
    public double getChance() { return chance; }
    public int getMinLevel() { return minLevel; }
    public int getMaxLevel() { return maxLevel; }
    public String getDespawn() { return despawn; }
    public boolean isEnabled() { return enabled; }
}
