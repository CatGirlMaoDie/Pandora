package org.gsn.pandora.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.Pandora;
import org.gsn.pandora.mobs.MobDrop;
import org.gsn.pandora.mobs.MobManager;
import org.gsn.pandora.mobs.MobType;
import org.gsn.pandora.script.ScriptingService;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.skills.SkillManager;
import org.gsn.pandora.skills.TriggerType;
import org.gsn.pandora.util.FoliaScheduler;
import org.gsn.pandora.util.PandoraTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EntityListener implements Listener {

    private final Pandora plugin;
    private final MobManager mobManager;
    private final SkillManager skillManager;

    /** 记录每个实体的 ON_TIMER 任务，实体死亡或失效时取消。 */
    private final Map<UUID, PandoraTask> timerTasks = new HashMap<>();

    public EntityListener(Pandora plugin, MobManager mobManager, SkillManager skillManager) {
        this.plugin = plugin;
        this.mobManager = mobManager;
        this.skillManager = skillManager;
        FoliaScheduler.runGlobalTaskTimer(plugin, this::cleanupStaleTimers, 200L, 200L);
    }

    private void triggerSkills(LivingEntity caster, LivingEntity target, TriggerType triggerType) {
        if (caster == null || triggerType == null) return;
        if (!mobManager.isPandoraMob(caster)) return;
        MobType mobType = mobManager.getMobTypeByEntity(caster);
        if (mobType == null) return;

        List<String> skills = resolveSkills(caster, target, triggerType, mobType);
        if (skills.isEmpty()) return;

        SkillExecutionContext ctx = new SkillExecutionContext(plugin, caster, target, triggerType, caster.getLocation());
        for (String skillName : skills) {
            skillManager.castSkill(skillName, ctx);
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> resolveSkills(LivingEntity caster, LivingEntity target,
                                       TriggerType triggerType, MobType mobType) {
        if (mobType.hasAiScript()) {
            ScriptingService scriptingService = plugin.getScriptingService();
            if (scriptingService != null && scriptingService.isEnabled()) {
                Map<String, Object> bindings = new HashMap<>(4);
                bindings.put("entity", caster);
                bindings.put("target", target);
                bindings.put("trigger", triggerType.name());
                bindings.put("mobType", mobType);
                Object result = scriptingService.call(
                    "mobs/" + mobType.getAiScript(), mobType.getAiFunction(), bindings);
                if (result instanceof List<?> raw && !raw.isEmpty()) {
                    List<String> skills = new ArrayList<>(raw.size());
                    for (Object o : raw) {
                        if (o != null) skills.add(o.toString());
                    }
                    if (!skills.isEmpty()) return skills;
                }
            }
        }
        return mobType.getSkillsForTrigger(triggerType);
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity living))
            return;
        if (!mobManager.isPandoraMob(living))
            return;

        triggerSkills(living, null, TriggerType.ON_SPAWN);

        MobType mobType = mobManager.getMobTypeByEntity(living);
        if (mobType == null || mobType.getTimerIntervalTicks() <= 0)
            return;

        long interval = mobType.getTimerIntervalTicks();
        UUID uid = living.getUniqueId();
        PandoraTask task = FoliaScheduler.runEntityTaskTimer(plugin, living, pt -> {
            if (!living.isValid() || living.isDead()) {
                cancelTimer(uid);
                return;
            }
            triggerSkills(living, null, TriggerType.ON_TIMER);
        }, interval, interval);
        timerTasks.put(uid, task);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity victim)) return;

        if (mobManager.isPandoraMob(victim)) {
            LivingEntity damager = event.getDamager() instanceof LivingEntity le ? le : null;
            // 找到实际攻击者（投射物归主）
            if (damager == null && event.getDamager() instanceof org.bukkit.entity.Projectile proj
                    && proj.getShooter() instanceof LivingEntity shooter) {
                damager = shooter;
            }
            triggerSkills(victim, damager, TriggerType.ON_HURT);

            // 威胁表累计伤害
            MobType mobType = mobManager.getMobTypeByEntity(victim);
            if (damager instanceof Player player && mobType != null && mobType.hasThreatTable()
                    && plugin.getThreatTableManager() != null) {
                plugin.getThreatTableManager().addThreat(victim, player, event.getFinalDamage());
            }

            // 伤害调整（对应 Wiki「伤害调整」）：按伤害原因应用倍率
            if (mobType != null) {
                double mult = mobType.getDamageModifier(event.getCause().name());
                if (mult != 1.0) {
                    event.setDamage(Math.max(0.0, event.getDamage() * mult));
                }
            }
        }

        if (event.getDamager() instanceof LivingEntity attacker && mobManager.isPandoraMob(attacker)) {
            triggerSkills(attacker, victim, TriggerType.ON_ATTACK);
        }
    }

    /** 所有伤害类型（含溺水/摔落等非实体伤害）的伤害调整。 */
    @EventHandler(ignoreCancelled = true)
    public void onGenericDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) return; // 已处理
        if (!(event.getEntity() instanceof LivingEntity victim)) return;
        MobType mobType = mobManager.getMobTypeByEntity(victim);
        if (mobType == null) return;
        double mult = mobType.getDamageModifier(event.getCause().name());
        if (mult != 1.0) {
            event.setDamage(Math.max(0.0, event.getDamage() * mult));
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (mobManager.isPandoraMob(entity)) {
            triggerSkills(entity, null, TriggerType.ON_DEATH);
            cancelTimer(entity.getUniqueId());
            if (plugin.getThreatTableManager() != null) plugin.getThreatTableManager().clear(entity);
            if (plugin.getSignalManager() != null) plugin.getSignalManager().remove(entity.getUniqueId());
            MobType deathType = mobManager.getMobTypeByEntity(entity);
            if (deathType != null) {
                try { new org.gsn.pandora.api.PandoraMobDeathEvent(entity, deathType).callEvent(); }
                catch (Throwable ignored) {}
            }

            MobType mobType = mobManager.getMobTypeByEntity(entity);
            if (mobType != null) {
                MobType.MobOptions opts = mobType.getOptions();
                boolean preventOther = opts != null && Boolean.TRUE.equals(opts.getPreventOtherDrops());

                if (!mobType.getDrops().isEmpty()) {
                    event.getDrops().clear();
                    event.setDroppedExp(0);
                    for (MobDrop drop : mobType.getDrops()) {
                        ItemStack item = drop.roll();
                        if (item != null) event.getDrops().add(item);
                    }
                } else if (preventOther) {
                    event.getDrops().clear();
                    event.setDroppedExp(0);
                }
            }
        }

        if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent dmgEvent
                && dmgEvent.getDamager() instanceof LivingEntity attacker
                && mobManager.isPandoraMob(attacker)) {
            triggerSkills(attacker, entity, TriggerType.ON_KILL);
        }
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (!mobManager.isPandoraMob(entity)) return;
        triggerSkills(entity, event.getTarget(), TriggerType.ON_COMBAT);

        // 威胁表：开启后锁定累计伤害最高的玩家为目标（对应 Wiki「威胁度」）
        MobType mobType = mobManager.getMobTypeByEntity(entity);
        if (mobType == null || !mobType.hasThreatTable()) return;
        var threatTable = plugin.getThreatTableManager();
        if (threatTable == null) return;
        Player topThreat = threatTable.getTopThreat(entity);
        if (topThreat != null && !topThreat.isDead()) {
            event.setTarget(topThreat);
            return;
        }

        // 阵营：同阵营实体不互相索敌（对应 Wiki「阵营」）
        if (event.getTarget() instanceof LivingEntity targetLiving
                && mobManager.isPandoraMob(targetLiving)) {
            MobType targetType = mobManager.getMobTypeByEntity(targetLiving);
            if (targetType != null && mobType.getFaction() != null
                    && mobType.getFaction().equalsIgnoreCase(targetType.getFaction())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityRemove(EntityRemoveEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (!mobManager.isPandoraMob(entity)) return;
        // 死亡已在 onDeath 处理并清除了 PDC 之前触发；这里只处理非死亡移除
        if (event.getCause() == EntityRemoveEvent.Cause.DEATH) return;
        cancelTimer(entity.getUniqueId());
        if (plugin.getThreatTableManager() != null) plugin.getThreatTableManager().clear(entity);
        if (plugin.getSignalManager() != null) plugin.getSignalManager().remove(entity.getUniqueId());
        MobType mobType = mobManager.getMobTypeByEntity(entity);
        if (mobType != null) {
            try { new org.gsn.pandora.api.PandoraMobDespawnEvent(entity, mobType).callEvent(); }
            catch (Throwable ignored) {}
        }
    }

    private void cancelTimer(UUID uuid) {
        PandoraTask task = timerTasks.remove(uuid);
        if (task != null) task.cancel();
    }

    /** 清理已失效实体（区块卸载/强制移除）的孤立计时任务。 */
    private void cleanupStaleTimers() {
        timerTasks.entrySet().removeIf(entry -> {
            org.bukkit.entity.Entity e = plugin.getServer().getEntity(entry.getKey());
            if (e == null || !e.isValid() || e.isDead()) {
                entry.getValue().cancel();
                return true;
            }
            return false;
        });
    }
}
