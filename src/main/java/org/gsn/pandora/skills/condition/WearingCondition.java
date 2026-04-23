package org.gsn.pandora.skills.condition;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测目标指定装备槽位的物品类型。slot: HEAD/CHEST/LEGS/FEET/HAND/OFF_HAND */
public class WearingCondition implements Condition {

    public enum ArmorSlot { HEAD, CHEST, LEGS, FEET, HAND, OFF_HAND }

    private final ArmorSlot slot;
    private final Set<Material> materials;

    public WearingCondition(String slotName, String typeList) {
        ArmorSlot s = ArmorSlot.HEAD;
        try { s = ArmorSlot.valueOf(slotName.toUpperCase(Locale.ROOT)); }
        catch (Exception ignored) {}
        this.slot = s;
        this.materials = Arrays.stream(typeList.split(","))
            .map(str -> {
                String name = str.trim().toUpperCase(Locale.ROOT);
                try { return Material.valueOf(name); }
                catch (IllegalArgumentException e) { return MaterialUtil.matchMaterialByNameOrChinese(name); }
            })
            .filter(m -> m != null)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || materials.isEmpty()) return false;
        EntityEquipment eq = target.getEquipment();
        if (eq == null) return false;
        ItemStack item = switch (slot) {
            case HEAD -> eq.getHelmet();
            case CHEST -> eq.getChestplate();
            case LEGS -> eq.getLeggings();
            case FEET -> eq.getBoots();
            case HAND -> eq.getItemInMainHand();
            case OFF_HAND -> eq.getItemInOffHand();
        };
        if (item == null) return false;
        return materials.contains(item.getType());
    }
}
