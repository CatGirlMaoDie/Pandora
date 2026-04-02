package org.gsn.pandora.util;

import org.bukkit.Material;

import java.util.Locale;

/**
 * 物品/方块类型解析工具，支持中文/别名。
 *
 * 注意：这里只做一个简单示例映射，不会覆盖所有原版物品/方块。
 * 后续可以根据需要逐步扩充表。
 */
public final class MaterialUtil {

    private MaterialUtil() {
    }

    /**
     * 尝试根据英文名或中文名匹配 Material。
     *
     * @param name 配置中写的名字，可以是枚举 ID（STONE）、英文名（Stone）、中文名（石头）等
     */
    public static Material matchMaterialByNameOrChinese(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        String upper = name.toUpperCase(Locale.ROOT).trim();

        // 1. 先尝试直接按枚举 ID 解析
        try {
            return Material.valueOf(upper);
        } catch (IllegalArgumentException ignored) {
        }

        // 2. 再处理常见中文 / 英文显示名的简易映射
        String normalized = name.trim();
        switch (normalized) {
            // --- 中文映射示例 ---
            case "石头":
                return Material.STONE;
            case "圆石":
                return Material.COBBLESTONE;
            case "泥土":
                return Material.DIRT;
            case "草方块":
                return Material.GRASS_BLOCK;
            case "沙子":
                return Material.SAND;
            case "沙砾":
                return Material.GRAVEL;
            case "橡木木板":
            case "橡木板":
                return Material.OAK_PLANKS;
            case "箱子":
                return Material.CHEST;
            case "熔炉":
                return Material.FURNACE;
            case "钻石":
                return Material.DIAMOND;
            case "钻石块":
                return Material.DIAMOND_BLOCK;
            case "金锭":
                return Material.GOLD_INGOT;
            case "金块":
                return Material.GOLD_BLOCK;
            case "铁锭":
                return Material.IRON_INGOT;
            case "铁块":
                return Material.IRON_BLOCK;
            case "木棍":
                return Material.STICK;
            case "面包":
                return Material.BREAD;

            // --- 英文显示名的宽松匹配 ---
            case "Stone":
                return Material.STONE;
            case "Cobblestone":
                return Material.COBBLESTONE;
            case "Dirt":
                return Material.DIRT;
            case "Grass Block":
                return Material.GRASS_BLOCK;
            case "Sand":
                return Material.SAND;
            case "Gravel":
                return Material.GRAVEL;
            case "Oak Planks":
                return Material.OAK_PLANKS;
            case "Chest":
                return Material.CHEST;
            case "Furnace":
                return Material.FURNACE;
            case "Diamond":
                return Material.DIAMOND;
            case "Diamond Block":
                return Material.DIAMOND_BLOCK;
            case "Gold Ingot":
                return Material.GOLD_INGOT;
            case "Gold Block":
                return Material.GOLD_BLOCK;
            case "Iron Ingot":
                return Material.IRON_INGOT;
            case "Iron Block":
                return Material.IRON_BLOCK;
            case "Stick":
                return Material.STICK;
            case "Bread":
                return Material.BREAD;
            default:
                return null;
        }
    }
}
