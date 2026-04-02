package org.gsn.pandora.skills.targeter;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.skills.Targeter;

import java.util.Collections;
import java.util.List;

public class SelfTargeter implements Targeter {

    @Override
    public List<LivingEntity> resolve(SkillExecutionContext context) {
        LivingEntity caster = context.getCaster();
        if (caster == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(caster);
    }
}
