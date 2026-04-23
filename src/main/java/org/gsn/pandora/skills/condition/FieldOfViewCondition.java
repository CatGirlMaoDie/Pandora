package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标是否在施法者视野角度内。angle 为半角度数（0-180）。 */
public class FieldOfViewCondition implements Condition {

    private final double halfAngleRadians;

    public FieldOfViewCondition(double angleDegrees) {
        double clamped = Math.max(0.0, Math.min(180.0, angleDegrees));
        this.halfAngleRadians = Math.toRadians(clamped / 2.0);
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        LivingEntity caster = context.getCaster();
        if (caster == null || target == null) return false;
        Vector dir = caster.getLocation().getDirection().normalize();
        Vector toTarget = target.getLocation().toVector()
            .subtract(caster.getLocation().toVector());
        if (toTarget.lengthSquared() == 0) return true;
        toTarget.normalize();
        return dir.angle(toTarget) <= halfAngleRadians;
    }
}
