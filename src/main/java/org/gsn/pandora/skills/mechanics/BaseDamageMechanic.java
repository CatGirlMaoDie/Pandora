package org.gsn.pandora.skills.mechanics;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 以施法者基础攻击力的倍数造成伤害。
 * multiplier|m=1.0
 */
public class BaseDamageMechanic implements Mechanic {

    private final double multiplier;

    public BaseDamageMechanic(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) return false;
        LivingEntity caster = context.getCaster();
        double baseDmg = 1.0;
        if (caster != null) {
            AttributeInstance atk = caster.getAttribute(Attribute.ATTACK_DAMAGE);
            if (atk != null) baseDmg = atk.getBaseValue();
        }
        double dmg = baseDmg * multiplier;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            target.damage(dmg, caster);
            any = true;
        }
        return any;
    }
}
