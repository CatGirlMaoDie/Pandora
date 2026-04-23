package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标当前生命值范围（绝对值，非百分比）。amount|a=min;maxAmount=max */
public class HealthCondition implements Condition {

    private final double min, max;

    public HealthCondition(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        double hp = target.getHealth();
        return hp >= min && hp <= max;
    }
}
