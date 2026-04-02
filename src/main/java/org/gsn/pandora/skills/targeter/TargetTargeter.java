package org.gsn.pandora.skills.targeter;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.skills.Targeter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TargetTargeter implements Targeter {

    @Override
    public List<LivingEntity> resolve(SkillExecutionContext context) {
        LivingEntity target = context.getTarget();
        if (target == null || target.isDead()) {
            return Collections.emptyList();
        }
        List<LivingEntity> result = new ArrayList<>(1);
        result.add(target);
        return result;
    }
}
