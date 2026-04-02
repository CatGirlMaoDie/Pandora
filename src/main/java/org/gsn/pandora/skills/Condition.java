package org.gsn.pandora.skills;

import org.bukkit.entity.LivingEntity;

/**
 * 用于控制技能是否对某个目标生效。
 */
public interface Condition {

    boolean matches(SkillExecutionContext context, LivingEntity target);
}
