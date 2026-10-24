package org.gsn.pandora.mobs;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.TriggerType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 一个自定义实体类型的静态配置。
 * 只负责"如何配置"，不负责生成实体或事件逻辑。
 */
public class MobType {

    private final String id;
    private final EntityType entityType;
    private final String displayName;
    private final String faction;
    private final double health;
    private final double damage;
    private final double armor;
    private final MobOptions options;
    private final Map<TriggerType, List<String>> triggerSkills;
    private final List<MobDrop> drops;
    private final MobEquipment equipment;
    private final long timerIntervalTicks;
    /** AI 脚本路径（相对 scripts/），null 表示不使用 JS AI */
    private final String aiScript;
    /** AI 脚本函数名 */
    private final String aiFunction;
    /** 是否启用威胁表（锁定累计伤害最高的玩家为目标） */
    private final boolean threatTable;
    /** 实体等级（0 = 无等级） */
    private final int level;
    /** 伤害调整：伤害原因名(大写) -> 倍率（对应 Wiki「伤害调整」） */
    private final Map<String, Double> damageModifiers;

    public MobType(String id,
                   EntityType entityType,
                   String displayName,
                   String faction,
                   double health,
                   double damage,
                   double armor,
                   MobOptions options,
                   Map<TriggerType, List<String>> triggerSkills,
                   List<MobDrop> drops,
                   MobEquipment equipment,
                   long timerIntervalTicks,
                   String aiScript,
                   String aiFunction,
                   boolean threatTable,
                   int level,
                   Map<String, Double> damageModifiers) {
        this.id = id;
        this.entityType = entityType;
        this.displayName = displayName;
        this.faction = faction;
        this.health = health;
        this.damage = damage;
        this.armor = armor;
        this.options = options;
        this.triggerSkills = triggerSkills;
        this.drops = drops == null ? Collections.emptyList() : drops;
        this.equipment = equipment;
        this.timerIntervalTicks = timerIntervalTicks;
        this.aiScript = aiScript;
        this.aiFunction = aiFunction;
        this.threatTable = threatTable;
        this.level = level;
        this.damageModifiers = damageModifiers == null ? Collections.emptyMap() : damageModifiers;
    }

