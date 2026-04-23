package org.gsn.pandora.skills.mechanics;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.meta.FireworkMeta;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 在目标位置发射烟花。
 * type|t=BALL  power|p|duration|d=2  flicker|f=false  trail|tr=false
 * colors|color|c=#FFFFFF  fadedColors|fcolors|fc=#FFFFFF
 */
public class FireworkMechanic implements Mechanic {

    private final FireworkEffect.Type type;
    private final int power;
    private final boolean flicker;
    private final boolean trail;
    private final Color color;
    private final Color fadeColor;

    public FireworkMechanic(String typeName, int power, boolean flicker, boolean trail,
                            String colorHex, String fadeColorHex) {
        FireworkEffect.Type t = FireworkEffect.Type.BALL;
        try { t = FireworkEffect.Type.valueOf(typeName.toUpperCase()); } catch (Exception ignored) {}
        this.type = t;
        this.power = Math.max(0, Math.min(127, power));
        this.flicker = flicker;
        this.trail = trail;
        this.color = parseColor(colorHex, Color.WHITE);
        this.fadeColor = parseColor(fadeColorHex, Color.WHITE);
    }

    private Color parseColor(String hex, Color def) {
        if (hex == null || hex.isEmpty()) return def;
        try {
            String clean = hex.startsWith("#") ? hex.substring(1) : hex;
            int rgb = Integer.parseInt(clean, 16);
            return Color.fromRGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
        } catch (Exception e) {
            return def;
        }
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null) continue;
            Location loc = target.getLocation();
            if (loc.getWorld() == null) continue;
            Firework fw = loc.getWorld().spawn(loc, Firework.class);
            FireworkMeta meta = fw.getFireworkMeta();
            meta.setPower(power);
            meta.addEffect(FireworkEffect.builder()
                .with(type)
                .flicker(flicker)
                .trail(trail)
                .withColor(color)
                .withFade(fadeColor)
                .build());
            fw.setFireworkMeta(meta);
            any = true;
        }
        return any;
    }
}
