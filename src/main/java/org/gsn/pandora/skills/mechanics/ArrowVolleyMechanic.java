package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 向目标方向发射一批箭矢（arrowvolley）。
 * amount|arrows|a=20  spread|s=45  fireTicks|ft|f=0
 * removeDelay|rd|r=200  velocity|v=20  pickup|canPickup=true
 */
public class ArrowVolleyMechanic implements Mechanic {

    private final int amount;
    private final double spread;
    private final int fireTicks;
    private final int removeDelay;
    private final double velocity;
    private final boolean pickup;

    public ArrowVolleyMechanic(int amount, double spread, int fireTicks,
                               int removeDelay, double velocity, boolean pickup) {
        this.amount = Math.max(1, amount);
        this.spread = Math.max(0, spread);
        this.fireTicks = Math.max(0, fireTicks);
        this.removeDelay = Math.max(1, removeDelay);
        this.velocity = Math.max(0.1, velocity / 10.0);
        this.pickup = pickup;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (caster == null) return false;
        Location origin = caster.getEyeLocation();
        if (origin.getWorld() == null) return false;

        ThreadLocalRandom rng = ThreadLocalRandom.current();
        double spreadRad = Math.toRadians(spread);

        for (int i = 0; i < amount; i++) {
            Vector dir = origin.getDirection().clone();
            if (spread > 0) {
                double angle = rng.nextDouble() * spreadRad;
                double azimuth = rng.nextDouble() * 2 * Math.PI;
                dir.rotateAroundX(Math.sin(azimuth) * angle);
                dir.rotateAroundY(Math.cos(azimuth) * angle);
            }
            dir.normalize().multiply(velocity);
            Arrow arrow = origin.getWorld().spawnArrow(origin, dir, (float) velocity, 0f);
            arrow.setShooter(caster);
            if (fireTicks > 0) arrow.setFireTicks(fireTicks);
            arrow.setPickupStatus(pickup
                ? Arrow.PickupStatus.ALLOWED
                : Arrow.PickupStatus.DISALLOWED);
            int delay = removeDelay;
            org.gsn.pandora.util.FoliaScheduler.runEntityTaskLater(
                context.getPlugin(), arrow, arrow::remove, delay);
        }
        return true;
    }
}
