package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 将目标推离施法者（push）。
 * velocity|v|magnitude=1  velocityY|yvelocity|vy|yv=0
 */
public class PushMechanic implements Mechanic {

    private final double velocity;
    private final double velocityY;

    public PushMechanic(double velocity, double velocityY) {
        this.velocity = velocity;
        this.velocityY = velocityY;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (targets == null || targets.isEmpty()) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            Vector dir;
            if (caster != null && !caster.equals(target)) {
                dir = target.getLocation().toVector()
                    .subtract(caster.getLocation().toVector());
                dir.setY(0);
                if (dir.lengthSquared() > 0) dir.normalize();
            } else {
                dir = target.getLocation().getDirection().clone();
                dir.setY(0);
                if (dir.lengthSquared() > 0) dir.normalize();
            }
            dir.multiply(velocity);
            dir.setY(velocityY);
            target.setVelocity(dir);
            any = true;
        }
        return any;
    }
}
