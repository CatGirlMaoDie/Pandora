package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标速度大小范围（velocity 为速度向量的长度）。 */
public class VelocityCondition implements Condition {

    private final double minVelocity;
    private final double maxVelocity;

    public VelocityCondition(double minVelocity, double maxVelocity) {
        this.minVelocity = minVelocity;
        this.maxVelocity = maxVelocity;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        double speed = target.getVelocity().length();
        return speed >= minVelocity && speed <= maxVelocity;
    }
}
