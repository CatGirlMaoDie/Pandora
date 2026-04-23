package org.gsn.pandora.util;

import org.bukkit.entity.EntityType;

import java.util.Locale;

/**
 * 实体类型解析工具，支持中文/别名/英文显示名。
 */
public final class EntityUtil {

    private EntityUtil() {}

    public static EntityType matchEntityTypeByNameOrChinese(String name) {
        if (name == null || name.isEmpty()) return null;

        // 1. 枚举 ID 直接匹配
        try {
            return EntityType.valueOf(name.toUpperCase(Locale.ROOT).trim());
        } catch (IllegalArgumentException ignored) {}

        // 2. 中文 / 英文显示名映射
        switch (name.trim()) {
            // ── 普通怪物 ──
            case "僵尸": case "Zombie":                         return EntityType.ZOMBIE;
            case "骷髅": case "Skeleton":                       return EntityType.SKELETON;
            case "凋零骷髅": case "凋灵骷髅": case "Wither Skeleton": return EntityType.WITHER_SKELETON;
            case "苦力怕": case "Creeper":                      return EntityType.CREEPER;
            case "蜘蛛": case "Spider":                         return EntityType.SPIDER;
            case "洞穴蜘蛛": case "Cave Spider":                return EntityType.CAVE_SPIDER;
            case "末影人": case "Enderman":                     return EntityType.ENDERMAN;
            case "溺尸": case "Drowned":                        return EntityType.DROWNED;
            case "尸壳": case "Husk":                           return EntityType.HUSK;
            case "流髑": case "Stray":                          return EntityType.STRAY;
            case "幻翼": case "Phantom":                        return EntityType.PHANTOM;
            case "蠹虫": case "Silverfish":                     return EntityType.SILVERFISH;
            case "末影螨": case "Endermite":                    return EntityType.ENDERMITE;
            case "守卫者": case "Guardian":                     return EntityType.GUARDIAN;
            case "远古守卫者": case "Elder Guardian":           return EntityType.ELDER_GUARDIAN;
            case "女巫": case "Witch":                          return EntityType.WITCH;
            case "蜘蛛骑士": case "Spider Jockey":              return null; // 不是独立实体
            case "史莱姆": case "Slime":                        return EntityType.SLIME;
            case "岩浆怪": case "Magma Cube":                   return EntityType.MAGMA_CUBE;
            case "烈焰人": case "Blaze":                        return EntityType.BLAZE;
            case "恶魂": case "Ghast":                          return EntityType.GHAST;
            case "僵尸猪灵": case "僵尸猪人": case "Zombified Piglin": return EntityType.ZOMBIFIED_PIGLIN;
            case "猪灵": case "Piglin":                         return EntityType.PIGLIN;
            case "猪灵蛮兵": case "Piglin Brute":               return EntityType.PIGLIN_BRUTE;
            case "疣猪兽": case "Hoglin":                       return EntityType.HOGLIN;
            case "僵尸疣猪兽": case "Zoglin":                   return EntityType.ZOGLIN;
            case "恼鬼": case "Vex":                            return EntityType.VEX;
            case "卫道士": case "Vindicator":                   return EntityType.VINDICATOR;
            case "幻术师": case "Illusioner":                   return EntityType.ILLUSIONER;
            case "唤魔者": case "Evoker":                       return EntityType.EVOKER;
            case "劫掠兽": case "Ravager":                      return EntityType.RAVAGER;
            case "掠夺者": case "Pillager":                     return EntityType.PILLAGER;
            case "卫道士长": case "Vindicator Captain":         return EntityType.VINDICATOR;
            case "骷髅马": case "Skeleton Horse":               return EntityType.SKELETON_HORSE;
            case "僵尸马": case "Zombie Horse":                 return EntityType.ZOMBIE_HORSE;
            case "蜘蛛骑手": case "Spider Rider":               return null;
            case "末影龙": case "Ender Dragon":                 return EntityType.ENDER_DRAGON;
            case "凋零": case "Wither":                         return EntityType.WITHER;
            case "铁傀儡": case "铁巨人": case "Iron Golem":    return EntityType.IRON_GOLEM;
            case "雪傀儡": case "Snow Golem":                   return EntityType.SNOW_GOLEM;
            case "潜影贝": case "Shulker":                      return EntityType.SHULKER;
            case "监守者": case "Warden":                       return EntityType.WARDEN;
            case "荒古守卫": case "Ancient Guardian":           return EntityType.ELDER_GUARDIAN;
            case "蠹鱼": case "Silverfish2":                    return EntityType.SILVERFISH;
            // ── 中性/友好生物 ──
            case "村民": case "Villager":                       return EntityType.VILLAGER;
            case "流浪商人": case "Wandering Trader":           return EntityType.WANDERING_TRADER;
            case "猫": case "Cat":                              return EntityType.CAT;
            case "狼": case "Wolf":                             return EntityType.WOLF;
            case "马": case "Horse":                            return EntityType.HORSE;
            case "驴": case "Donkey":                           return EntityType.DONKEY;
            case "骡": case "Mule":                             return EntityType.MULE;
            case "猪": case "Pig":                              return EntityType.PIG;
            case "牛": case "Cow":                              return EntityType.COW;
            case "羊": case "Sheep":                            return EntityType.SHEEP;
            case "鸡": case "Chicken":                          return EntityType.CHICKEN;
            case "兔子": case "Rabbit":                         return EntityType.RABBIT;
            case "海龟": case "Turtle":                         return EntityType.TURTLE;
            case "狐狸": case "Fox":                            return EntityType.FOX;
            case "熊猫": case "Panda":                          return EntityType.PANDA;
            case "北极熊": case "Polar Bear":                   return EntityType.POLAR_BEAR;
            case "蜜蜂": case "Bee":                            return EntityType.BEE;
            case "山羊": case "Goat":                           return EntityType.GOAT;
            case "青蛙": case "Frog":                           return EntityType.FROG;
            case "蝌蚪": case "Tadpole":                        return EntityType.TADPOLE;
            case "悦灵": case "Allay":                          return EntityType.ALLAY;
            case "骆驼": case "Camel":                          return EntityType.CAMEL;
            case "犰狳": case "Armadillo":                      return EntityType.ARMADILLO;
            case "犰狳虫": case "Armadillo Scute":              return null;
            case "嗅探兽": case "Sniffer":                      return EntityType.SNIFFER;
            case "鱿鱼": case "Squid":                          return EntityType.SQUID;
            case "发光鱿鱼": case "Glow Squid":                 return EntityType.GLOW_SQUID;
            case "海豚": case "Dolphin":                        return EntityType.DOLPHIN;
            case "鳕鱼": case "Cod":                            return EntityType.COD;
            case "鲑鱼": case "Salmon":                         return EntityType.SALMON;
            case "热带鱼": case "Tropical Fish":                return EntityType.TROPICAL_FISH;
            case "河豚": case "Pufferfish":                     return EntityType.PUFFERFISH;
            case "美西螈": case "Axolotl":                      return EntityType.AXOLOTL;
            case "蝙蝠": case "Bat":                            return EntityType.BAT;
            case "鹦鹉": case "Parrot":                         return EntityType.PARROT;
            case "驼鹿": case "Mooshroom":                      return EntityType.MOOSHROOM;
            case "哞菇": case "Mushroom Cow":                   return EntityType.MOOSHROOM;
            case "羊驼": case "Llama":                          return EntityType.LLAMA;
            case "流浪羊驼": case "Trader Llama":               return EntityType.TRADER_LLAMA;
            case "猪灵商人": case "Piglin Trader":              return EntityType.PIGLIN;
            case "僵尸村民": case "Zombie Villager":            return EntityType.ZOMBIE_VILLAGER;
            case "女巫村民": case "Witch Villager":             return EntityType.WITCH;
            case "末影人村民": case "Enderman Villager":        return EntityType.ENDERMAN;
            // ── 抛射物/其他 ──
            case "箭": case "Arrow":                            return EntityType.ARROW;
            case "火球": case "Fireball":                       return EntityType.FIREBALL;
            case "小火球": case "Small Fireball":               return EntityType.SMALL_FIREBALL;
            case "末影珍珠": case "Ender Pearl":                return EntityType.ENDER_PEARL;
            case "雪球": case "Snowball":                       return EntityType.SNOWBALL;
            case "鸡蛋": case "Egg":                            return EntityType.EGG;
            case "三叉戟": case "Trident":                      return EntityType.TRIDENT;
            case "船": case "Boat":                             return EntityType.OAK_BOAT;
            case "矿车": case "Minecart":                       return EntityType.MINECART;
            case "盔甲架": case "Armor Stand":                  return EntityType.ARMOR_STAND;
            case "物品展示框": case "Item Frame":               return EntityType.ITEM_FRAME;
            case "画": case "Painting":                         return EntityType.PAINTING;
            case "掉落物": case "Item":                         return EntityType.ITEM;
            case "经验球": case "Experience Orb":               return EntityType.EXPERIENCE_ORB;
            default:                                            return null;
        }
    }
}
