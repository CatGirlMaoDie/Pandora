package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

public class HealMechanic implements Mechanic {

    private final double amount;

    public HealMechanic(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) {
            return false;
        }
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) {
                continue;
            }
            double max = target.getMaxHealth();
            double newHealth = Math.min(max, target.getHealth() + amount);
            target.setHealth(newHealth);
            any = true;
        }
        return any;
    }
}
