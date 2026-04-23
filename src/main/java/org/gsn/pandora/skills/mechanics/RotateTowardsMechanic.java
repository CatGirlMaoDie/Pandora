package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 将目标旋转到指定 yaw/pitch。
 * yaw|y=...  pitch|p=...  relative|rel|r=false
 */
public class RotateTowardsMechanic implements Mechanic {

    private final Float yaw;
    private final Float pitch;
    private final boolean relative;

    public RotateTowardsMechanic(Float yaw, Float pitch, boolean relative) {
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
            var loc = target.getLocation().clone();
            if (yaw != null) loc.setYaw(relative ? loc.getYaw() + yaw : yaw);
            if (pitch != null) loc.setPitch(relative ? loc.getPitch() + pitch : pitch);
            target.teleport(loc);
            any = true;
        }
        return any;
    }
}
