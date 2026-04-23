package org.gsn.pandora.skills.mechanics;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 在目标位置显示悬浮文字（通过隐形盔甲架实现）。
 * text|t=...  stayTime|time|stay=100（tick）  yOffset|yo|y=0
 */
public class HologramMechanic implements Mechanic {

    private final String text;
    private final int stayTicks;
    private final double yOffset;

    public HologramMechanic(String text, int stayTicks, double yOffset) {
        this.text = text == null ? "" : ChatColor.translateAlternateColorCodes('&', text);
        this.stayTicks = Math.max(1, stayTicks);
        this.yOffset = yOffset;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (text.isEmpty() || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null) continue;
            Location loc = target.getLocation().clone().add(0, yOffset, 0);
            if (loc.getWorld() == null) continue;
            ArmorStand stand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setCustomName(text);
            stand.setCustomNameVisible(true);
            stand.setSmall(true);
            stand.setMarker(true);
            org.gsn.pandora.util.FoliaScheduler.runEntityTaskLater(
                context.getPlugin(), stand, stand::remove, stayTicks);
            any = true;
        }
        return any;
    }
}
