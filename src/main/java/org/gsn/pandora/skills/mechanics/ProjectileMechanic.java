package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 向目标方向发射抛射物，命中时对目标造成伤害。
 * velocity|v=5  maxRange|mr=40  gravity|g=0
 * horizontalRadius|hradius|hr|r=1.25  verticalRadius|vradius|vr=1.25
 * hitPlayers|hp=true  hitNonPlayers|hnp=false  hitSelf=false
 * startYOffset|syo=1  startForwardOffset|sfo=1
 * onHitSkill|onhit|oh=<skillName>（命中时触发技能）
 * type=NORMAL（NORMAL/ARROW/SNOWBALL）
 */
public class ProjectileMechanic implements Mechanic {

    public enum ProjectileType { NORMAL, ARROW, SNOWBALL }

    private final ProjectileType type;
    private final double velocity;
    private final double maxRange;
    private final double gravity;
    private final double hitRadius;
    private final boolean hitPlayers;
    private final boolean hitNonPlayers;
    private final boolean hitSelf;
    private final double startYOffset;
    private final String onHitSkill;

    public ProjectileMechanic(String typeName, double velocity, double maxRange, double gravity,
                              double hitRadius, boolean hitPlayers, boolean hitNonPlayers,
                              boolean hitSelf, double startYOffset, String onHitSkill) {
        ProjectileType t = ProjectileType.NORMAL;
        if (typeName != null) {
            try { t = ProjectileType.valueOf(typeName.toUpperCase(Locale.ROOT)); }
            catch (IllegalArgumentException ignored) {}
        }
        this.type = t;
        this.velocity = Math.max(0.1, velocity);
        this.maxRange = Math.max(1, maxRange);
        this.gravity = gravity;
        this.hitRadius = Math.max(0.1, hitRadius);
        this.hitPlayers = hitPlayers;
        this.hitNonPlayers = hitNonPlayers;
        this.hitSelf = hitSelf;
        this.startYOffset = startYOffset;
        this.onHitSkill = onHitSkill;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (caster == null || targets == null || targets.isEmpty()) return false;
        LivingEntity dest = targets.get(0);
        if (dest == null) return false;

        Location origin = caster.getEyeLocation().clone().add(0, startYOffset - 1.62, 0);
        if (origin.getWorld() == null) return false;

        Vector dir = dest.getLocation().add(0, dest.getHeight() / 2, 0)
            .toVector().subtract(origin.toVector()).normalize();

        // 发射抛射物
        org.bukkit.entity.Projectile proj = switch (type) {
            case ARROW -> origin.getWorld().spawnArrow(origin, dir, (float) velocity, 0f);
            case SNOWBALL -> {
                Snowball sb = origin.getWorld().spawn(origin, Snowball.class);
                sb.setVelocity(dir.multiply(velocity));
                yield sb;
            }
            default -> {
                Arrow arrow = origin.getWorld().spawnArrow(origin, dir, (float) velocity, 0f);
                arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
                yield arrow;
            }
        };
        proj.setShooter(caster);
        proj.setGravity(gravity != 0);

        // 简单碰撞检测：通过 scheduler 每 tick 检查
        final double maxRangeSq = maxRange * maxRange;
        final Location startLoc = origin.clone();
        final String hitSkill = onHitSkill;
        final org.gsn.pandora.skills.SkillManager skillManager =
            context.getPlugin() instanceof org.gsn.pandora.Pandora pandora
                ? pandora.getSkillManager() : null;

        org.gsn.pandora.util.FoliaScheduler.runEntityTaskTimer(
            context.getPlugin(), proj,
            pt -> {
                if (!proj.isValid() || proj.isDead()) { pt.cancel(); return; }
                if (proj.getLocation().distanceSquared(startLoc) > maxRangeSq) {
                    proj.remove(); pt.cancel(); return;
                }
                // 检测命中
                for (var entity : proj.getWorld().getNearbyEntities(proj.getLocation(), hitRadius, hitRadius, hitRadius)) {
                    if (!(entity instanceof LivingEntity le)) continue;
                    if (!hitSelf && le.equals(caster)) continue;
                    if (!hitPlayers && entity instanceof org.bukkit.entity.Player) continue;
                    if (!hitNonPlayers && !(entity instanceof org.bukkit.entity.Player)) continue;
                    if (le.equals(caster)) continue;
                    le.damage(1.0, caster);
                    if (hitSkill != null && !hitSkill.isEmpty() && skillManager != null) {
                        var hitCtx = new org.gsn.pandora.skills.SkillExecutionContext(
                            context.getPlugin(), caster, le,
                            context.getTriggerType(), proj.getLocation());
                        skillManager.castSkill(hitSkill, hitCtx);
                    }
                    proj.remove(); pt.cancel(); return;
                }
            }, 1L, 1L);

        return true;
    }
}
