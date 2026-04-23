package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测当前伤害事件的伤害量范围（在 onHurt 触发器中有效）。amount|a=min;maxAmount=max */
public class DamageAmountCondition implements Condition {
    private final double min, max;
    public DamageAmountCondition(double min, double max) { this.min = min; this.max = max; }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        var lastDmg = target.getLastDamageCause();
        if (lastDmg == null) return false;
        double dmg = lastDmg.getDamage();
        return dmg >= min && dmg <= max;
    }
}
