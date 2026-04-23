package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测施法者周围威胁（玩家）不在指定距离内的数量。distance|d=16  amount|a=1 */
public class ThreatNotWithinCondition implements Condition {

    private final double distanceSq;
    private final int minAmount;

    public ThreatNotWithinCondition(double distance, int minAmount) {
        double d = Math.max(0, distance);
        this.distanceSq = d * d;
        this.minAmount = Math.max(1, minAmount);
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        int count = 0;
        for (Player p : target.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(target.getLocation()) > distanceSq) count++;
        }
        return count >= minAmount;
    }
}
