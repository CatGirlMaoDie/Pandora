package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测目标最后一次受伤的伤害类型。damagecause|cause|c=... 支持逗号分隔多个 */
public class LastDamageCauseCondition implements Condition {
    private final Set<DamageCause> causes;

    public LastDamageCauseCondition(String causeList) {
        this.causes = Arrays.stream(causeList.split(","))
            .map(s -> {
                try { return DamageCause.valueOf(s.trim().toUpperCase(Locale.ROOT)); }
                catch (IllegalArgumentException e) { return null; }
            })
            .filter(c -> c != null)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || causes.isEmpty()) return false;
        var lastDmg = target.getLastDamageCause();
        if (lastDmg == null) return false;
        return causes.contains(lastDmg.getCause());
    }
}
