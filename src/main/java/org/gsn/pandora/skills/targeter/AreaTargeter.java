package org.gsn.pandora.skills.targeter;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.skills.Targeter;

import java.util.ArrayList;
import java.util.List;

public class AreaTargeter implements Targeter {

    private final double radius;

    public AreaTargeter(double radius) {
        this.radius = Math.max(0.0, radius);
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
        return result;
    }
}
