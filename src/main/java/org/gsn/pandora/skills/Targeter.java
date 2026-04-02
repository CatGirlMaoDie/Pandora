package org.gsn.pandora.skills;

import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * 决定技能作用到哪些实体上。
 */
public interface Targeter {

    List<LivingEntity> resolve(SkillExecutionContext context);
}
