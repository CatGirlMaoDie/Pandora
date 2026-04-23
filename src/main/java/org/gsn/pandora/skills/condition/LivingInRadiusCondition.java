package org.gsn.pandora.skills.condition;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标周围指定半径内的生物数量范围。 */
public class LivingInRadiusCondition implements Condition {

    private final double radius;
    private final int minAmount;
    private final int maxAmount;

    public LivingInRadiusCondition(double radius, int minAmount, int maxAmount) {
        this.radius = Math.max(0.0, radius);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        int count = 0;
        for (Entity e : target.getWorld().getNearbyEntities(target.getLocation(), radius, radius, radius)) {
            if (e instanceof LivingEntity && !e.equals(target)) count++;
        }
        return count >= minAmount && count <= maxAmount;
    }
}
