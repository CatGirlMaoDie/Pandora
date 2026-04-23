package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标实体存活时间（tick）范围。time|t=min;maxTime=max */
public class TimeAliveCondition implements Condition {
    private final int min, max;
    public TimeAliveCondition(int min, int max) { this.min = min; this.max = max; }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        int ticks = target.getTicksLived();
        return ticks >= min && ticks <= max;
    }
}
