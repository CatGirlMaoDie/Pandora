package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

public class ParticlesMechanic implements Mechanic {

    private final Particle particle;
    private final int amount;
    private final double radius;

    public ParticlesMechanic(String particleName, int amount, double radius) {
        Particle p;
        try {
            p = Particle.valueOf(particleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            p = Particle.FLAME;
        }
        this.particle = p;
        this.amount = amount;
        this.radius = radius;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        Location origin = context.getOrigin();
        if (origin == null || origin.getWorld() == null) {
            return false;
        }
        origin.getWorld().spawnParticle(
            particle,
            origin,
            amount,
            radius,
            radius,
            radius,
            0
        );
        return true;
    }
}
