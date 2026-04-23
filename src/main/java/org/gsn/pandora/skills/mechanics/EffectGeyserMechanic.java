package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 在目标位置播放间歇泉效果（水/岩浆粒子柱）。
 * height|h=3  type|t=water
 */
public class EffectGeyserMechanic implements Mechanic {

    private final int height;
    private final Particle particle;

    public EffectGeyserMechanic(int height, String type) {
        this.height = Math.max(1, height);
        String t = type == null ? "water" : type.toLowerCase(Locale.ROOT);
        this.particle = t.contains("lava") ? Particle.LAVA : Particle.SPLASH;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.getWorld() == null) continue;
            Location base = target.getLocation();
            for (int y = 0; y <= height; y++) {
                base.getWorld().spawnParticle(particle, base.clone().add(0, y, 0),
                    5, 0.3, 0, 0.3, 0.1);
            }
            any = true;
        }
        return any;
    }
}
