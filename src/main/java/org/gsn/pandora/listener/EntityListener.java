package org.gsn.pandora.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
        if (!(event.getEntity() instanceof LivingEntity living)) return;
        if (!mobManager.isPandoraMob(living)) return;

        triggerSkills(living, null, TriggerType.ON_SPAWN);

        MobType mobType = mobManager.getMobTypeByEntity(living);
        if (mobType == null || mobType.getTimerIntervalTicks() <= 0) return;

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
            triggerSkills(victim, damager, TriggerType.ON_HURT);
        }

        if (event.getDamager() instanceof LivingEntity attacker && mobManager.isPandoraMob(attacker)) {
            triggerSkills(attacker, victim, TriggerType.ON_ATTACK);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (mobManager.isPandoraMob(entity)) {
            triggerSkills(entity, null, TriggerType.ON_DEATH);
            cancelTimer(entity.getUniqueId());

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
