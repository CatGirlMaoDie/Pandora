package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标 X/Y/Z 轴速度范围。velocity|v=min;maxVelocity=max  axis: X/Y/Z */
public class MotionCondition implements Condition {
    public enum Axis { X, Y, Z }
    private final Axis axis;
    private final double min, max;

    public MotionCondition(Axis axis, double min, double max) {
        this.axis = axis;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        var v = target.getVelocity();
        double val = switch (axis) {
            case X -> v.getX();
            case Y -> v.getY();
            case Z -> v.getZ();
        };
        return val >= min && val <= max;
    }
}
