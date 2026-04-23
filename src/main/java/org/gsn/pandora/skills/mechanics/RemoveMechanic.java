package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 移除目标实体。
 * onRemove|then=<skillName>（可选，移除前触发技能，暂不实现）
 */
public class RemoveMechanic implements Mechanic {

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            target.remove();
            any = true;
        }
        return any;
    }
}
