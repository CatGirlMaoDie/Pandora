package org.gsn.pandora.skills.condition;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测全局记分板固定 entry 的分数范围。objective|o=...  value|v=min  maxValue=max */
public class GlobalScoreCondition implements Condition {

    private final String objective;
    private final String entry;
    private final int min, max;

    public GlobalScoreCondition(String objective, String entry, int min, int max) {
        this.objective = objective == null ? "" : objective;
        this.entry = entry == null ? "dummy" : entry;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (objective.isEmpty()) return false;
        var board = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective obj = board.getObjective(objective);
        if (obj == null) return false;
        Score score = obj.getScore(entry);
        if (!score.isScoreSet()) return false;
        int v = score.getScore();
        return v >= min && v <= max;
    }
}
