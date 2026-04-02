package org.gsn.pandora.skills;

import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * 技能实际执行逻辑。
 */
public interface Mechanic {

    /**
     * @return 是否实际对至少一个目标生效
     */
    boolean execute(SkillExecutionContext context, List<LivingEntity> targets);
}
