package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/**
 * 检测目标玩家弓的拉弓力度（0-1）。force|f|value|v|val=0.5
 * 注：Paper 不直接暴露拉弓力度，通过 PlayerBowShotEvent 的 force 字段获取。
 * 此处通过检测玩家是否正在使用弓来近似判断。
 */
public class BowTensionCondition implements Condition {

    private final float minForce;

    public BowTensionCondition(float minForce) {
        this.minForce = Math.max(0f, Math.min(1f, minForce));
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (!(target instanceof Player p)) return false;
        // 检测玩家是否正在使用弓
        var item = p.getActiveItem();
        if (item == null || item.getType().isAir()) return false;
        return item.getType() == org.bukkit.Material.BOW
            || item.getType() == org.bukkit.Material.CROSSBOW;
    }
}
