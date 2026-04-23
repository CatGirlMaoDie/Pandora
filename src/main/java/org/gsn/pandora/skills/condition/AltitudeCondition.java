package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标 Y 坐标高度范围。height 为最小高度，maxHeight 为最大高度。 */
public class AltitudeCondition implements Condition {

    private final double minHeight;
    private final double maxHeight;

    public AltitudeCondition(double minHeight, double maxHeight) {
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        double y = target.getLocation().getY();
        return y >= minHeight && y <= maxHeight;
    }
}
