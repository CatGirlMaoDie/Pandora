package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 让狼坐下或站起。sit|state|value=true
 */
public class WolfSitMechanic implements Mechanic {

    private final boolean sit;

    public WolfSitMechanic(boolean sit) {
        this.sit = sit;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Wolf)) continue;
            ((Wolf) target).setSitting(sit);
            any = true;
        }
        return any;
    }
}
