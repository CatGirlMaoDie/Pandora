package org.gsn.pandora.util;

import org.bukkit.Material;

import java.util.Locale;

/**
 * 物品/方块类型解析工具，支持中文/别名/英文显示名。
 */
public final class MaterialUtil {

    private MaterialUtil() {}

    public static Material matchMaterialByNameOrChinese(String name) {
        if (name == null || name.isEmpty()) return null;

        // 1. 枚举 ID 直接匹配（去掉 minecraft: 前缀）
        String upper = name.toUpperCase(Locale.ROOT).trim();
        if (upper.startsWith("MINECRAFT:")) upper = upper.substring(10);
        try {
            return Material.valueOf(upper);
        } catch (IllegalArgumentException ignored) {}

        // 2. 中文 / 英文显示名映射
        switch (name.trim()) {
            // ── 基础方块 ──
            case "石头": case "Stone":                          return Material.STONE;
            case "圆石": case "Cobblestone":                    return Material.COBBLESTONE;
            case "泥土": case "Dirt":                           return Material.DIRT;
            case "草方块": case "Grass Block":                  return Material.GRASS_BLOCK;
            case "沙子": case "Sand":                           return Material.SAND;
            case "沙砾": case "Gravel":                         return Material.GRAVEL;
            case "基岩": case "Bedrock":                        return Material.BEDROCK;
            case "黑曜石": case "Obsidian":                     return Material.OBSIDIAN;
            case "哭泣的黑曜石": case "Crying Obsidian":        return Material.CRYING_OBSIDIAN;
            case "冰": case "Ice":                              return Material.ICE;
            case "浮冰": case "Packed Ice":                     return Material.PACKED_ICE;
            case "蓝冰": case "Blue Ice":                       return Material.BLUE_ICE;
            case "雪": case "Snow":                             return Material.SNOW;
            case "雪块": case "Snow Block":                     return Material.SNOW_BLOCK;
            case "岩浆块": case "Magma Block":                  return Material.MAGMA_BLOCK;
            case "灵魂沙": case "Soul Sand":                    return Material.SOUL_SAND;
            case "灵魂土": case "Soul Soil":                    return Material.SOUL_SOIL;
            case "下界岩": case "Netherrack":                   return Material.NETHERRACK;
            case "下界砖": case "Nether Bricks":                return Material.NETHER_BRICKS;
            case "末地石": case "End Stone":                    return Material.END_STONE;
            case "末地石砖": case "End Stone Bricks":           return Material.END_STONE_BRICKS;
            case "深板岩": case "Deepslate":                    return Material.DEEPSLATE;
            case "凝灰岩": case "Tuff":                         return Material.TUFF;
            case "方解石": case "Calcite":                      return Material.CALCITE;
            case "岩浆": case "Lava":                           return Material.LAVA;
            case "水": case "Water":                            return Material.WATER;
            // ── 木材 ──
            case "橡木木板": case "橡木板": case "Oak Planks":  return Material.OAK_PLANKS;
            case "云杉木板": case "Spruce Planks":              return Material.SPRUCE_PLANKS;
            case "白桦木板": case "Birch Planks":               return Material.BIRCH_PLANKS;
            case "丛林木板": case "Jungle Planks":              return Material.JUNGLE_PLANKS;
            case "金合欢木板": case "Acacia Planks":            return Material.ACACIA_PLANKS;
            case "深色橡木板": case "Dark Oak Planks":          return Material.DARK_OAK_PLANKS;
            case "红树木板": case "Mangrove Planks":            return Material.MANGROVE_PLANKS;
            case "樱花木板": case "Cherry Planks":              return Material.CHERRY_PLANKS;
            case "竹木板": case "Bamboo Planks":                return Material.BAMBOO_PLANKS;
            // ── 矿石/锭 ──
            case "钻石": case "Diamond":                        return Material.DIAMOND;
            case "钻石块": case "Diamond Block":                return Material.DIAMOND_BLOCK;
            case "钻石矿石": case "Diamond Ore":                return Material.DIAMOND_ORE;
            case "金锭": case "Gold Ingot":                     return Material.GOLD_INGOT;
            case "金块": case "Gold Block":                     return Material.GOLD_BLOCK;
            case "金矿石": case "Gold Ore":                     return Material.GOLD_ORE;
            case "铁锭": case "Iron Ingot":                     return Material.IRON_INGOT;
            case "铁块": case "Iron Block":                     return Material.IRON_BLOCK;
            case "铁矿石": case "Iron Ore":                     return Material.IRON_ORE;
            case "煤炭": case "Coal":                           return Material.COAL;
            case "煤炭块": case "Coal Block":                   return Material.COAL_BLOCK;
            case "煤矿石": case "Coal Ore":                     return Material.COAL_ORE;
            case "绿宝石": case "Emerald":                      return Material.EMERALD;
            case "绿宝石块": case "Emerald Block":              return Material.EMERALD_BLOCK;
            case "绿宝石矿石": case "Emerald Ore":              return Material.EMERALD_ORE;
            case "青金石": case "Lapis Lazuli":                 return Material.LAPIS_LAZULI;
            case "青金石块": case "Lapis Block":                return Material.LAPIS_BLOCK;
            case "红石": case "Redstone":                       return Material.REDSTONE;
            case "红石块": case "Redstone Block":               return Material.REDSTONE_BLOCK;
            case "石英": case "Quartz":                         return Material.QUARTZ;
            case "石英块": case "Quartz Block":                 return Material.QUARTZ_BLOCK;
            case "下界合金锭": case "Netherite Ingot":          return Material.NETHERITE_INGOT;
            case "下界合金块": case "Netherite Block":          return Material.NETHERITE_BLOCK;
            case "铜锭": case "Copper Ingot":                   return Material.COPPER_INGOT;
            case "铜块": case "Copper Block":                   return Material.COPPER_BLOCK;
            case "紫水晶碎片": case "Amethyst Shard":           return Material.AMETHYST_SHARD;
            // ── 工具/武器/防具 ──
            case "木剑": case "Wooden Sword":                   return Material.WOODEN_SWORD;
            case "石剑": case "Stone Sword":                    return Material.STONE_SWORD;
            case "铁剑": case "Iron Sword":                     return Material.IRON_SWORD;
            case "金剑": case "Golden Sword":                   return Material.GOLDEN_SWORD;
            case "钻石剑": case "Diamond Sword":                return Material.DIAMOND_SWORD;
            case "下界合金剑": case "Netherite Sword":          return Material.NETHERITE_SWORD;
            case "弓": case "Bow":                              return Material.BOW;
            case "弩": case "Crossbow":                         return Material.CROSSBOW;
            case "三叉戟": case "Trident":                      return Material.TRIDENT;
            case "铁头盔": case "Iron Helmet":                  return Material.IRON_HELMET;
            case "铁胸甲": case "Iron Chestplate":              return Material.IRON_CHESTPLATE;
            case "铁护腿": case "Iron Leggings":                return Material.IRON_LEGGINGS;
            case "铁靴子": case "Iron Boots":                   return Material.IRON_BOOTS;
            case "钻石头盔": case "Diamond Helmet":             return Material.DIAMOND_HELMET;
            case "钻石胸甲": case "Diamond Chestplate":         return Material.DIAMOND_CHESTPLATE;
            case "钻石护腿": case "Diamond Leggings":           return Material.DIAMOND_LEGGINGS;
            case "钻石靴子": case "Diamond Boots":              return Material.DIAMOND_BOOTS;
            case "下界合金头盔": case "Netherite Helmet":       return Material.NETHERITE_HELMET;
            case "下界合金胸甲": case "Netherite Chestplate":   return Material.NETHERITE_CHESTPLATE;
            case "下界合金护腿": case "Netherite Leggings":     return Material.NETHERITE_LEGGINGS;
            case "下界合金靴子": case "Netherite Boots":        return Material.NETHERITE_BOOTS;
            case "盾牌": case "Shield":                         return Material.SHIELD;
            // ── 食物/消耗品 ──
            case "面包": case "Bread":                          return Material.BREAD;
            case "苹果": case "Apple":                          return Material.APPLE;
            case "金苹果": case "Golden Apple":                 return Material.GOLDEN_APPLE;
            case "附魔金苹果": case "Enchanted Golden Apple":   return Material.ENCHANTED_GOLDEN_APPLE;
            case "牛肉": case "Beef":                           return Material.BEEF;
            case "熟牛肉": case "Cooked Beef":                  return Material.COOKED_BEEF;
            case "猪肉": case "Porkchop":                       return Material.PORKCHOP;
            case "熟猪肉": case "Cooked Porkchop":              return Material.COOKED_PORKCHOP;
            case "鸡肉": case "Chicken":                        return Material.CHICKEN;
            case "熟鸡肉": case "Cooked Chicken":               return Material.COOKED_CHICKEN;
            case "腐肉": case "Rotten Flesh":                   return Material.ROTTEN_FLESH;
            case "骨头": case "Bone":                           return Material.BONE;
            case "蜘蛛眼": case "Spider Eye":                   return Material.SPIDER_EYE;
            case "火药": case "Gunpowder":                      return Material.GUNPOWDER;
            case "烈焰棒": case "Blaze Rod":                    return Material.BLAZE_ROD;
            case "烈焰粉": case "Blaze Powder":                 return Material.BLAZE_POWDER;
            case "末影珍珠": case "Ender Pearl":                return Material.ENDER_PEARL;
            case "末影之眼": case "Eye of Ender":               return Material.ENDER_EYE;
            case "下界之星": case "Nether Star":                return Material.NETHER_STAR;
            case "信标": case "Beacon":                         return Material.BEACON;
            case "经验瓶": case "Bottle o' Enchanting":         return Material.EXPERIENCE_BOTTLE;
            case "木棍": case "Stick":                          return Material.STICK;
            case "线": case "String":                           return Material.STRING;
            case "羽毛": case "Feather":                        return Material.FEATHER;
            case "皮革": case "Leather":                        return Material.LEATHER;
            case "纸": case "Paper":                            return Material.PAPER;
            case "书": case "Book":                             return Material.BOOK;
            case "附魔书": case "Enchanted Book":               return Material.ENCHANTED_BOOK;
            case "箱子": case "Chest":                          return Material.CHEST;
            case "熔炉": case "Furnace":                        return Material.FURNACE;
            case "工作台": case "Crafting Table":               return Material.CRAFTING_TABLE;
            case "铁砧": case "Anvil":                          return Material.ANVIL;
            case "附魔台": case "Enchanting Table":             return Material.ENCHANTING_TABLE;
            case "酿造台": case "Brewing Stand":                return Material.BREWING_STAND;
            case "末地传送门框架": case "End Portal Frame":     return Material.END_PORTAL_FRAME;
            case "龙蛋": case "Dragon Egg":                     return Material.DRAGON_EGG;
            case "凋零骷髅头颅": case "Wither Skeleton Skull":  return Material.WITHER_SKELETON_SKULL;
            case "玩家头颅": case "Player Head":                return Material.PLAYER_HEAD;
            case "僵尸头颅": case "Zombie Head":                return Material.ZOMBIE_HEAD;
            case "骷髅头颅": case "Skeleton Skull":             return Material.SKELETON_SKULL;
            case "苦力怕头颅": case "Creeper Head":             return Material.CREEPER_HEAD;
            case "龙头": case "Dragon Head":                    return Material.DRAGON_HEAD;
            // ── 药水 ──
            case "药水": case "Potion":                         return Material.POTION;
            case "喷溅药水": case "Splash Potion":              return Material.SPLASH_POTION;
            case "滞留药水": case "Lingering Potion":           return Material.LINGERING_POTION;
            default:                                            return null;
        }
    }
}
