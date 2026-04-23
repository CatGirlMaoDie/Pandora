package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标是否正在移动（速度向量不为零）。 */
public class MovingCondition implements Condition {

    private static final double EPSILON = 1e-6;

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        Vector v = target.getVelocity();
        return v.lengthSquared() > EPSILON;
    }
}
