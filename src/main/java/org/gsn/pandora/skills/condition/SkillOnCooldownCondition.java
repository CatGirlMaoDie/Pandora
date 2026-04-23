package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Locale;

/** 检测指定技能是否在冷却中。skill|s=<skillName> */
public class SkillOnCooldownCondition implements Condition {
    private final String skillName;
    private final org.gsn.pandora.skills.SkillManager skillManager;

    public SkillOnCooldownCondition(String skillName, org.gsn.pandora.skills.SkillManager skillManager) {
        this.skillName = skillName == null ? "" : skillName.toLowerCase(Locale.ROOT);
        this.skillManager = skillManager;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (skillName.isEmpty() || skillManager == null || target == null) return false;
        return skillManager.isOnCooldown(skillName, target);
    }
}
