package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 让施法者向目标位置移动（通过速度向量）。
 * speed|s=1  spreadH|sh=0  spreadV|sv=0
 */
public class GotoMechanic implements Mechanic {

    private final double speed;
    private final double spreadH;
    private final double spreadV;

    public GotoMechanic(double speed, double spreadH, double spreadV) {
        this.speed = Math.max(0.1, speed);
        this.spreadH = Math.max(0, spreadH);
        this.spreadV = Math.max(0, spreadV);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (caster == null || targets == null || targets.isEmpty()) return false;
        LivingEntity dest = targets.get(0);
        if (dest == null) return false;

        Vector dir = dest.getLocation().toVector()
            .subtract(caster.getLocation().toVector());
        double len = dir.length();
        if (len == 0) return false;
        dir.normalize().multiply(speed);

        if (spreadH > 0) {
            ThreadLocalRandom rng = ThreadLocalRandom.current();
            dir.setX(dir.getX() + (rng.nextDouble() * 2 - 1) * spreadH);
            dir.setZ(dir.getZ() + (rng.nextDouble() * 2 - 1) * spreadH);
        }
        if (spreadV > 0) {
            dir.setY(dir.getY() + (ThreadLocalRandom.current().nextDouble() * 2 - 1) * spreadV);
        }
        caster.setVelocity(dir);
        return true;
    }
}
