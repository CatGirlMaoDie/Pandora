package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

public class DamageMechanic implements Mechanic {

    private final double amount;

    public DamageMechanic(double amount) {
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
            target.damage(amount, context.getCaster());
            any = true;
        }
        return any;
    }
}
