package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Locale;

/** 检测目标的 Faction（阵营），通过 PersistentData 或 MobType 匹配。 */
public class FactionCondition implements Condition {

    private static final NamespacedKey FACTION_KEY =
        new NamespacedKey("pandora", "faction");

    private final String faction;

    public FactionCondition(String faction) {
        this.faction = faction == null ? "" : faction.toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || faction.isEmpty()) return false;
        String stored = target.getPersistentDataContainer().get(FACTION_KEY, PersistentDataType.STRING);
        if (stored == null) return false;
        return stored.toLowerCase(Locale.ROOT).equals(faction);
    }
}
