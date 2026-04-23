package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 设置目标实体的朝向（yaw/pitch）。
 * yaw|y=0  pitch|p=0  relative|rel|r=false
 */
public class SetRotationMechanic implements Mechanic {

    private final float yaw;
    private final float pitch;
    private final boolean relative;

    public SetRotationMechanic(float yaw, float pitch, boolean relative) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.relative = relative;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            org.bukkit.Location loc = target.getLocation().clone();
            if (relative) {
                loc.setYaw(loc.getYaw() + yaw);
                loc.setPitch(loc.getPitch() + pitch);
            } else {
                loc.setYaw(yaw);
                loc.setPitch(pitch);
            }
            target.teleport(loc);
            any = true;
        }
        return any;
    }
}
