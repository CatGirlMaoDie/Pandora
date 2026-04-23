package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 向目标玩家发送 ActionBar 消息。
 * message|msg|m=...
 */
public class ActionBarMechanic implements Mechanic {

    private final String message;

    public ActionBarMechanic(String message) {
        this.message = message == null ? "" : org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (message.isEmpty() || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player) || target.isDead()) continue;
            ((Player) target).sendActionBar(message);
            any = true;
        }
        return any;
    }
}
