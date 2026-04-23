package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标下落速度范围（Y轴负速度）。speed|s=min;maxSpeed=max */
public class FallSpeedCondition implements Condition {
    private final double min, max;
    public FallSpeedCondition(double min, double max) { this.min = min; this.max = max; }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        double vy = target.getVelocity().getY();
        // 下落时 Y 为负，取绝对值
        double fallSpeed = Math.max(0, -vy);
        return fallSpeed >= min && fallSpeed <= max;
    }
}
