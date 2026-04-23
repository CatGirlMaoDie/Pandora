package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标周围指定距离内是否有玩家（仇恨威胁）。distance|d=16 */
public class ThreatWithinCondition implements Condition {
    private final double distanceSq;
    public ThreatWithinCondition(double distance) {
        double d = Math.max(0, distance);
        this.distanceSq = d * d;
    }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        for (Player p : target.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(target.getLocation()) <= distanceSq) return true;
        }
        return false;
    }
}
