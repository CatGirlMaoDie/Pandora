package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标玩家击杀玩家数量范围。kills|k=min;maxKills=max */
public class PlayerKillsCondition implements Condition {
    private final int min, max;
    public PlayerKillsCondition(int min, int max) { this.min = min; this.max = max; }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (!(target instanceof Player p)) return false;
        int kills = p.getStatistic(org.bukkit.Statistic.PLAYER_KILLS);
        return kills >= min && kills <= max;
    }
}
