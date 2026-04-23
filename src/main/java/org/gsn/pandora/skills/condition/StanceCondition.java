package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Locale;

/**
 * 检测目标玩家的姿态（STANDING/SNEAKING/SWIMMING/FLYING/SLEEPING/DEAD）。
 * stance|s=SNEAKING  strict|str=false
 */
public class StanceCondition implements Condition {

    private final String stance;

    public StanceCondition(String stance) {
        this.stance = stance == null ? "" : stance.toUpperCase(Locale.ROOT);
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || stance.isEmpty()) return false;
        return switch (stance) {
            case "SNEAKING", "CROUCHING" -> target.isSneaking();
            case "SWIMMING" -> target.isSwimming();
            case "FLYING" -> target instanceof Player p && p.isFlying();
            case "SLEEPING" -> target.isSleeping();
            case "DEAD" -> target.isDead();
            case "GLIDING" -> target.isGliding();
            case "STANDING" -> !target.isSneaking() && !target.isSwimming()
                && !target.isSleeping() && !target.isDead();
            default -> false;
        };
    }
}
