package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测施法者的当前目标（target）是否在指定距离内。 */
public class TargetWithinCondition implements Condition {

    private final double distanceSquared;

    public TargetWithinCondition(double distance) {
        double d = Math.max(0.0, distance);
        this.distanceSquared = d * d;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        LivingEntity caster = context.getCaster();
        LivingEntity ctxTarget = context.getTarget();
        if (caster == null || ctxTarget == null) return false;
        if (!caster.getWorld().equals(ctxTarget.getWorld())) return false;
        return caster.getLocation().distanceSquared(ctxTarget.getLocation()) <= distanceSquared;
    }
}
