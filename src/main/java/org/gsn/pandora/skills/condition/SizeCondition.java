package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标实体大小（适用于 Slime/MagmaCube）。size=min;maxSize=max */
public class SizeCondition implements Condition {
    private final int min, max;
    public SizeCondition(int min, int max) { this.min = min; this.max = max; }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (!(target instanceof Slime slime)) return false;
        int size = slime.getSize();
        return size >= min && size <= max;
    }
}
