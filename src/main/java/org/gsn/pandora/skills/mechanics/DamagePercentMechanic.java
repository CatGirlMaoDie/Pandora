package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 对目标造成基于生命值百分比的伤害。
 * percent|p=0.1 (1=100%)  currentHealth|current|ch|c=false（true=基于当前血量，false=基于最大血量）
 */
public class DamagePercentMechanic implements Mechanic {

    private final double percent;
    private final boolean currentHealth;

    public DamagePercentMechanic(double percent, boolean currentHealth) {
        this.percent = percent;
        this.currentHealth = currentHealth;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) return false;
        LivingEntity caster = context.getCaster();
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            double base = currentHealth ? target.getHealth() : target.getMaxHealth();
            double dmg = base * percent;
            if (dmg > 0) {
                target.damage(dmg, caster);
                any = true;
            }
        }
        return any;
    }
}
