package org.gsn.pandora.skills.condition;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标所在世界的生物总数范围。amount|a=min;maxAmount=max */
public class MobsInWorldCondition implements Condition {
    private final int min, max;

    public MobsInWorldCondition(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        long count = target.getWorld().getEntities().stream()
            .filter(e -> e instanceof LivingEntity && !(e instanceof org.bukkit.entity.Player))
            .count();
        return count >= min && count <= max;
    }
}
