package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标是否拥有指定 PersistentData 标签（key 存在即视为有）。 */
public class HasTagCondition implements Condition {

    private final NamespacedKey key;

    public HasTagCondition(String tag) {
        NamespacedKey k = null;
        if (tag != null && !tag.isEmpty()) {
            try {
                k = NamespacedKey.fromString(tag.contains(":") ? tag : "pandora:" + tag);
            } catch (Exception ignored) {}
        }
        this.key = k;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (key == null || target == null) return false;
        return target.getPersistentDataContainer().has(key, PersistentDataType.STRING)
            || target.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)
            || target.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }
}
