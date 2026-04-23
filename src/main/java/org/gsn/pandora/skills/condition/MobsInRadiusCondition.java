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

/** 检测目标周围指定半径内特定 MythicMob 类型的数量范围。 */
public class MobsInRadiusCondition implements Condition {

    private static final NamespacedKey MOB_ID_KEY = new NamespacedKey("pandora", "mob-id");

    private final Set<String> types;
    private final double radius;
    private final int minAmount;
    private final int maxAmount;

    public MobsInRadiusCondition(String typeList, double radius, int minAmount, int maxAmount) {
        this.types = typeList == null || typeList.isEmpty()
            ? java.util.Collections.emptySet()
            : Arrays.stream(typeList.split(","))
                .map(s -> s.trim().toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());
        this.radius = Math.max(0.0, radius);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        int count = 0;
        for (Entity e : target.getWorld().getNearbyEntities(target.getLocation(), radius, radius, radius)) {
            if (!(e instanceof LivingEntity) || e.equals(target)) continue;
            if (!types.isEmpty()) {
                String mobId = e.getPersistentDataContainer().get(MOB_ID_KEY, PersistentDataType.STRING);
                if (mobId == null || !types.contains(mobId.toLowerCase(Locale.ROOT))) continue;
            }
            count++;
        }
        return count >= minAmount && count <= maxAmount;
    }
}
