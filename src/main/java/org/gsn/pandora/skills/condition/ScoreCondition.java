package org.gsn.pandora.skills.condition;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标在指定记分板目标上的分数范围。 */
public class ScoreCondition implements Condition {

    private final String objective;
    private final int minValue;
    private final int maxValue;

    public ScoreCondition(String objective, int minValue, int maxValue) {
        this.objective = objective == null ? "" : objective;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || objective.isEmpty()) return false;
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective obj = board.getObjective(objective);
        if (obj == null) return false;
        String entry = target instanceof Player
            ? target.getName()
            : target.getUniqueId().toString();
        Score score = obj.getScore(entry);
        if (!score.isScoreSet()) return false;
        int value = score.getScore();
        return value >= minValue && value <= maxValue;
    }
}
