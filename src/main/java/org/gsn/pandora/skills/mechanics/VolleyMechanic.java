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
 * 向目标方向发射一组箭矢（扇形散射）。
 * amount|a=5  radius|r=3  yOffset|y=0  pickup|canPickup=false
 */
public class VolleyMechanic implements Mechanic {

    private final int amount;
    private final double radius;
    private final double yOffset;
    private final boolean pickup;

    public VolleyMechanic(int amount, double radius, double yOffset, boolean pickup) {
        this.amount = Math.max(1, amount);
        this.radius = Math.max(0, radius);
        this.yOffset = yOffset;
        this.pickup = pickup;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (caster == null || targets == null || targets.isEmpty()) return false;
        LivingEntity dest = targets.get(0);
        if (dest == null) return false;

        Location origin = caster.getEyeLocation();
        if (origin.getWorld() == null) return false;

        ThreadLocalRandom rng = ThreadLocalRandom.current();
        for (int i = 0; i < amount; i++) {
            Location target = dest.getLocation().clone().add(0, yOffset, 0);
            if (radius > 0) {
                target.add(
                    (rng.nextDouble() * 2 - 1) * radius,
                    (rng.nextDouble() * 2 - 1) * radius * 0.5,
                    (rng.nextDouble() * 2 - 1) * radius);
            }
            Vector dir = target.toVector().subtract(origin.toVector()).normalize();
            Arrow arrow = origin.getWorld().spawnArrow(origin, dir, 2.0f, 0f);
            arrow.setShooter(caster);
            arrow.setPickupStatus(pickup ? Arrow.PickupStatus.ALLOWED : Arrow.PickupStatus.DISALLOWED);
        }
        return true;
    }
}
