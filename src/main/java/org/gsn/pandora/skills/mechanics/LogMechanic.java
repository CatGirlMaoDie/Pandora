package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.logging.Level;

/**
 * 向控制台输出日志。
 * message|m=...  level|l=INFO (INFO/WARNING/SEVERE)
 */
public class LogMechanic implements Mechanic {

    private final String message;
    private final Level level;

    public LogMechanic(String message, String levelName) {
        this.message = message == null ? "" : message;
        Level l = Level.INFO;
        if (levelName != null) {
            switch (levelName.toUpperCase(java.util.Locale.ROOT)) {
                case "WARNING": case "WARN": l = Level.WARNING; break;
                case "SEVERE": case "ERROR": l = Level.SEVERE; break;
            }
        }
        this.level = l;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (message.isEmpty()) return false;
        context.getPlugin().getLogger().log(level, "[Pandora] " + message);
        return true;
    }
}
