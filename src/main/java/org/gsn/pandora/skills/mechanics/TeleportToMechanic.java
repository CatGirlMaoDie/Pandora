package org.gsn.pandora.skills.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 传送目标到指定坐标。
 * location|target|to|loc|l|coordinates|c=x,y,z
 * world|w=...  yaw|y=0  pitch|p=0  relative|m=false
 */
public class TeleportToMechanic implements Mechanic {

    private final String worldName;
    private final double x, y, z;
    private final float yaw, pitch;
    private final boolean relative;
    private final boolean hasCoords;

    public TeleportToMechanic(String coords, String worldName,
                              float yaw, float pitch, boolean relative) {
        this.worldName = worldName;
        this.yaw = yaw;
        this.pitch = pitch;
        this.relative = relative;

        double px = 0, py = 0, pz = 0;
        boolean ok = false;
        if (coords != null && !coords.isBlank()) {
            String[] parts = coords.split(",");
            if (parts.length >= 3) {
                try {
                    px = Double.parseDouble(parts[0].trim());
                    py = Double.parseDouble(parts[1].trim());
                    pz = Double.parseDouble(parts[2].trim());
                    ok = true;
                } catch (NumberFormatException ignored) {}
            }
        }
        this.x = px; this.y = py; this.z = pz;
        this.hasCoords = ok;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (!hasCoords || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            World world = worldName != null && !worldName.isBlank()
                ? Bukkit.getWorld(worldName) : target.getWorld();
            if (world == null) continue;
            Location dest;
            if (relative) {
                Location base = context.getOrigin() != null ? context.getOrigin() : target.getLocation();
                dest = base.clone().add(x, y, z);
            } else {
                dest = new Location(world, x, y, z, yaw, pitch);
            }
            dest.setYaw(yaw);
            dest.setPitch(pitch);
            target.teleport(dest);
            any = true;
        }
        return any;
    }
}
