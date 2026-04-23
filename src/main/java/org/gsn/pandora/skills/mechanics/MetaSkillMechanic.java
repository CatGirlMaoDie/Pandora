package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 执行另一个已定义的技能（meta-skill）。
 * skill=<skillName>
 * 实际调用由 SkillManager 处理，此类仅作为数据载体。
 */
public class MetaSkillMechanic implements Mechanic {

    private final String skillName;
    private final org.gsn.pandora.skills.SkillManager skillManager;

    public MetaSkillMechanic(String skillName, org.gsn.pandora.skills.SkillManager skillManager) {
        this.skillName = skillName == null ? "" : skillName;
        this.skillManager = skillManager;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (skillName.isEmpty() || skillManager == null) return false;
        skillManager.castSkill(skillName, context);
        return true;
    }
}
