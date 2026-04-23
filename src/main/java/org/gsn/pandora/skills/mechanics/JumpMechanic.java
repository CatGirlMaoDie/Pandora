package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/** 给目标施加垂直速度（跳跃）。velocity|v=1 正值向上，负值向下。 */
public class JumpMechanic implements Mechanic {

    private final double velocity;

    public JumpMechanic(double velocity) {
        this.velocity = velocity;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            Vector v = target.getVelocity();
            v.setY(velocity);
            target.setVelocity(v);
            any = true;
        }
        return any;
    }
}
