package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 给目标实体打上伤害标签（存入 PDC），供 damageTag 条件检测。
 * tag|t=<tagName>
 */
public class DamageTagMechanic implements Mechanic {

    private static final NamespacedKey TAG_KEY = new NamespacedKey("pandora", "damage_tag");

    private final String tag;

    public DamageTagMechanic(String tag) {
        this.tag = tag == null ? "" : tag;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (tag.isEmpty() || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null) continue;
            target.getPersistentDataContainer().set(TAG_KEY, PersistentDataType.STRING, tag);
            // 1 tick 后清除标签，避免持久污染
            org.gsn.pandora.util.FoliaScheduler.runEntityTaskLater(
                context.getPlugin(), target,
                () -> target.getPersistentDataContainer().remove(TAG_KEY), 1L);
            any = true;
        }
        return any;
    }
}
