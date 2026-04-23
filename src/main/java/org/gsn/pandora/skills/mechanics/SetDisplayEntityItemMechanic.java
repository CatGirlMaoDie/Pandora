package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.List;
import java.util.Locale;

/**
 * 设置 ItemDisplay 实体显示的物品。
 * item|i|type|t|material|mat|m=<材质>
 */
public class SetDisplayEntityItemMechanic implements Mechanic {

    private final ItemStack item;

    public SetDisplayEntityItemMechanic(String materialName) {
        org.bukkit.Material mat = null;
        if (materialName != null && !materialName.isBlank()) {
            String name = materialName.contains(":")
                ? materialName.substring(materialName.indexOf(':') + 1).toUpperCase(Locale.ROOT)
                : materialName.toUpperCase(Locale.ROOT);
            try { mat = org.bukkit.Material.valueOf(name); }
            catch (IllegalArgumentException e) { mat = MaterialUtil.matchMaterialByNameOrChinese(materialName); }
        }
        this.item = mat != null ? new ItemStack(mat) : null;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (item == null || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof ItemDisplay id)) continue;
            id.setItemStack(item.clone());
            any = true;
        }
        return any;
    }
}
