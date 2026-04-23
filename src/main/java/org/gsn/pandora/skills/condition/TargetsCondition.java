package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/**
 * 检测施法者周围的目标数量范围（通过 @area 等 targeter 解析后的目标列表）。
 * amount|a=min;maxAmount=max
 * 注：此条件在 SkillManager.executeLine 中特殊处理，检测 targets 列表大小。
 * 此处实现为检测施法者附近的生物数量作为近似。
 */
public class TargetsCondition implements Condition {

    private final int min;
    private final int max;
    private final double radius;

    public TargetsCondition(int min, int max, double radius) {
        this.min = min;
        this.max = max;
        this.radius = Math.max(1, radius);
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        var origin = context.getOrigin();
        if (origin == null || origin.getWorld() == null) return false;
        long count = origin.getWorld().getNearbyEntities(origin, radius, radius, radius)
            .stream()
            .filter(e -> e instanceof LivingEntity && !e.equals(context.getCaster()))
            .count();
        return count >= min && count <= max;
    }
}
