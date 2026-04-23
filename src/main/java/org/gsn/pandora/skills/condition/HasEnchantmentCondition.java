package org.gsn.pandora.skills.condition;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Locale;

/** 检测目标玩家主手物品是否有指定附魔及等级。type|t|enchantment|ench|e=...  level|lvl|l=1 */
public class HasEnchantmentCondition implements Condition {
    private final Enchantment enchantment;
    private final int minLevel;

    public HasEnchantmentCondition(String enchName, int minLevel) {
        Enchantment e = null;
        if (enchName != null && !enchName.isBlank()) {
            e = Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft(enchName.toLowerCase(Locale.ROOT)));
            if (e == null) {
                // 尝试遍历匹配
                for (Enchantment ench : Enchantment.values()) {
                    if (ench.getKey().getKey().equalsIgnoreCase(enchName)) { e = ench; break; }
                }
            }
        }
        this.enchantment = e;
        this.minLevel = Math.max(1, minLevel);
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (enchantment == null || !(target instanceof Player p)) return false;
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item.getType().isAir()) return false;
        int level = item.getEnchantmentLevel(enchantment);
        return level >= minLevel;
    }
}
