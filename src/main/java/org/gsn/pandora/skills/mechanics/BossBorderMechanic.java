package org.gsn.pandora.skills.mechanics;

import org.bukkit.WorldBorder;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 以施法者为中心设置世界边界。
 * radius|r=64
 */
public class BossBorderMechanic implements Mechanic {

    private final double radius;

    public BossBorderMechanic(double radius) {
        this.radius = Math.max(1, radius);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (caster == null || caster.getWorld() == null) return false;
        WorldBorder border = caster.getWorld().getWorldBorder();
        border.setCenter(caster.getLocation());
        border.setSize(radius * 2);
        return true;
    }
}
