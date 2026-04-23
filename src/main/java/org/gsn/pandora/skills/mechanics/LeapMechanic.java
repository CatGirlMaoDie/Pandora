package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 让目标向施法者方向跳跃冲刺。
 * velocity|v=1.0  noise|n=0（噪声/偏差）
 */
public class LeapMechanic implements Mechanic {

    private final double velocity;
    private final double noise;

    public LeapMechanic(double velocity, double noise) {
        this.velocity = velocity;
        this.noise = Math.max(0, noise);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            Vector dir;
            if (caster != null && !caster.equals(target)) {
                dir = caster.getLocation().toVector()
                    .subtract(target.getLocation().toVector()).normalize();
            } else {
                dir = target.getLocation().getDirection().normalize();
            }
            if (noise > 0) {
                ThreadLocalRandom rng = ThreadLocalRandom.current();
                dir.add(new Vector(
                    (rng.nextDouble() * 2 - 1) * noise,
                    (rng.nextDouble() * 2 - 1) * noise,
                    (rng.nextDouble() * 2 - 1) * noise));
                dir.normalize();
            }
            target.setVelocity(dir.multiply(velocity));
            any = true;
        }
        return any;
    }
}
