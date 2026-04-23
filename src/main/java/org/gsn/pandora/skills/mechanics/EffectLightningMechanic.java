package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 在目标位置召唤闪电视觉效果（不造成伤害）。
 * localized|l=false  localizedRadius|lr|r=128
 */
public class EffectLightningMechanic implements Mechanic {

    private final boolean localized;
    private final double localizedRadius;

    public EffectLightningMechanic(boolean localized, double localizedRadius) {
        this.localized = localized;
        this.localizedRadius = Math.max(1, localizedRadius);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.getWorld() == null) continue;
            var loc = target.getLocation();
            if (localized) {
                // 只对范围内玩家显示
                for (var player : loc.getWorld().getPlayers()) {
                    if (player.getLocation().distanceSquared(loc) <= localizedRadius * localizedRadius) {
                        player.sendBlockChange(loc, org.bukkit.Material.AIR.createBlockData());
                    }
                }
                loc.getWorld().strikeLightningEffect(loc);
            } else {
                loc.getWorld().strikeLightningEffect(loc);
            }
            any = true;
        }
        return any;
    }
}
