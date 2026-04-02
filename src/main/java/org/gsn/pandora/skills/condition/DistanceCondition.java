package org.gsn.pandora.skills.condition;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

public class DistanceCondition implements Condition {

    private final double minSquared;
    private final double maxSquared;

    public DistanceCondition(double min, double max) {
        double minClamped = Math.max(0.0, min);
        double maxClamped = max < minClamped ? minClamped : max;
        this.minSquared = minClamped * minClamped;
        this.maxSquared = maxClamped * maxClamped;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.isDead()) {
            return false;
        }
        Location origin = context.getOrigin();
        if (origin == null || origin.getWorld() == null) {
            return false;
        }
        if (!origin.getWorld().equals(target.getWorld())) {
            return false;
        }
        double distanceSquared = origin.distanceSquared(target.getLocation());
        return distanceSquared >= minSquared && distanceSquared <= maxSquared;
    }
}
