package org.gsn.pandora.skills.targeter;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.skills.Targeter;

import java.util.ArrayList;
import java.util.List;

/**
 * 以施法者朝向为中心的扇形目标选择器。
 */
public class ConeTargeter implements Targeter {

    private final double radius;
    private final double halfAngleRadians;

    public ConeTargeter(double radius, double angleDegrees) {
        this.radius = Math.max(0.0, radius);
        double angle = Math.max(0.0, Math.min(180.0, angleDegrees));
        this.halfAngleRadians = Math.toRadians(angle / 2.0);
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

        LivingEntity caster = context.getCaster();
        Vector dir = caster != null
            ? caster.getLocation().getDirection()
            : origin.getDirection();
        if (dir.lengthSquared() == 0) {
            dir = new Vector(0, 0, 1);
        }
        dir = dir.normalize();

        double r = radius;
        double rSquared = r * r;
        for (Entity entity : world.getNearbyEntities(origin, r, r, r)) {
            if (!(entity instanceof LivingEntity)) {
                continue;
            }
            LivingEntity le = (LivingEntity) entity;
            if (le.isDead()) {
                continue;
            }
            Vector toTarget = le.getLocation().toVector().subtract(origin.toVector());
            double lenSq = toTarget.lengthSquared();
            if (lenSq == 0.0 || lenSq > rSquared) {
                continue;
            }
            toTarget.normalize();
            double angle = dir.angle(toTarget);
            if (angle <= halfAngleRadians) {
                result.add(le);
            }
        }
        return result;
    }
}
