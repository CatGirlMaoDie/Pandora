package org.gsn.pandora.skills.condition;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测目标玩家背包中是否拥有指定物品。types|type|t|material|mat|m|item|i=...  amount|a=1 */
public class HasItemCondition implements Condition {
    private final Set<Material> materials;
    private final int minAmount;

    public HasItemCondition(String typeList, int minAmount) {
        this.materials = Arrays.stream(typeList.split(","))
            .map(s -> {
                String name = s.trim().toUpperCase(Locale.ROOT);
                try { return Material.valueOf(name); }
                catch (IllegalArgumentException e) { return MaterialUtil.matchMaterialByNameOrChinese(s.trim()); }
            })
            .filter(m -> m != null)
            .collect(Collectors.toSet());
        this.minAmount = Math.max(1, minAmount);
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (!(target instanceof Player p) || materials.isEmpty()) return false;
        int count = 0;
        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null && materials.contains(item.getType())) count += item.getAmount();
        }
        return count >= minAmount;
    }
}
