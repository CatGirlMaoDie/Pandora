package org.gsn.pandora.skills.condition;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测施法者原点附近指定半径内特定 Mob 类型的数量范围。types|t=...  amount|a=min  radius|r=16 */
public class MobsNearOriginCondition implements Condition {
    private static final NamespacedKey MOB_ID_KEY = new NamespacedKey("pandora", "mob-id");

    private final Set<String> types;
    private final int minAmount;
    private final double radius;

    public MobsNearOriginCondition(String typeList, int minAmount, double radius) {
        this.types = typeList == null || typeList.isBlank()
            ? java.util.Collections.emptySet()
            : Arrays.stream(typeList.split(","))
                .map(s -> s.trim().toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());
        this.minAmount = Math.max(1, minAmount);
        this.radius = Math.max(0, radius);
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        var origin = context.getOrigin();
        if (origin == null || origin.getWorld() == null) return false;
        int count = 0;
        for (Entity e : origin.getWorld().getNearbyEntities(origin, radius, radius, radius)) {
            if (!(e instanceof LivingEntity)) continue;
            if (!types.isEmpty()) {
                String mobId = e.getPersistentDataContainer().get(MOB_ID_KEY, PersistentDataType.STRING);
                if (mobId == null || !types.contains(mobId.toLowerCase(Locale.ROOT))) continue;
            }
            count++;
        }
        return count >= minAmount;
    }
}
