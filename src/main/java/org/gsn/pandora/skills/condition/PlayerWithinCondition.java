package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标周围指定距离内是否有玩家。 */
public class PlayerWithinCondition implements Condition {

    private final double distanceSquared;

    public PlayerWithinCondition(double distance) {
        double d = Math.max(0.0, distance);
        this.distanceSquared = d * d;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        for (Player player : target.getWorld().getPlayers()) {
            if (player.getLocation().distanceSquared(target.getLocation()) <= distanceSquared) {
                return true;
            }
        }
        return false;
    }
}
