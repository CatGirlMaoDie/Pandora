package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/** 点燃目标，持续指定 tick 数。ticks|duration|d|t=60 */
public class IgniteMechanic implements Mechanic {

    private final int ticks;

    public IgniteMechanic(int ticks) {
        this.ticks = Math.max(0, ticks);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            target.setFireTicks(ticks);
            any = true;
        }
        return any;
    }
}
