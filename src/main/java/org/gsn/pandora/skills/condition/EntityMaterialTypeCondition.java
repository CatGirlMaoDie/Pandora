package org.gsn.pandora.skills.condition;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测目标实体的材质类型（适用于 FallingBlock、Item 等）。material|mat|m|type|types|t=... */
public class EntityMaterialTypeCondition implements Condition {

    private final Set<Material> materials;

    public EntityMaterialTypeCondition(String typeList) {
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
        if (target == null || materials.isEmpty()) return false;
        // FallingBlock
        if (target instanceof FallingBlock fb) return materials.contains(fb.getBlockData().getMaterial());
        // 检查主手物品
        var eq = target.getEquipment();
        if (eq != null) {
            var item = eq.getItemInMainHand();
            if (!item.getType().isAir() && materials.contains(item.getType())) return true;
        }
        return false;
    }
}
