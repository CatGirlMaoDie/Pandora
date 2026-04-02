package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

public class HealthAboveCondition implements Condition {

    private final double thresholdPercent; // 0-1

    public HealthAboveCondition(double thresholdPercent) {
        this.thresholdPercent = thresholdPercent;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getMaxHealth() <= 0) {
            return false;
        }
        double percent = target.getHealth() / target.getMaxHealth();
        return percent > thresholdPercent;
    }
}
