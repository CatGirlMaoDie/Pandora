package org.gsn.pandora.skills.mechanics;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.NamespacedKey;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 给目标施加短暂的击退抗性（模拟硬直/recoil）。
 * duration|d=20（tick）
 */
public class RecoilMechanic implements Mechanic {

    private final int durationTicks;

    public RecoilMechanic(int durationTicks) {
        this.durationTicks = Math.max(1, durationTicks);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            AttributeInstance kb = target.getAttribute(Attribute.KNOCKBACK_RESISTANCE);
            if (kb == null) continue;
            AttributeModifier mod = new AttributeModifier(
                NamespacedKey.fromString("pandora:recoil"),
                1.0, AttributeModifier.Operation.ADD_NUMBER);
            kb.addModifier(mod);
            org.gsn.pandora.util.FoliaScheduler.runEntityTaskLater(
                context.getPlugin(), target, () -> kb.removeModifier(mod), durationTicks);
            any = true;
        }
        return any;
    }
}
