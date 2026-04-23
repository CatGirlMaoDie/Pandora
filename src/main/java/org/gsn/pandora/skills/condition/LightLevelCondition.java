package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标所在位置的光照等级范围。 */
public class LightLevelCondition implements Condition {

    private final int minLevel;
    private final int maxLevel;

    public LightLevelCondition(int minLevel, int maxLevel) {
        this.minLevel = Math.max(0, minLevel);
        this.maxLevel = Math.min(15, maxLevel);
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        int light = target.getLocation().getBlock().getLightLevel();
        return light >= minLevel && light <= maxLevel;
    }
}
