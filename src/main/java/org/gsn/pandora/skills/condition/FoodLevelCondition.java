package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标玩家食物等级范围（0-20）。amount|a=minLevel;maxLevel=20 */
public class FoodLevelCondition implements Condition {
    private final int min, max;
    public FoodLevelCondition(int min, int max) { this.min = min; this.max = max; }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (!(target instanceof Player p)) return false;
        int food = p.getFoodLevel();
        return food >= min && food <= max;
    }
}
