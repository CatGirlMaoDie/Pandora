package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标与施法者的 X/Y/Z 坐标差值范围。difference|diff|d=min;maxDiff=max  axis: X/Y/Z */
public class CoordDiffCondition implements Condition {
    public enum Axis { X, Y, Z }
    private final Axis axis;
    private final double min, max;

    public CoordDiffCondition(Axis axis, double min, double max) {
        this.axis = axis;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        LivingEntity caster = context.getCaster();
        if (caster == null || target == null) return false;
        double diff = switch (axis) {
            case X -> Math.abs(target.getLocation().getX() - caster.getLocation().getX());
            case Y -> Math.abs(target.getLocation().getY() - caster.getLocation().getY());
            case Z -> Math.abs(target.getLocation().getZ() - caster.getLocation().getZ());
        };
        return diff >= min && diff <= max;
    }
}
