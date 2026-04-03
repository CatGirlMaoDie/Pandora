package org.gsn.pandora.spawner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Locale;

/**
 * 一个刷怪点的静态配置数据。只负责保存配置，不包含运行期状态。
 */
public class SpawnerDefinition {

    private final String id;
    private final String mobId;
    private final String worldName;
    private final double x;
    private final double y;
    private final double z;
    private final double radius;
    private final int maxMobs;
    private final long intervalTicks;
    private final int spawnAmount;
    private final boolean enabled;
    private final DespawnConfig despawnConfig;

    public SpawnerDefinition(String id,
                             String mobId,
                             String worldName,
                             double x,
                             double y,
                             double z,
                             double radius,
                             int maxMobs,
                             long intervalTicks,
                             int spawnAmount,
                             boolean enabled,
                             DespawnConfig despawnConfig) {
        this.id = id;
        this.mobId = mobId;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.maxMobs = maxMobs;
        this.intervalTicks = intervalTicks;
        this.spawnAmount = spawnAmount;
        this.enabled = enabled;
        this.despawnConfig = despawnConfig;
    }

    public String getId() {
        return id;
    }

    public String getMobId() {
        return mobId;
    }

    public String getWorldName() {
        return worldName;
    }

    public double getRadius() {
        return radius;
    }

    public int getMaxMobs() {
        return maxMobs;
    }

    public long getIntervalTicks() {
        return intervalTicks;
    }

    public int getSpawnAmount() {
        return spawnAmount;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public DespawnConfig getDespawnConfig() {
        return despawnConfig;
    }

    public Location getLocation() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }
        return new Location(world, x + 0.5, y, z + 0.5);
    }

    public String getKey() {
        return id == null ? null : id.toLowerCase(Locale.ROOT);
    }

    /**
     * 实体消失配置
     */
    public static class DespawnConfig {
        private final double distance;
        private final int delaySeconds;
        private final boolean removeWhenFarAway;

        public DespawnConfig(double distance, int delaySeconds, boolean removeWhenFarAway) {
            this.distance = distance;
            this.delaySeconds = delaySeconds;
            this.removeWhenFarAway = removeWhenFarAway;
        }

        public double getDistance() {
            return distance;
        }

        public int getDelaySeconds() {
            return delaySeconds;
        }

        public boolean isRemoveWhenFarAway() {
            return removeWhenFarAway;
        }
    }
}
