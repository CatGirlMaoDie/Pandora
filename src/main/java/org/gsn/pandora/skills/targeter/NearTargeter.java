package org.gsn.pandora.skills.targeter;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.skills.Targeter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NearTargeter implements Targeter {

    private final double radius;
    private final int limit;

    public NearTargeter(double radius, int limit) {
        this.radius = Math.max(0.0, radius);
        this.limit = Math.max(1, limit);
    }

    @Override
    public List<LivingEntity> resolve(SkillExecutionContext context) {
        List<LivingEntity> result = new ArrayList<>();
        Location origin = context.getOrigin();
        if (origin == null) {
            return result;
        }
        World world = origin.getWorld();
        if (world == null) {
            return result;
        }

        double r = radius;
        for (Entity entity : world.getNearbyEntities(origin, r, r, r)) {
            if (entity instanceof LivingEntity) {
                result.add((LivingEntity) entity);
            }
        }

        if (result.isEmpty()) {
            return result;
        }

        result.sort(Comparator.comparingDouble(e -> e.getLocation().distanceSquared(origin)));
        if (result.size() > limit) {
            return new ArrayList<>(result.subList(0, limit));
        }
        return result;
    }
}
