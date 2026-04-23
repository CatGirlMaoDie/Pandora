package org.gsn.pandora.skills.condition;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标所在区块内的生物数量范围。amount|a=min;maxAmount=max */
public class MobsInChunkCondition implements Condition {
    private final int min, max;
    public MobsInChunkCondition(int min, int max) { this.min = min; this.max = max; }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        var chunk = target.getLocation().getChunk();
        long count = 0;
        for (Entity e : chunk.getEntities()) {
            if (e instanceof LivingEntity && !(e instanceof org.bukkit.entity.Player)) count++;
        }
        return count >= min && count <= max;
    }
}
