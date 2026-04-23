package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标视角俯仰角（pitch）范围，-90（朝上）到 90（朝下）。 */
public class PitchCondition implements Condition {

    private final float minPitch;
    private final float maxPitch;

    public PitchCondition(float minPitch, float maxPitch) {
        this.minPitch = minPitch;
        this.maxPitch = maxPitch;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        float pitch = target.getLocation().getPitch();
        return pitch >= minPitch && pitch <= maxPitch;
    }
}
