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

    public SpawnerDefinition(String id,
                             String mobId,
                             String worldName,
                             double x,
                             double y,
                             double z,
                             double radius,
                             int maxMobs,
                             long intervalTicks) {
        this.id = id;
        this.mobId = mobId;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.maxMobs = maxMobs;
        this.intervalTicks = intervalTicks;
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
}
