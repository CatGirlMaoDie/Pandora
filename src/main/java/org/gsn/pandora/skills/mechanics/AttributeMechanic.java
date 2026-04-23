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
 * 临时修改实体属性。
 * attribute|attr=GENERIC_MAX_HEALTH  amount|amt|a=0
 * duration|dur=0（0=永久）  operation|op=ADD_NUMBER
 */
public class AttributeMechanic implements Mechanic {

    private final Attribute attribute;
    private final double amount;
    private final int durationTicks;
    private final AttributeModifier.Operation operation;

    public AttributeMechanic(String attrName, double amount, int durationTicks, String opName) {
        Attribute a = null;
        if (attrName != null) {
            NamespacedKey key = attrName.contains(":")
                ? NamespacedKey.fromString(attrName.toLowerCase(Locale.ROOT))
                : NamespacedKey.minecraft(attrName.toLowerCase(Locale.ROOT));
            if (key != null) a = Registry.ATTRIBUTE.get(key);
        }
        this.attribute = a;
        this.amount = amount;
        this.durationTicks = Math.max(0, durationTicks);
        AttributeModifier.Operation op = AttributeModifier.Operation.ADD_NUMBER;
        if (opName != null) {
            switch (opName.toUpperCase(Locale.ROOT)) {
                case "ADD_SCALAR": case "MULTIPLY_SCALAR_1": case "MULTIPLY_SCALAR":
                    op = AttributeModifier.Operation.ADD_SCALAR; break;
            }
        }
        this.operation = op;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (attribute == null || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            AttributeInstance inst = target.getAttribute(attribute);
            if (inst == null) continue;

            if (durationTicks == 0) {
                // 永久修改基础值
                inst.setBaseValue(inst.getBaseValue() + amount);
            } else {
                // 临时修改器
                AttributeModifier mod = new AttributeModifier(
                    NamespacedKey.fromString("pandora:attr_" + attribute.getKey().getKey()),
                    amount, operation);
                inst.addModifier(mod);
                // 到期后移除
                org.gsn.pandora.util.FoliaScheduler.runEntityTaskLater(
                    context.getPlugin(), target, () -> inst.removeModifier(mod), durationTicks);
            }
            any = true;
        }
        return any;
    }
}
