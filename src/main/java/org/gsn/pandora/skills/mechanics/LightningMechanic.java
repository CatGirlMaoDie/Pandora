package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/** 在目标位置召唤闪电。damage|d=0 控制伤害（0 = 仅视觉效果）。 */
public class LightningMechanic implements Mechanic {

    private final double damage;

    public LightningMechanic(double damage) {
        this.damage = damage;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            Location loc = target.getLocation();
            if (loc.getWorld() == null) continue;
            if (damage <= 0) {
                loc.getWorld().strikeLightningEffect(loc);
            } else {
                loc.getWorld().strikeLightning(loc);
                if (damage > 0) {
                    target.damage(damage, context.getCaster());
                }
            }
            any = true;
        }
        return any;
    }
}
