package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测施法者的目标是否不在指定距离内（targetWithin 的反向）。distance|d=16 */
public class TargetNotWithinCondition implements Condition {
    private final double distanceSq;
    public TargetNotWithinCondition(double distance) {
        double d = Math.max(0, distance);
        this.distanceSq = d * d;
    }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        LivingEntity caster = context.getCaster();
        LivingEntity ctxTarget = context.getTarget();
        if (caster == null || ctxTarget == null) return true;
        if (!caster.getWorld().equals(ctxTarget.getWorld())) return true;
        return caster.getLocation().distanceSquared(ctxTarget.getLocation()) > distanceSq;
    }
}
