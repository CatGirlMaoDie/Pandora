package org.gsn.pandora.skills.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 修改记分板分数。
 * objective|obj|o=...  entry|e|name|n=dummy  action|a=SET  value|v=0
 * action: SET / ADD / SUBTRACT / MULTIPLY / DIVIDE / MOD / RESET
 */
public class ModifyScoreMechanic implements Mechanic {

    public enum Action { SET, ADD, SUBTRACT, MULTIPLY, DIVIDE, MOD, RESET }

    private final String objective;
    private final String entry;
    private final Action action;
    private final int value;

    public ModifyScoreMechanic(String objective, String entry, String actionName, int value) {
        this.objective = objective == null ? "" : objective;
        this.entry = entry == null ? "dummy" : entry;
        Action a = Action.SET;
        try { a = Action.valueOf(actionName.toUpperCase()); } catch (Exception ignored) {}
        this.action = a;
        this.value = value;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (objective.isEmpty()) return false;
        var board = Bukkit.getScoreboardManager().getMainScoreboard();
        var obj = board.getObjective(objective);
        if (obj == null) return false;

        // 解析 entry：支持 <caster> / <target> 占位符
        String resolvedEntry = resolveEntry(entry, context);

        var score = obj.getScore(resolvedEntry);
        int current = score.isScoreSet() ? score.getScore() : 0;

        if (action == Action.RESET) {
            board.resetScores(resolvedEntry);
            return true;
        }

        int newValue = switch (action) {
            case SET -> value;
            case ADD -> current + value;
            case SUBTRACT -> current - value;
            case MULTIPLY -> current * value;
            case DIVIDE -> value != 0 ? current / value : current;
            case MOD -> value != 0 ? current % value : current;
            default -> value;
        };
        score.setScore(newValue);
        return true;
    }

    private String resolveEntry(String raw, SkillExecutionContext ctx) {
        if (raw.equalsIgnoreCase("<caster>") && ctx.getCaster() instanceof Player p) return p.getName();
        if (raw.equalsIgnoreCase("<target>") && ctx.getTarget() instanceof Player p) return p.getName();
        return raw;
    }
}
