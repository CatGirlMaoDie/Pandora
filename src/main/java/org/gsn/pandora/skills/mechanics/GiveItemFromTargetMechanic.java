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
 * 从目标玩家背包中取出指定物品给施法者。
 * items|item|i=<材质>  amount|a=1
 */
public class GiveItemFromTargetMechanic implements Mechanic {

    private final org.bukkit.Material material;
    private final int amount;

    public GiveItemFromTargetMechanic(String materialName, int amount) {
        org.bukkit.Material mat = null;
        if (materialName != null && !materialName.isBlank()) {
            String name = materialName.contains(":")
                ? materialName.substring(materialName.indexOf(':') + 1).toUpperCase(Locale.ROOT)
                : materialName.toUpperCase(Locale.ROOT);
            try { mat = org.bukkit.Material.valueOf(name); }
            catch (IllegalArgumentException e) { mat = MaterialUtil.matchMaterialByNameOrChinese(materialName); }
        }
        this.material = mat;
        this.amount = Math.max(1, amount);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (material == null || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player victim)) continue;
            // 从目标背包中移除物品
            var inv = victim.getInventory();
            int remaining = amount;
            for (int i = 0; i < inv.getSize() && remaining > 0; i++) {
                ItemStack slot = inv.getItem(i);
                if (slot == null || slot.getType() != material) continue;
                int take = Math.min(slot.getAmount(), remaining);
                remaining -= take;
                if (slot.getAmount() - take <= 0) inv.setItem(i, null);
                else slot.setAmount(slot.getAmount() - take);
                // 给施法者
                if (caster instanceof Player giver) {
                    giver.getInventory().addItem(new ItemStack(material, take));
                }
            }
            if (remaining < amount) any = true;
        }
        return any;
    }
}
