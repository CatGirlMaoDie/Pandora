package org.gsn.pandora.skills.condition;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标脚下农田的湿润度（0-7）。level|l|m|moistness=min;maxLevel=max */
public class MoistureLevelCondition implements Condition {
    private final int min, max;
    public MoistureLevelCondition(int min, int max) { this.min = min; this.max = max; }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        Block block = target.getLocation().clone().subtract(0, 1, 0).getBlock();
        if (!(block.getBlockData() instanceof org.bukkit.block.data.type.Farmland farmland)) return false;
        int moisture = farmland.getMoisture();
        return moisture >= min && moisture <= max;
    }
}
