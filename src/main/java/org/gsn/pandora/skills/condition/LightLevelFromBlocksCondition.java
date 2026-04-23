package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标位置仅来自方块的光照等级（不含天空光）。level|l=min;maxLevel=max */
public class LightLevelFromBlocksCondition implements Condition {
    private final int min, max;
    public LightLevelFromBlocksCondition(int min, int max) {
        this.min = Math.max(0, min);
        this.max = Math.min(15, max);
    }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        int light = target.getLocation().getBlock().getLightFromBlocks();
        return light >= min && light <= max;
    }
}
