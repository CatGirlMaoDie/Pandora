package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标玩家饱食度范围（0-20）。amount|a|food|f|saturation|s=min;maxSaturation=max */
public class FoodSaturationCondition implements Condition {
    private final float min, max;
    public FoodSaturationCondition(float min, float max) { this.min = min; this.max = max; }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (!(target instanceof Player p)) return false;
        float sat = p.getSaturation();
        return sat >= min && sat <= max;
    }
}
