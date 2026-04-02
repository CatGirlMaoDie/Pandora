package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.concurrent.ThreadLocalRandom;

public class ChanceCondition implements Condition {

    private final double chance; // 0-1

    public ChanceCondition(double chance) {
        this.chance = chance;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        return ThreadLocalRandom.current().nextDouble() <= chance;
    }
}
