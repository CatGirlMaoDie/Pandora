package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标到世界出生点的距离范围。distance|d=min~max */
public class DistanceFromSpawnCondition implements Condition {
    private final double minSq, maxSq;

    public DistanceFromSpawnCondition(double min, double max) {
        double mn = Math.max(0, min);
        double mx = Math.max(mn, max);
        this.minSq = mn * mn;
        this.maxSq = mx * mx;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        var spawn = target.getWorld().getSpawnLocation();
        double distSq = target.getLocation().distanceSquared(spawn);
        return distSq >= minSq && distSq <= maxSq;
    }
}
