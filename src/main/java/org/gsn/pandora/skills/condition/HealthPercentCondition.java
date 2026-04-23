package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标当前生命值百分比范围（0-1）。percent 为最大值，minPercent 为最小值。 */
public class HealthPercentCondition implements Condition {

    private final double minPercent;
    private final double maxPercent;

    public HealthPercentCondition(double minPercent, double maxPercent) {
        this.minPercent = minPercent;
        this.maxPercent = maxPercent;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getMaxHealth() <= 0) return false;
        double percent = target.getHealth() / target.getMaxHealth();
        return percent >= minPercent && percent <= maxPercent;
    }
}
