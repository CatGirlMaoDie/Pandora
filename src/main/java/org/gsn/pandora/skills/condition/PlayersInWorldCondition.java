package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标所在世界的在线玩家数量范围。 */
public class PlayersInWorldCondition implements Condition {

    private final int minAmount;
    private final int maxAmount;

    public PlayersInWorldCondition(int minAmount, int maxAmount) {
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        int count = target.getWorld().getPlayers().size();
        return count >= minAmount && count <= maxAmount;
    }
}
