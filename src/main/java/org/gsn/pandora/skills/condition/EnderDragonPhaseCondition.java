package org.gsn.pandora.skills.condition;

import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测末影龙当前阶段。type|types|t=CIRCLING,STRAFING,... */
public class EnderDragonPhaseCondition implements Condition {

    private final Set<EnderDragon.Phase> phases;

    public EnderDragonPhaseCondition(String typeList) {
        this.phases = Arrays.stream(typeList.split(","))
            .map(s -> {
                try { return EnderDragon.Phase.valueOf(s.trim().toUpperCase(Locale.ROOT)); }
                catch (IllegalArgumentException e) { return null; }
            })
            .filter(p -> p != null)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (!(target instanceof EnderDragon dragon) || phases.isEmpty()) return false;
        return phases.contains(dragon.getPhase());
    }
}
