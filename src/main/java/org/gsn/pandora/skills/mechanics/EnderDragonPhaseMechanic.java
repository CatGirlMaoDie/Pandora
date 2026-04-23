package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 设置末影龙当前阶段。phase|p=CIRCLING
 */
public class EnderDragonPhaseMechanic implements Mechanic {

    private final EnderDragon.Phase phase;

    public EnderDragonPhaseMechanic(String phaseName) {
        EnderDragon.Phase p = EnderDragon.Phase.CIRCLING;
        if (phaseName != null) {
            try { p = EnderDragon.Phase.valueOf(phaseName.toUpperCase(Locale.ROOT)); }
            catch (IllegalArgumentException ignored) {}
        }
        this.phase = p;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof EnderDragon dragon)) continue;
            dragon.setPhase(phase);
            any = true;
        }
        return any;
    }
}
