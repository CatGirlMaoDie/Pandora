package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 在施法者和目标之间显示守卫者光束效果。
 * duration|maxduration|md|d=400  startYOffset|syo=1  targetYOffset|tyo=1
 */
public class EffectGuardianBeamMechanic implements Mechanic {

    private final int durationTicks;
    private final double startYOffset;
    private final double targetYOffset;

    public EffectGuardianBeamMechanic(int durationTicks, double startYOffset, double targetYOffset) {
        this.durationTicks = Math.max(1, durationTicks);
        this.startYOffset = startYOffset;
        this.targetYOffset = targetYOffset;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (caster == null || targets == null || targets.isEmpty()) return false;
        LivingEntity dest = targets.get(0);
        if (dest == null || dest.getWorld() == null) return false;

        // 在施法者位置生成临时守卫者并设置目标，产生光束视觉效果
        Location spawnLoc = caster.getLocation().clone().add(0, startYOffset, 0);
        Guardian guardian = spawnLoc.getWorld().spawn(spawnLoc, Guardian.class);
        guardian.setTarget(dest);
        guardian.setInvisible(true);
        guardian.setAI(false);
        guardian.setGravity(false);
        guardian.setSilent(true);

        org.gsn.pandora.util.FoliaScheduler.runEntityTaskLater(
            context.getPlugin(), guardian, guardian::remove, durationTicks);
        return true;
    }
}
