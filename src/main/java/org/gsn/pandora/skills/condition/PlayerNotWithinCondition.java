package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标周围指定距离内是否没有玩家（playerWithin 的反向）。 */
public class PlayerNotWithinCondition implements Condition {

    private final double distanceSquared;

    public PlayerNotWithinCondition(double distance) {
        double d = Math.max(0.0, distance);
        this.distanceSquared = d * d;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        for (Player player : target.getWorld().getPlayers()) {
            if (player.getLocation().distanceSquared(target.getLocation()) <= distanceSquared) {
                return false;
            }
        }
        return true;
    }
}
