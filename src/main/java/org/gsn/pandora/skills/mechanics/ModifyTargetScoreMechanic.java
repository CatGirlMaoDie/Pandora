package org.gsn.pandora.skills.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 修改目标玩家的记分板分数。
 * objective|obj|o=...  action|a=SET  value|v=0
 */
public class ModifyTargetScoreMechanic implements Mechanic {

    public enum Action { SET, ADD, SUBTRACT, MULTIPLY, DIVIDE, MOD, RESET }

    private final String objective;
    private final Action action;
    private final int value;

    public ModifyTargetScoreMechanic(String objective, String actionName, int value) {
        this.objective = objective == null ? "" : objective;
        Action a = Action.SET;
        try { a = Action.valueOf(actionName == null ? "SET" : actionName.toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException ignored) {}
        this.action = a;
        this.value = value;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (objective.isEmpty() || targets == null) return false;
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective obj = board.getObjective(objective);
        if (obj == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null) continue;
            String entry = target instanceof Player ? target.getName() : target.getUniqueId().toString();
            Score score = obj.getScore(entry);
            int current = score.isScoreSet() ? score.getScore() : 0;
            if (action == Action.RESET) { board.resetScores(entry); any = true; continue; }
            int newVal = switch (action) {
                case SET -> value;
                case ADD -> current + value;
                case SUBTRACT -> current - value;
                case MULTIPLY -> current * value;
                case DIVIDE -> value != 0 ? current / value : current;
                case MOD -> value != 0 ? current % value : current;
                default -> value;
            };
            score.setScore(newVal);
            any = true;
        }
        return any;
    }
}
