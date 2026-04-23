package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 对目标造成伤害并治疗施法者（吸血）。
 * damage|dmg|d=1  heal|h=1
 */
public class ConsumeMechanic implements Mechanic {

    private final double damage;
    private final double heal;

    public ConsumeMechanic(double damage, double heal) {
        this.damage = damage;
        this.heal = heal;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) return false;
        LivingEntity caster = context.getCaster();
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            target.damage(damage, caster);
            // 治疗施法者
            if (caster != null && !caster.isDead() && heal != 0) {
                double newHp = Math.max(0, Math.min(caster.getMaxHealth(), caster.getHealth() + heal));
                caster.setHealth(newHp);
            }
            any = true;
        }
        return any;
    }
}
