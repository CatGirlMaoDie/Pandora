package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.List;
import java.util.Locale;

/**
 * 给目标玩家物品。
 * items|item|i=<材质>  amount|a=1  doFakeLooting|fl=false
 */
public class GiveItemMechanic implements Mechanic {

    private final ItemStack item;

    public GiveItemMechanic(String materialName, int amount) {
        org.bukkit.Material mat = null;
        if (materialName != null && !materialName.isBlank()) {
            String name = materialName.contains(":")
                ? materialName.substring(materialName.indexOf(':') + 1).toUpperCase(Locale.ROOT)
                : materialName.toUpperCase(Locale.ROOT);
            try { mat = org.bukkit.Material.valueOf(name); }
            catch (IllegalArgumentException e) { mat = MaterialUtil.matchMaterialByNameOrChinese(materialName); }
        }
        this.item = mat != null ? new ItemStack(mat, Math.max(1, amount)) : null;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (item == null || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player) || target.isDead()) continue;
            ((Player) target).getInventory().addItem(item.clone());
            any = true;
        }
        return any;
    }
}
