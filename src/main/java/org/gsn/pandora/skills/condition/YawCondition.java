package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标水平朝向（yaw）范围，-180 到 180。 */
public class YawCondition implements Condition {

    private final float minYaw;
    private final float maxYaw;

    public YawCondition(float minYaw, float maxYaw) {
        this.minYaw = minYaw;
        this.maxYaw = maxYaw;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        float yaw = target.getLocation().getYaw();
        return yaw >= minYaw && yaw <= maxYaw;
    }
}
