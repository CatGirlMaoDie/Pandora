package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 在地面上放置一个图腾式抛射物（贴地移动的盔甲架）。
 * velocity|v=5  maxRange|mr=40  hugSurface|hs=false
 * heightFromSurface|hfs=0.025  onHitSkill|onhit|oh=<skillName>
 * hitRadius|r=1.25  hitPlayers|hp=true  hitNonPlayers|hnp=false
 */
public class TotemMechanic implements Mechanic {

    private final double velocity;
    private final double maxRange;
    private final double hitRadius;
    private final boolean hitPlayers;
    private final boolean hitNonPlayers;
    private final String onHitSkill;

    public TotemMechanic(double velocity, double maxRange, double hitRadius,
                         boolean hitPlayers, boolean hitNonPlayers, String onHitSkill) {
        this.velocity = Math.max(0.1, velocity);
        this.maxRange = Math.max(1, maxRange);
        this.hitRadius = Math.max(0.1, hitRadius);
        this.hitPlayers = hitPlayers;
        this.hitNonPlayers = hitNonPlayers;
        this.onHitSkill = onHitSkill;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (caster == null || targets == null || targets.isEmpty()) return false;
        LivingEntity dest = targets.get(0);
        if (dest == null || dest.getWorld() == null) return false;

        Location origin = caster.getLocation().clone();
        if (origin.getWorld() == null) return false;

        // 生成隐形盔甲架作为图腾载体
        ArmorStand totem = (ArmorStand) origin.getWorld().spawnEntity(origin, EntityType.ARMOR_STAND);
        totem.setVisible(false);
        totem.setGravity(false);
        totem.setAI(false);
        totem.setSilent(true);
        totem.setMarker(true);

        org.bukkit.util.Vector dir = dest.getLocation().toVector()
            .subtract(origin.toVector());
        dir.setY(0);
        if (dir.lengthSquared() > 0) dir.normalize();
        dir.multiply(velocity / 20.0); // 每 tick 移动距离

        final double maxRangeSq = maxRange * maxRange;
        final Location startLoc = origin.clone();
        final org.gsn.pandora.skills.SkillManager skillManager =
            context.getPlugin() instanceof org.gsn.pandora.Pandora pandora
                ? pandora.getSkillManager() : null;

        org.gsn.pandora.util.FoliaScheduler.runEntityTaskTimer(
            context.getPlugin(), totem,
            pt -> {
                if (!totem.isValid() || totem.isDead()) { pt.cancel(); return; }
                if (totem.getLocation().distanceSquared(startLoc) > maxRangeSq) {
                    totem.remove(); pt.cancel(); return;
                }
                // 移动图腾
                Location newLoc = totem.getLocation().clone().add(dir);
                int y = totem.getWorld().getHighestBlockYAt(newLoc);
                newLoc.setY(y);
                totem.teleport(newLoc);

                // 检测命中
                for (var entity : totem.getWorld().getNearbyEntities(newLoc, hitRadius, hitRadius, hitRadius)) {
                    if (!(entity instanceof LivingEntity le)) continue;
                    if (le.equals(caster)) continue;
                    if (!hitPlayers && entity instanceof org.bukkit.entity.Player) continue;
                    if (!hitNonPlayers && !(entity instanceof org.bukkit.entity.Player)) continue;
                    le.damage(1.0, caster);
                    if (onHitSkill != null && !onHitSkill.isEmpty() && skillManager != null) {
                        var hitCtx = new org.gsn.pandora.skills.SkillExecutionContext(
                            context.getPlugin(), caster, le,
                            context.getTriggerType(), newLoc);
                        skillManager.castSkill(onHitSkill, hitCtx);
                    }
                    totem.remove(); pt.cancel(); return;
                }
            }, 1L, 1L);

        return true;
    }
}
