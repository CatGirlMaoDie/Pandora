package org.gsn.pandora.skills.mechanics;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建/更新/移除 Boss 血条。
 * name|n=infobar  display|d=...  value|v=1.0  color|c=RED  style|s=SOLID
 */
public class BossBarMechanic implements Mechanic {

    public enum Mode { CREATE, SET, REMOVE }

    private static final Map<String, BossBar> BARS = new ConcurrentHashMap<>();

    private final Mode mode;
    private final String name;
    private final String display;
    private final double value;
    private final BarColor color;
    private final BarStyle style;

    public BossBarMechanic(Mode mode, String name, String display,
                           double value, String colorName, String styleName) {
        this.mode = mode;
        this.name = name == null ? "infobar" : name;
        this.display = display == null ? "" : display;
        this.value = Math.max(0.0, Math.min(1.0, value));
        BarColor c = BarColor.RED;
        try { c = BarColor.valueOf(colorName == null ? "RED" : colorName.toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException ignored) {}
        this.color = c;
        BarStyle s = BarStyle.SOLID;
        try { s = BarStyle.valueOf(styleName == null ? "SOLID" : styleName.toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException ignored) {}
        this.style = s;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player p)) continue;
            switch (mode) {
                case REMOVE -> {
                    BossBar bar = BARS.remove(name + ":" + p.getUniqueId());
                    if (bar != null) { bar.removePlayer(p); bar.setVisible(false); }
                }
                case CREATE -> {
                    String key = name + ":" + p.getUniqueId();
                    BossBar bar = BARS.computeIfAbsent(key, k ->
                        context.getPlugin().getServer().createBossBar(
                            org.bukkit.ChatColor.translateAlternateColorCodes('&', display),
                            color, style));
                    bar.setTitle(org.bukkit.ChatColor.translateAlternateColorCodes('&', display));
                    bar.setProgress(value);
                    bar.setColor(color);
                    bar.setStyle(style);
                    bar.addPlayer(p);
                    bar.setVisible(true);
                }
                case SET -> {
                    BossBar bar = BARS.get(name + ":" + p.getUniqueId());
                    if (bar != null) {
                        if (!display.isEmpty()) bar.setTitle(org.bukkit.ChatColor.translateAlternateColorCodes('&', display));
                        bar.setProgress(value);
                        bar.setColor(color);
                        bar.setStyle(style);
                    }
                }
            }
            any = true;
        }
        return any;
    }
}
