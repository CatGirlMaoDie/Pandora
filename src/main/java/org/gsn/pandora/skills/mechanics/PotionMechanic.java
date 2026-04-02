package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

public class PotionMechanic implements Mechanic {

    private final PotionEffectType type;
    private final int durationTicks;
    private final int amplifier;

    public PotionMechanic(String typeName, int durationTicks, int amplifier) {
        this.type = typeName == null ? null : PotionEffectType.getByName(typeName.toUpperCase(Locale.ROOT));
        this.durationTicks = Math.max(0, durationTicks);
        this.amplifier = Math.max(0, amplifier);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (type == null || targets == null || targets.isEmpty()) {
            return false;
        }
        boolean any = false;
        PotionEffect effect = new PotionEffect(type, durationTicks, amplifier);
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) {
                continue;
            }
            target.addPotionEffect(effect);
            any = true;
        }
        return any;
    }
}
