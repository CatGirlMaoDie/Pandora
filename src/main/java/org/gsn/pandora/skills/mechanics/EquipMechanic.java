package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.List;
import java.util.Locale;

/**
 * 给目标实体装备物品（支持 item:slot 格式）。
 * items|item|i|equipment|equip|e=<材质>:<槽位>
 * 槽位: HAND/OFF_HAND/HEAD/CHEST/LEGS/FEET
 */
public class EquipMechanic implements Mechanic {

    private final ItemStack item;
    private final String slot;

    public EquipMechanic(String spec) {
        String mat = spec;
        String sl = "HAND";
        if (spec != null && spec.contains(":")) {
            String[] parts = spec.split(":", 2);
            mat = parts[0].trim();
            sl = parts[1].trim().toUpperCase(Locale.ROOT);
        }
        org.bukkit.Material m = null;
        if (mat != null && !mat.isBlank()) {
            String name = mat.contains("minecraft:")
                ? mat.substring(mat.indexOf(':') + 1).toUpperCase(Locale.ROOT)
                : mat.toUpperCase(Locale.ROOT);
            try { m = org.bukkit.Material.valueOf(name); }
            catch (IllegalArgumentException e) { m = MaterialUtil.matchMaterialByNameOrChinese(mat); }
        }
        this.item = m != null ? new ItemStack(m) : null;
        this.slot = sl;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (item == null || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.getEquipment() == null) continue;
            var eq = target.getEquipment();
            switch (slot) {
                case "OFF_HAND", "OFFHAND" -> eq.setItemInOffHand(item.clone());
                case "HEAD", "HELMET" -> eq.setHelmet(item.clone());
                case "CHEST", "CHESTPLATE" -> eq.setChestplate(item.clone());
                case "LEGS", "LEGGINGS" -> eq.setLeggings(item.clone());
                case "FEET", "BOOTS" -> eq.setBoots(item.clone());
                default -> eq.setItemInMainHand(item.clone());
            }
            any = true;
        }
        return any;
    }
}
