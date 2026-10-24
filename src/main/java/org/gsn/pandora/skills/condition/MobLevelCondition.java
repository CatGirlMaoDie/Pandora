package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测实体等级范围（配合 SetLevelMechanic 的等级系统）。 level|l=min;maxLevel=max */
public class MobLevelCondition implements Condition {

    private final int min;
    private final int max;

    public MobLevelCondition(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        int lvl = org.gsn.pandora.skills.mechanics.SetLevelMechanic.getLevel(target);
        return lvl >= min && lvl <= max;
    }
}