    public String getId() {
        return id;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFaction() {
        return faction;
    }

    public double getHealth() {
        return health;
    }

    public double getDamage() {
        return damage;
    }

    public double getArmor() {
        return armor;
    }

    public MobOptions getOptions() {
        return options;
    }

    public List<String> getSkillsForTrigger(TriggerType triggerType) {
        if (triggerSkills == null || triggerType == null) {
            return Collections.emptyList();
        }
        List<String> list = triggerSkills.get(triggerType);
        if (list == null) {
            return Collections.emptyList();
        }
        return list;
    }

    public List<MobDrop> getDrops() {
        return drops;
    }

    public MobEquipment getEquipment() {
        return equipment;
    }

    public long getTimerIntervalTicks() {
        return timerIntervalTicks;
    }

    public String getAiScript() {
        return aiScript;
    }

    public String getAiFunction() {
        return aiFunction;
    }

    public boolean hasAiScript() {
        return aiScript != null && !aiScript.isEmpty();
    }

    public boolean hasThreatTable() {
        return threatTable;
    }

    public int getLevel() {
        return level;
    }

    /**
     * 获取指定伤害原因的倍率，未配置返回 1.0（不调整）。
     */
    public double getDamageModifier(String damageCauseName) {
        if (damageCauseName == null || damageModifiers.isEmpty()) return 1.0;
        Double mult = damageModifiers.get(damageCauseName.toUpperCase(java.util.Locale.ROOT));
        return mult == null ? 1.0 : mult;
    }

    /**
     * 将本类型的静态属性应用到实体身上。
     */
    public void applyTo(LivingEntity entity) {
        // 名字
        if (displayName != null && !displayName.isEmpty()) {
            entity.setCustomName(ChatColor.translateAlternateColorCodes('&', displayName));
            entity.setCustomNameVisible(true);
        }

        // 血量
        if (health > 0) {
            // 1.21+ 中 GENERIC_MAX_HEALTH 已重命名为 MAX_HEALTH
            AttributeInstance maxHealth = entity.getAttribute(Attribute.MAX_HEALTH);
            if (maxHealth != null) {
                maxHealth.setBaseValue(health);
            }
            entity.setHealth(Math.min(health, entity.getMaxHealth()));
        }

        // 基础攻击
        if (damage > 0) {
            AttributeInstance attack = entity.getAttribute(Attribute.ATTACK_DAMAGE);
            if (attack != null) {
                attack.setBaseValue(damage);
            }
        }

        // 护甲
        if (armor > 0) {
            AttributeInstance armorAttr = entity.getAttribute(Attribute.ARMOR);
            if (armorAttr != null) {
                armorAttr.setBaseValue(armor);
            }
        }

        // 选项
        if (options != null) {
            if (options.isSilent()) {
                entity.setSilent(true);
            }

            if (options.getMovementSpeed() != null) {
                AttributeInstance speed = entity.getAttribute(Attribute.MOVEMENT_SPEED);
                if (speed != null) {
                    speed.setBaseValue(options.getMovementSpeed());
                }
            }

            if (options.getDespawn() != null) {
                // true = 按原版规则可自然消失; false = 不要自然消失
                entity.setRemoveWhenFarAway(options.getDespawn());
            }

            // AI 开关（对应 MythicMobs 的 NoAI）
            if (options.getNoAI() != null) {
                entity.setAI(!options.getNoAI());
            }

            // 发光效果
            if (options.getGlowing() != null) {
                entity.setGlowing(options.getGlowing());
            }

            // 隐身
            if (options.getInvisible() != null) {
                entity.setInvisible(options.getInvisible());
            }

            // 重力
            if (options.getHasGravity() != null) {
                entity.setGravity(options.getHasGravity());
            }

            // 可碰撞
            if (options.getCollidable() != null) {
                entity.setCollidable(options.getCollidable());
            }

            // 额外属性：跟随距离、击退抗性、攻击速度
            if (options.getFollowRange() != null) {
                AttributeInstance followRange = entity.getAttribute(Attribute.FOLLOW_RANGE);
                if (followRange != null) {
                    followRange.setBaseValue(options.getFollowRange());
                }
            }

            if (options.getKnockbackResistance() != null) {
                AttributeInstance knockback = entity.getAttribute(Attribute.KNOCKBACK_RESISTANCE);
                if (knockback != null) {
                    knockback.setBaseValue(options.getKnockbackResistance());
                }
            }

            if (options.getAttackSpeed() != null) {
                AttributeInstance attackSpeed = entity.getAttribute(Attribute.ATTACK_SPEED);
                if (attackSpeed != null) {
                    attackSpeed.setBaseValue(options.getAttackSpeed());
                }
            }
        }
    }

    /**
     * 对应 mobs.yml 中 Options 小节。
     */
    public static class MobOptions {
        private final boolean silent;
        private final Boolean preventOtherDrops; // 先解析出来，后面在死亡事件里用
        private final Boolean despawn;
        private final Double movementSpeed;
        private final Boolean noAI;
        private final Boolean glowing;
        private final Boolean invisible;
        private final Boolean hasGravity;
        private final Boolean collidable;
        private final Double followRange;
        private final Double knockbackResistance;
        private final Double attackSpeed;

        public MobOptions(boolean silent,
                          Boolean preventOtherDrops,
                          Boolean despawn,
                          Double movementSpeed,
                          Boolean noAI,
                          Boolean glowing,
                          Boolean invisible,
                          Boolean hasGravity,
                          Boolean collidable,
                          Double followRange,
                          Double knockbackResistance,
                          Double attackSpeed) {
            this.silent = silent;
            this.preventOtherDrops = preventOtherDrops;
            this.despawn = despawn;
            this.movementSpeed = movementSpeed;
            this.noAI = noAI;
            this.glowing = glowing;
            this.invisible = invisible;
            this.hasGravity = hasGravity;
            this.collidable = collidable;
            this.followRange = followRange;
            this.knockbackResistance = knockbackResistance;
            this.attackSpeed = attackSpeed;
        }

        public boolean isSilent() {
            return silent;
        }

        public Boolean getPreventOtherDrops() {
            return preventOtherDrops;
        }

        public Boolean getDespawn() {
            return despawn;
        }

        public Double getMovementSpeed() {
            return movementSpeed;
        }

        public Boolean getNoAI() {
            return noAI;
        }

        public Boolean getGlowing() {
            return glowing;
        }

        public Boolean getInvisible() {
            return invisible;
        }

        public Boolean getHasGravity() {
            return hasGravity;
        }

        public Boolean getCollidable() {
            return collidable;
        }

        public Double getFollowRange() {
            return followRange;
        }

        public Double getKnockbackResistance() {
            return knockbackResistance;
        }

        public Double getAttackSpeed() {
            return attackSpeed;
        }
    }
}
