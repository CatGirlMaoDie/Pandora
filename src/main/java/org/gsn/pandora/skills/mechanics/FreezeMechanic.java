package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/** 冻结目标（冰霜效果），持续指定 tick 数，最大 170。ticks|duration|d|t=60 */
public class FreezeMechanic implements Mechanic {

    private static final int MAX_FREEZE_TICKS = 170;
    private final int ticks;

    public FreezeMechanic(int ticks) {
        this.ticks = Math.min(MAX_FREEZE_TICKS, Math.max(0, ticks));
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            target.setFreezeTicks(ticks);
            any = true;
        }
        return any;
    }
}
