package org.gsn.pandora.skills.mechanics;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TextDisplay;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 设置 TextDisplay 实体的文字内容。text|t=...
 */
public class SetTextDisplayMechanic implements Mechanic {

    private final String text;

    public SetTextDisplayMechanic(String text) {
        this.text = text == null ? "" : ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof TextDisplay td)) continue;
            td.setText(text);
            any = true;
        }
        return any;
    }
}
