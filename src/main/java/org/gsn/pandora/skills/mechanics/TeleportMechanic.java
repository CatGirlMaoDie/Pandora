package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TeleportMechanic implements Mechanic {

    private final double radius;

    public TeleportMechanic(double radius) {
        this.radius = Math.max(0.0, radius);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) {
            return false;
        }
        Location origin = context.getOrigin();
        if (origin == null) {
            return false;
        }
        World world = origin.getWorld();
        if (world == null) {
            return false;
        }

        boolean any = false;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) {
                continue;
            }
            Location base = origin.clone();
            if (radius > 0.0) {
                double dx = (random.nextDouble() * 2.0 - 1.0) * radius;
                double dz = (random.nextDouble() * 2.0 - 1.0) * radius;
                base.add(dx, 0.0, dz);
            }
            int y = world.getHighestBlockYAt(base);
            Location safe = new Location(world, base.getX(), y + 1.0, base.getZ());
            safe.add(0.5, 0.0, 0.5);
            target.teleport(safe);
            any = true;
        }
        return any;
    }
}
