package org.gsn.pandora.skills.condition;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/**
 * 检测目标到已追踪位置（存入 PDC）的距离范围。
 * 追踪位置通过 pandora:tracked_x/y/z 存储。
 * distance|d=min;maxDistance=max
 */
public class DistanceFromTrackedLocationCondition implements Condition {

    private static final NamespacedKey TX = new NamespacedKey("pandora", "tracked_x");
    private static final NamespacedKey TY = new NamespacedKey("pandora", "tracked_y");
    private static final NamespacedKey TZ = new NamespacedKey("pandora", "tracked_z");

    private final double minSq, maxSq;

    public DistanceFromTrackedLocationCondition(double min, double max) {
        double mn = Math.max(0, min);
        double mx = Math.max(mn, max);
        this.minSq = mn * mn;
        this.maxSq = mx * mx;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        var pdc = target.getPersistentDataContainer();
        Double x = pdc.get(TX, PersistentDataType.DOUBLE);
        Double y = pdc.get(TY, PersistentDataType.DOUBLE);
        Double z = pdc.get(TZ, PersistentDataType.DOUBLE);
        if (x == null || y == null || z == null) return false;
        Location tracked = new Location(target.getWorld(), x, y, z);
        double distSq = target.getLocation().distanceSquared(tracked);
        return distSq >= minSq && distSq <= maxSq;
    }
}
