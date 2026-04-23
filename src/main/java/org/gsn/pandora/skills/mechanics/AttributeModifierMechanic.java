package org.gsn.pandora.skills.mechanics;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 给目标添加属性修改器（支持不同运算方式）。
 * attribute|attr=GENERIC_MAX_HEALTH  operation|op=ADD_NUMBER
 * name|modifierName=<uuid>  amount|amt|a=0  duration|dur=0（0=永久）
 */
public class AttributeModifierMechanic implements Mechanic {

    private final Attribute attribute;
    private final AttributeModifier.Operation operation;
    private final String modifierName;
    private final double amount;
    private final int durationTicks;

    public AttributeModifierMechanic(String attrName, String opName,
                                     String modifierName, double amount, int durationTicks) {
        Attribute a = null;
        if (attrName != null) {
            NamespacedKey key = attrName.contains(":")
                ? NamespacedKey.fromString(attrName.toLowerCase(Locale.ROOT))
                : NamespacedKey.minecraft(attrName.toLowerCase(Locale.ROOT));
            if (key != null) a = Registry.ATTRIBUTE.get(key);
        }
        this.attribute = a;

        AttributeModifier.Operation op = AttributeModifier.Operation.ADD_NUMBER;
        if (opName != null) {
            switch (opName.toUpperCase(Locale.ROOT)) {
                case "ADD_SCALAR", "MULTIPLY_SCALAR_1", "MULTIPLY_SCALAR" -> op = AttributeModifier.Operation.ADD_SCALAR;
            }
        }
        this.operation = op;
        this.modifierName = modifierName == null ? "pandora_modifier" : modifierName;
        this.amount = amount;
        this.durationTicks = Math.max(0, durationTicks);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (attribute == null || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            AttributeInstance inst = target.getAttribute(attribute);
            if (inst == null) continue;

            AttributeModifier mod = new AttributeModifier(
                NamespacedKey.fromString("pandora:" + modifierName.toLowerCase(Locale.ROOT).replace(" ", "_")),
                amount, operation);
            inst.addModifier(mod);

            if (durationTicks > 0) {
                org.gsn.pandora.util.FoliaScheduler.runEntityTaskLater(
                    context.getPlugin(), target, () -> inst.removeModifier(mod), durationTicks);
            }
            any = true;
        }
        return any;
    }
}
