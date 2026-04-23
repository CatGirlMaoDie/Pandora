package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 让目标朝向施法者（或指定方向）。
 * headOnly|ho=false  immediately|i=true
 */
public class LookMechanic implements Mechanic {

    private final boolean headOnly;

    public LookMechanic(boolean headOnly) {
        this.headOnly = headOnly;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (caster == null || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            Vector dir = caster.getLocation().toVector()
                .subtract(target.getLocation().toVector()).normalize();
            org.bukkit.Location newLoc = target.getLocation().clone();
            // 计算 yaw/pitch
            double x = dir.getX(), y = dir.getY(), z = dir.getZ();
            float yaw = (float) Math.toDegrees(Math.atan2(-x, z));
            float pitch = (float) Math.toDegrees(Math.atan2(-y, Math.sqrt(x * x + z * z)));
            newLoc.setYaw(yaw);
            if (!headOnly) newLoc.setPitch(pitch);
            target.teleport(newLoc);
            any = true;
        }
        return any;
    }
}
