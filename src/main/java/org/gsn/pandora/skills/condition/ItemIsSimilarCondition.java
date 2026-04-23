package org.gsn.pandora.skills.condition;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.Locale;

/**
 * 检测目标玩家指定槽位的物品是否与指定材质相似。
 * slot|s=HAND  item|i|material|m|mm|mythicitem=DIRT
 */
public class ItemIsSimilarCondition implements Condition {

    private final String slot;
    private final Material material;

    public ItemIsSimilarCondition(String slot, String materialName) {
        this.slot = slot == null ? "HAND" : slot.toUpperCase(Locale.ROOT);
        Material mat = null;
        if (materialName != null && !materialName.isBlank()) {
            String name = materialName.contains(":")
                ? materialName.substring(materialName.indexOf(':') + 1).toUpperCase(Locale.ROOT)
                : materialName.toUpperCase(Locale.ROOT);
            try { mat = Material.valueOf(name); }
            catch (IllegalArgumentException e) { mat = MaterialUtil.matchMaterialByNameOrChinese(materialName); }
        }
        this.material = mat;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (!(target instanceof Player p) || material == null) return false;
        ItemStack item = switch (slot) {
            case "OFF_HAND", "OFFHAND" -> p.getInventory().getItemInOffHand();
            case "HEAD", "HELMET" -> p.getInventory().getHelmet();
            case "CHEST", "CHESTPLATE" -> p.getInventory().getChestplate();
            case "LEGS", "LEGGINGS" -> p.getInventory().getLeggings();
            case "FEET", "BOOTS" -> p.getInventory().getBoots();
            default -> p.getInventory().getItemInMainHand();
        };
        return item != null && item.getType() == material;
    }
}
