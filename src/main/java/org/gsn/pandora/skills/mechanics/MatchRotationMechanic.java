package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 让施法者朝向目标位置旋转。
 * location|target|to|loc|l|coordinates|c=x,y,z
 */
public class MatchRotationMechanic implements Mechanic {

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (caster == null || targets == null || targets.isEmpty()) return false;
        LivingEntity dest = targets.get(0);
        if (dest == null) return false;

        Location from = caster.getLocation();
        Location to = dest.getLocation();
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dz = to.getZ() - from.getZ();
        float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        float pitch = (float) Math.toDegrees(Math.atan2(-dy, Math.sqrt(dx * dx + dz * dz)));
        from.setYaw(yaw);
        from.setPitch(pitch);
        caster.teleport(from);
        return true;
    }
}
