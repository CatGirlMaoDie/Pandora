package org.gsn.pandora.skills.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 修改全局（固定 entry）记分板分数。
 * objective|obj|o=...  entry|e=dummy  action|a=ADD  value|v=0
 */
public class ModifyGlobalScoreMechanic implements Mechanic {

    public enum Action { SET, ADD, SUBTRACT, MULTIPLY, DIVIDE, MOD, RESET }

    private final String objective;
    private final String entry;
    private final Action action;
    private final int value;

    public ModifyGlobalScoreMechanic(String objective, String entry, String actionName, int value) {
        this.objective = objective == null ? "" : objective;
        this.entry = entry == null ? "dummy" : entry;
        Action a = Action.ADD;
        try { a = Action.valueOf(actionName == null ? "ADD" : actionName.toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException ignored) {}
        this.action = a;
        this.value = value;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (objective.isEmpty()) return false;
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective obj = board.getObjective(objective);
        if (obj == null) return false;
        Score score = obj.getScore(entry);
        int current = score.isScoreSet() ? score.getScore() : 0;
        if (action == Action.RESET) { board.resetScores(entry); return true; }
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
        return true;
    }
}
