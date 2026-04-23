package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 给抛射物设置自定义模型数据标签（存入 PDC）。
 * value|v|model|m=0
 */
public class SetProjectileBulletModelMechanic implements Mechanic {

    private static final NamespacedKey MODEL_KEY = new NamespacedKey("pandora", "bullet_model");
    private final int modelValue;

    public SetProjectileBulletModelMechanic(int modelValue) {
        this.modelValue = modelValue;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Projectile proj)) continue;
            proj.getPersistentDataContainer().set(MODEL_KEY, PersistentDataType.INTEGER, modelValue);
            any = true;
        }
        return any;
    }
}
