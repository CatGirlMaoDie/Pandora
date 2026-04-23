package org.gsn.pandora.skills.condition;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测服务器全局在线玩家数量范围。 */
public class PlayersOnlineCondition implements Condition {

    private final int minAmount;
    private final int maxAmount;

    public PlayersOnlineCondition(int minAmount, int maxAmount) {
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        int count = Bukkit.getOnlinePlayers().size();
        return count >= minAmount && count <= maxAmount;
    }
}
