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
 * 给目标实体装备物品到指定槽位。
 * item|i=<材质>  slot|s=0 (0=主手,1=靴子,2=护腿,3=胸甲,4=头盔,5=副手)
 */
public class CquipMechanic implements Mechanic {

    private final ItemStack item;
    private final int slot;

    public CquipMechanic(String materialName, int slot) {
        org.bukkit.Material mat = null;
        if (materialName != null && !materialName.isBlank()) {
            String name = materialName.contains(":")
                ? materialName.substring(materialName.indexOf(':') + 1).toUpperCase(Locale.ROOT)
                : materialName.toUpperCase(Locale.ROOT);
            try { mat = org.bukkit.Material.valueOf(name); }
            catch (IllegalArgumentException e) { mat = MaterialUtil.matchMaterialByNameOrChinese(materialName); }
        }
        this.item = mat != null ? new ItemStack(mat) : null;
        this.slot = slot;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (item == null || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.getEquipment() == null) continue;
            var eq = target.getEquipment();
            switch (slot) {
                case 0 -> eq.setItemInMainHand(item.clone());
                case 1 -> eq.setBoots(item.clone());
                case 2 -> eq.setLeggings(item.clone());
                case 3 -> eq.setChestplate(item.clone());
                case 4 -> eq.setHelmet(item.clone());
                case 5 -> eq.setItemInOffHand(item.clone());
                default -> eq.setItemInMainHand(item.clone());
            }
            any = true;
        }
        return any;
    }
}
