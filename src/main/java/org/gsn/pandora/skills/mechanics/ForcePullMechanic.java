package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 将目标拉向施法者（forcepull）。
 * spread|s=0 水平偏差；spreadV|vspread|vs=0 垂直偏差。
 */
public class ForcePullMechanic implements Mechanic {

    private final double spread;
    private final double spreadV;

    public ForcePullMechanic(double spread, double spreadV) {
        this.spread = spread;
        this.spreadV = spreadV;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (caster == null || targets == null || targets.isEmpty()) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            Vector dir = caster.getLocation().toVector()
                .subtract(target.getLocation().toVector());
            double len = dir.length();
            if (len == 0) continue;
            dir.normalize();
            if (spread > 0) {
                dir.setX(dir.getX() + (Math.random() * 2 - 1) * spread);
                dir.setZ(dir.getZ() + (Math.random() * 2 - 1) * spread);
            }
            if (spreadV > 0) {
                dir.setY(dir.getY() + (Math.random() * 2 - 1) * spreadV);
            }
            target.setVelocity(dir.multiply(Math.min(len, 3.0)));
            any = true;
        }
        return any;
    }
}
