package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标玩家经验等级范围（绝对等级）。level|l=min;maxLevel=max */
public class LevelCondition implements Condition {
    private final int min, max;
    public LevelCondition(int min, int max) { this.min = min; this.max = max; }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (!(target instanceof Player p)) return false;
        int level = p.getLevel();
        return level >= min && level <= max;
    }
}
