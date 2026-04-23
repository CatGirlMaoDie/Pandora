package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 在目标位置播放末影光束视觉效果（通过粒子模拟）。
 * duration|d=60  yOffset|yo|y=0
 */
public class EffectEnderBeamMechanic implements Mechanic {

    private final int durationTicks;
    private final double yOffset;

    public EffectEnderBeamMechanic(int durationTicks, double yOffset) {
        this.durationTicks = Math.max(1, durationTicks);
        this.yOffset = yOffset;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (caster == null || targets == null || targets.isEmpty()) return false;
        LivingEntity dest = targets.get(0);
        if (dest == null || dest.getWorld() == null) return false;

        Location from = caster.getLocation().clone().add(0, yOffset, 0);
        Location to = dest.getLocation().clone().add(0, yOffset, 0);

        // 沿路径生成粒子
        double dist = from.distance(to);
        if (dist == 0) return false;
        int steps = (int) (dist * 4);
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            Location point = from.clone().add(
                (to.getX() - from.getX()) * t,
                (to.getY() - from.getY()) * t,
                (to.getZ() - from.getZ()) * t);
            from.getWorld().spawnParticle(Particle.DRAGON_BREATH, point, 1, 0, 0, 0, 0);
        }
        return true;
    }
}
