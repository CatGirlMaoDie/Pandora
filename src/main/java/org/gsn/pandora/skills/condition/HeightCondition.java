package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标 Y 坐标是否在 min~max 范围内。height|h=min;maxHeight=max */
public class HeightCondition implements Condition {
    private final double min, max;
    public HeightCondition(double min, double max) { this.min = min; this.max = max; }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        double y = target.getLocation().getY();
        return y >= min && y <= max;
    }
}
