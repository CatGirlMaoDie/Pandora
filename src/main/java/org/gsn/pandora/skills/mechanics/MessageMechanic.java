package org.gsn.pandora.skills.mechanics;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

public class MessageMechanic implements Mechanic {

    private final String message;

    public MessageMechanic(String message) {
        this.message = ChatColor.translateAlternateColorCodes('&', message == null ? "" : message);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (message.isEmpty() || targets == null || targets.isEmpty()) {
            return false;
        }
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target instanceof Player && !target.isDead()) {
                ((Player) target).sendMessage(message);
                any = true;
            }
        }
        return any;
    }
}
