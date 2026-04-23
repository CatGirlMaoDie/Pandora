package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.List;
import java.util.Locale;

/**
 * 向目标位置的箱子填充物品。
 * items|item|i=<材质>  amount|a=1  shouldEmptyChest|empty=false
 */
public class FillChestMechanic implements Mechanic {

    private final ItemStack item;
    private final boolean emptyFirst;

    public FillChestMechanic(String materialName, int amount, boolean emptyFirst) {
        Material mat = null;
        if (materialName != null && !materialName.isBlank()) {
            String name = materialName.contains(":")
                ? materialName.substring(materialName.indexOf(':') + 1).toUpperCase(Locale.ROOT)
                : materialName.toUpperCase(Locale.ROOT);
            try { mat = Material.valueOf(name); }
            catch (IllegalArgumentException e) { mat = MaterialUtil.matchMaterialByNameOrChinese(materialName); }
        }
        this.item = mat != null ? new ItemStack(mat, Math.max(1, amount)) : null;
        this.emptyFirst = emptyFirst;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (item == null || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null) continue;
            Location loc = target.getLocation();
            if (loc.getWorld() == null) continue;
            Block block = loc.getBlock();
            if (!(block.getState() instanceof Chest chest)) continue;
            Inventory inv = chest.getInventory();
            if (emptyFirst) inv.clear();
            inv.addItem(item.clone());
            any = true;
        }
        return any;
    }
}
