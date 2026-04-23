package org.gsn.pandora.skills.mechanics;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 基于武器攻击力造成物理伤害。
 * multiplier|m=1.0  forcedDamage|fd=0（>0 时忽略武器属性直接用此值）
 */
public class PhysicalDamageMechanic implements Mechanic {

    private final double multiplier;
    private final double forcedDamage;

    public PhysicalDamageMechanic(double multiplier, double forcedDamage) {
        this.multiplier = multiplier;
        this.forcedDamage = forcedDamage;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) return false;
        LivingEntity caster = context.getCaster();
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            double dmg;
            if (forcedDamage > 0) {
                dmg = forcedDamage;
            } else if (caster != null) {
                AttributeInstance atk = caster.getAttribute(Attribute.ATTACK_DAMAGE);
                dmg = (atk != null ? atk.getValue() : 1.0) * multiplier;
            } else {
                dmg = multiplier;
            }
            target.damage(dmg, caster);
            any = true;
        }
        return any;
    }
}
