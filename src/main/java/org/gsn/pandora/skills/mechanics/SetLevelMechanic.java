package org.gsn.pandora.skills.mechanics;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 设置目标实体的 Pandora 等级（对应 Wiki「等级」）。
 * level|l=<等级>
 * 等级通过 1.25 血量与 0.25 近战伤害的默认等级调整值成长，
 * 可被 PandoraLevelEvent / 等级调整配置使用。
 */
public class SetLevelMechanic implements Mechanic {

    public static final NamespacedKey LEVEL_KEY = new NamespacedKey("pandora", "level");
    /** 默认每级加成：血量 +0.25 倍基数，伤害 +0.25 倍基数（对应 MythicMobs 默认） */
    public static final double HEALTH_PER_LEVEL = 0.25;
    public static final double DAMAGE_PER_LEVEL = 0.25;

    private final int level;

    public SetLevelMechanic(int level) {
        this.level = level;
    }

    public static int getLevel(LivingEntity entity) {
        if (entity == null) return 0;
        Integer lvl = entity.getPersistentDataContainer().get(LEVEL_KEY, PersistentDataType.INTEGER);
        return lvl == null ? 0 : lvl;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            applyLevel(target, level);
            any = true;
        }
        return any;
    }

    /** 将等级应用到实体：记录 PDC 并按调整值增强血量/伤害。 */
    public static void applyLevel(LivingEntity entity, int newLevel) {
        if (entity == null || newLevel < 0) return;
        entity.getPersistentDataContainer().set(LEVEL_KEY, PersistentDataType.INTEGER, newLevel);

        // 基于当前基础值成长（与 MythicMobs 默认仅调整血量/近战伤害一致）
        var maxHealth = entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH);
        if (maxHealth != null) {
            double base = maxHealth.getBaseValue() / (1.0 + HEALTH_PER_LEVEL * getLevel(entity));
            maxHealth.setBaseValue(base * (1.0 + HEALTH_PER_LEVEL * newLevel));
            entity.setHealth(Math.min(entity.getHealth(), entity.getMaxHealth()));
        }
        var attack = entity.getAttribute(org.bukkit.attribute.Attribute.ATTACK_DAMAGE);
        if (attack != null) {
            double base = attack.getBaseValue() / (1.0 + DAMAGE_PER_LEVEL * getLevel(entity));
            attack.setBaseValue(base * (1.0 + DAMAGE_PER_LEVEL * newLevel));
        }
    }
}
