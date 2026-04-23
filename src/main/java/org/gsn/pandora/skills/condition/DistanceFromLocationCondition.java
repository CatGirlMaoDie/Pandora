package org.gsn.pandora.skills.condition;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标到指定坐标的距离范围。x y z world distance|d=min;maxDistance=max */
public class DistanceFromLocationCondition implements Condition {
    private final double x, y, z;
    private final String worldName;
    private final double minSq, maxSq;

    public DistanceFromLocationCondition(double x, double y, double z, String worldName,
                                         double min, double max) {
        this.x = x; this.y = y; this.z = z;
        this.worldName = worldName;
        double mn = Math.max(0, min);
        double mx = Math.max(mn, max);
        this.minSq = mn * mn;
        this.maxSq = mx * mx;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        var world = worldName != null && !worldName.isBlank()
            ? Bukkit.getWorld(worldName) : target.getWorld();
        if (world == null || !world.equals(target.getWorld())) return false;
        Location loc = new Location(world, x, y, z);
        double distSq = target.getLocation().distanceSquared(loc);
        return distSq >= minSq && distSq <= maxSq;
    }
}
