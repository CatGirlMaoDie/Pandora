package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 按百分比治疗目标。
 * multiplier|m|a|amount=0.1 (1=100%)
 */
public class HealPercentMechanic implements Mechanic {

    private final double multiplier;

    public HealPercentMechanic(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            double max = target.getMaxHealth();
            double heal = max * multiplier;
            target.setHealth(Math.min(max, target.getHealth() + heal));
            any = true;
        }
        return any;
    }
}
