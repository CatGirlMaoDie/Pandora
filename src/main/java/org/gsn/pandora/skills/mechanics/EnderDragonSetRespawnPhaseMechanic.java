package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 触发末影龙重生流程。phase|p 参数保留兼容性但暂不使用。
 */
public class EnderDragonSetRespawnPhaseMechanic implements Mechanic {

    public EnderDragonSetRespawnPhaseMechanic(String phaseName) {
        // phaseName 保留参数兼容性，实际调用 initiateRespawn()
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null) continue;
            var battle = target.getWorld().getEnderDragonBattle();
            if (battle == null) continue;
            battle.initiateRespawn();
            any = true;
        }
        return any;
    }
}

