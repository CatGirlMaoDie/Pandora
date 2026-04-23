package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Locale;

/**
 * 检测目标是否带有指定伤害标签（由 damagetag mechanic 设置）。
 * tag|t=<tagName>
 */
public class DamageTagCondition implements Condition {

    private static final NamespacedKey TAG_KEY = new NamespacedKey("pandora", "damage_tag");

    private final String tag;

    public DamageTagCondition(String tag) {
        this.tag = tag == null ? "" : tag.toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || tag.isEmpty()) return false;
        String stored = target.getPersistentDataContainer().get(TAG_KEY, PersistentDataType.STRING);
        return stored != null && stored.toLowerCase(Locale.ROOT).equals(tag);
    }
}
