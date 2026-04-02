package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

public class VelocityMechanic implements Mechanic {

    private final double y;

    public VelocityMechanic(double y) {
        this.y = y;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) {
            return false;
        }
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) {
                continue;
            }
            Vector velocity = target.getVelocity();
            velocity.setY(y);
            target.setVelocity(velocity);
            any = true;
        }
        return any;
    }
}
