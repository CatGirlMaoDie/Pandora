package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 使施法者向目标反方向冲刺脱离（disengage）。
 * velocity|v|magnitude=1 水平速度；velocityY|yvelocity|vy|yv=0.5 垂直速度。
 */
public class DisengageMechanic implements Mechanic {

    private final double velocity;
    private final double velocityY;

    public DisengageMechanic(double velocity, double velocityY) {
        this.velocity = velocity;
        this.velocityY = velocityY;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (caster == null) return false;
        LivingEntity target = context.getTarget();
        Vector dir;
        if (target != null) {
            dir = caster.getLocation().toVector()
                .subtract(target.getLocation().toVector());
            dir.setY(0);
            if (dir.lengthSquared() > 0) dir.normalize();
        } else {
            dir = caster.getLocation().getDirection().multiply(-1);
            dir.setY(0);
            if (dir.lengthSquared() > 0) dir.normalize();
        }
        dir.multiply(velocity);
        dir.setY(velocityY);
        caster.setVelocity(dir);
        return true;
    }
}
