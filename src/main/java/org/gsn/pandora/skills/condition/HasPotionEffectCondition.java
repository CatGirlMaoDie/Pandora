package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Locale;

/** 检测目标是否拥有指定药水效果，可选检测等级和剩余时长范围。 */
public class HasPotionEffectCondition implements Condition {

    private final PotionEffectType type;
    private final int minLevel;
    private final int maxLevel;
    private final int minDuration;
    private final int maxDuration;

    public HasPotionEffectCondition(String typeName, int minLevel, int maxLevel,
                                    int minDuration, int maxDuration) {
        this.type = typeName == null ? null : PotionEffectType.getByName(typeName.toUpperCase(Locale.ROOT));
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (type == null || target == null) return false;
        var effect = target.getPotionEffect(type);
        if (effect == null) return false;
        int amp = effect.getAmplifier() + 1; // amplifier 0 = level 1
        if (amp < minLevel || amp > maxLevel) return false;
        int dur = effect.getDuration();
        return dur >= minDuration && dur <= maxDuration;
    }
}
