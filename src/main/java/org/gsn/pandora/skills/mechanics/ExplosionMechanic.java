package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 在目标位置制造爆炸。
 * yield|y=0 爆炸威力；blockdamage|bd=false 是否破坏方块；fire|f=false 是否留火。
 */
public class ExplosionMechanic implements Mechanic {

    private final float yield;
    private final boolean blockDamage;
    private final boolean fire;

    public ExplosionMechanic(float yield, boolean blockDamage, boolean fire) {
        this.yield = Math.max(0f, yield);
        this.blockDamage = blockDamage;
        this.fire = fire;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null) continue;
            Location loc = target.getLocation();
            if (loc.getWorld() == null) continue;
            loc.getWorld().createExplosion(loc, yield, fire, blockDamage);
            any = true;
        }
        return any;
    }
}
