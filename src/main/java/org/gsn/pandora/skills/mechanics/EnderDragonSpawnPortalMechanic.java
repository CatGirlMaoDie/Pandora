package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 生成末影龙出口传送门。withPortals|wp|p=false
 */
public class EnderDragonSpawnPortalMechanic implements Mechanic {

    private final boolean withPortals;

    public EnderDragonSpawnPortalMechanic(boolean withPortals) {
        this.withPortals = withPortals;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof EnderDragon dragon)) continue;
            // Paper API: 通过设置阶段触发传送门生成
            dragon.setPhase(EnderDragon.Phase.DYING);
            any = true;
        }
        return any;
    }
}
