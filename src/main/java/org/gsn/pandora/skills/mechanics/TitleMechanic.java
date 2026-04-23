package org.gsn.pandora.skills.mechanics;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 向目标玩家发送 Title 标题。
 * title|t=...  subtitle|sub|s=...  fadeIn|fi=10  stay=70  fadeOut|fo=20
 */
public class TitleMechanic implements Mechanic {

    private final String title;
    private final String subtitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    public TitleMechanic(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title == null ? "" : org.bukkit.ChatColor.translateAlternateColorCodes('&', title);
        this.subtitle = subtitle == null ? "" : org.bukkit.ChatColor.translateAlternateColorCodes('&', subtitle);
        this.fadeIn = Math.max(0, fadeIn);
        this.stay = Math.max(0, stay);
        this.fadeOut = Math.max(0, fadeOut);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player) || target.isDead()) continue;
            ((Player) target).sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            any = true;
        }
        return any;
    }
}
