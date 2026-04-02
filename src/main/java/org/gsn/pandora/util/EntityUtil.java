package org.gsn.pandora.util;

import org.bukkit.EntityEffect;
import org.bukkit.entity.EntityType;

import java.util.Locale;

/**
 * 实体类型解析工具，支持中文/别名。
 *
 * 注意：这里只做一个简单示例映射，不会覆盖所有原版实体。
 * 后续可以根据需要逐步扩充表。
 */
public final class EntityUtil {

    private EntityUtil() {
    }

    /**
     * 尝试根据英文名或中文名匹配 EntityType。
     *
     * @param name 配置中写的名字，可以是枚举 ID（ZOMBIE）、英文名（Zombie）、中文名（僵尸）等
     */
    public static EntityType matchEntityTypeByNameOrChinese(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        String upper = name.toUpperCase(Locale.ROOT).trim();

        // 1. 先尝试直接按枚举 ID 解析
        try {
            return EntityType.valueOf(upper);
        } catch (IllegalArgumentException ignored) {
        }

        // 2. 再处理常见中文 / 英文显示名的简易映射
        String normalized = name.trim();
        switch (normalized) {
            // --- 中文映射示例 ---
            case "僵尸":
                return EntityType.ZOMBIE;
            case "骷髅":
                return EntityType.SKELETON;
            case "凋零骷髅":
            case "凋灵骷髅":
                return EntityType.WITHER_SKELETON;
            case "苦力怕":
                return EntityType.CREEPER;
            case "蜘蛛":
                return EntityType.SPIDER;
            case "洞穴蜘蛛":
                return EntityType.CAVE_SPIDER;
            case "末影人":
                return EntityType.ENDERMAN;
            case "末影龙":
                return EntityType.ENDER_DRAGON;
            case "铁傀儡":
            case "铁巨人":
                return EntityType.IRON_GOLEM;
            case "雪傀儡":
                return EntityType.SNOW_GOLEM;
            case "猪灵":
                return EntityType.PIGLIN;
            case "猪灵蛮兵":
                return EntityType.PIGLIN_BRUTE;
            case "僵尸猪灵":
            case "僵尸猪人":
                return EntityType.ZOMBIFIED_PIGLIN;
            case "凋零":
                return EntityType.WITHER;
            case "村民":
                return EntityType.VILLAGER;
            case "流浪商人":
                return EntityType.WANDERING_TRADER;

            // --- 英文显示名的宽松匹配 ---
            case "Zombie":
                return EntityType.ZOMBIE;
            case "Skeleton":
                return EntityType.SKELETON;
            case "Wither Skeleton":
                return EntityType.WITHER_SKELETON;
            case "Creeper":
                return EntityType.CREEPER;
            case "Spider":
                return EntityType.SPIDER;
            case "Cave Spider":
                return EntityType.CAVE_SPIDER;
            case "Enderman":
                return EntityType.ENDERMAN;
            case "Ender Dragon":
                return EntityType.ENDER_DRAGON;
            case "Iron Golem":
                return EntityType.IRON_GOLEM;
            case "Snow Golem":
                return EntityType.SNOW_GOLEM;
            case "Piglin":
                return EntityType.PIGLIN;
            case "Piglin Brute":
                return EntityType.PIGLIN_BRUTE;
            case "Zombified Piglin":
                return EntityType.ZOMBIFIED_PIGLIN;
            case "Wither":
                return EntityType.WITHER;
            case "Villager":
                return EntityType.VILLAGER;
            case "Wandering Trader":
                return EntityType.WANDERING_TRADER;
            default:
                return null;
        }
    }
}
