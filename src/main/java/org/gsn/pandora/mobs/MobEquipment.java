package org.gsn.pandora.mobs;

import org.bukkit.Material;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

/**
 * 代表一个 Mob 的装备配置。
 * 配置节点 Equipment:
 *   MainHand: IRON_SWORD
 *   OffHand: SHIELD
 *   Helmet: IRON_HELMET
 *   Chestplate: IRON_CHESTPLATE
 *   Leggings: IRON_LEGGINGS
 *   Boots: IRON_BOOTS
 *   MainHandDropChance: 0.0   # 0-1，默认 0（不掉落）
 *   HelmetDropChance: 0.0
 *   ...
 */
public class MobEquipment {

    private final ItemStack mainHand;
    private final ItemStack offHand;
    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;

    private final float mainHandDropChance;
    private final float offHandDropChance;
    private final float helmetDropChance;
    private final float chestplateDropChance;
    private final float leggingsDropChance;
    private final float bootsDropChance;

    public MobEquipment(ItemStack mainHand, ItemStack offHand,
                        ItemStack helmet, ItemStack chestplate,
                        ItemStack leggings, ItemStack boots,
                        float mainHandDropChance, float offHandDropChance,
                        float helmetDropChance, float chestplateDropChance,
                        float leggingsDropChance, float bootsDropChance) {
        this.mainHand = mainHand;
        this.offHand = offHand;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.mainHandDropChance = mainHandDropChance;
        this.offHandDropChance = offHandDropChance;
        this.helmetDropChance = helmetDropChance;
        this.chestplateDropChance = chestplateDropChance;
        this.leggingsDropChance = leggingsDropChance;
        this.bootsDropChance = bootsDropChance;
    }

    /** 将装备应用到实体身上。 */
    public void applyTo(EntityEquipment eq) {
        if (eq == null) return;
        if (mainHand != null) { eq.setItemInMainHand(mainHand); eq.setItemInMainHandDropChance(mainHandDropChance); }
        if (offHand != null)  { eq.setItemInOffHand(offHand);   eq.setItemInOffHandDropChance(offHandDropChance); }
        if (helmet != null)      { eq.setHelmet(helmet);           eq.setHelmetDropChance(helmetDropChance); }
        if (chestplate != null)  { eq.setChestplate(chestplate);   eq.setChestplateDropChance(chestplateDropChance); }
        if (leggings != null)    { eq.setLeggings(leggings);       eq.setLeggingsDropChance(leggingsDropChance); }
        if (boots != null)       { eq.setBoots(boots);             eq.setBootsDropChance(bootsDropChance); }
    }

    /** 从材质名字符串解析 ItemStack，失败返回 null。 */
    public static ItemStack parseMaterial(String name) {
        if (name == null || name.isBlank()) return null;
        String matName = name.contains(":")
            ? name.substring(name.indexOf(':') + 1).toUpperCase(Locale.ROOT)
            : name.toUpperCase(Locale.ROOT);
        try {
            Material mat = Material.valueOf(matName);
            return mat.isAir() ? null : new ItemStack(mat);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
