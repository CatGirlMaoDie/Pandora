package org.gsn.pandora.skills.condition;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测目标所在位置的方块类型（脚下方块）。types|block|b|type|t|material|mat|m=... */
public class BlockTypeCondition implements Condition {
    private final Set<Material> materials;

    public BlockTypeCondition(String typeList) {
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
        Location loc = target.getLocation();
        if (loc.getWorld() == null) return false;
        Block block = loc.getBlock();
        return materials.contains(block.getType());
    }
}
