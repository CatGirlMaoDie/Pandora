package org.gsn.pandora.skills.condition;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Material;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测目标是否为 Item 实体且物品类型匹配。material|mat|m|type|types|t=... */
public class EntityItemTypeCondition implements Condition {
    private final Set<Material> materials;

    public EntityItemTypeCondition(String typeList) {
        this.materials = Arrays.stream(typeList.split(","))
            .map(s -> {
                String name = s.trim().toUpperCase(Locale.ROOT);
                try { return Material.valueOf(name); }
                catch (IllegalArgumentException e) { return MaterialUtil.matchMaterialByNameOrChinese(s.trim()); }
            })
            .filter(m -> m != null)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        // target 通常是 LivingEntity，此条件主要用于 Item 实体（通过 @area 等选中）
        if (target == null || materials.isEmpty()) return false;
        // 检查施法者附近的 Item 实体
        if (context.getOrigin() == null || context.getOrigin().getWorld() == null) return false;
        for (Entity e : context.getOrigin().getWorld().getNearbyEntities(context.getOrigin(), 1, 1, 1)) {
            if (e instanceof Item item && materials.contains(item.getItemStack().getType())) return true;
        }
        return false;
    }
}
