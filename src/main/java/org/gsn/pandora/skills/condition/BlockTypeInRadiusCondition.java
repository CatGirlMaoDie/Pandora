package org.gsn.pandora.skills.condition;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测目标周围半径内是否有指定数量的特定方块。types|t=...  amount|a=1  radius|r=5 */
public class BlockTypeInRadiusCondition implements Condition {
    private final Set<Material> materials;
    private final int minAmount;
    private final int radius;

    public BlockTypeInRadiusCondition(String typeList, int minAmount, int radius) {
        this.materials = Arrays.stream(typeList.split(","))
            .map(s -> {
                String name = s.trim().toUpperCase(Locale.ROOT);
                try { return Material.valueOf(name); }
                catch (IllegalArgumentException e) { return MaterialUtil.matchMaterialByNameOrChinese(s.trim()); }
            })
            .filter(m -> m != null)
            .collect(Collectors.toSet());
        this.minAmount = Math.max(1, minAmount);
        this.radius = Math.max(1, radius);
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || materials.isEmpty()) return false;
        Location center = target.getLocation();
        if (center.getWorld() == null) return false;
        int count = 0;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (materials.contains(center.clone().add(x, y, z).getBlock().getType())) {
                        count++;
                        if (count >= minAmount) return true;
                    }
                }
            }
        }
        return false;
    }
}
