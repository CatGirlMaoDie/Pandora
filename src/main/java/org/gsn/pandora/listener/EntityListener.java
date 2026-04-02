package org.gsn.pandora.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.Pandora;
import org.gsn.pandora.mobs.MobManager;
import org.gsn.pandora.mobs.MobType;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.skills.SkillManager;
import org.gsn.pandora.skills.TriggerType;

import java.util.List;

public class EntityListener implements Listener {

    private final Pandora plugin;
    private final MobManager mobManager;
    private final SkillManager skillManager;

    public EntityListener(Pandora plugin, MobManager mobManager, SkillManager skillManager) {
        this.plugin = plugin;
        this.mobManager = mobManager;
        this.skillManager = skillManager;
    }

    private void triggerSkills(LivingEntity caster, LivingEntity target, TriggerType triggerType) {
        if (caster == null || triggerType == null) {
            return;
        }
        if (!mobManager.isPandoraMob(caster)) {
            return;
        }
        MobType mobType = mobManager.getMobTypeByEntity(caster);
        if (mobType == null) {
            return;
        }
        List<String> skills = mobType.getSkillsForTrigger(triggerType);
        if (skills.isEmpty()) {
            return;
        }
        SkillExecutionContext ctx = new SkillExecutionContext(plugin, caster, target, triggerType, caster.getLocation());
        for (String skillName : skills) {
            skillManager.castSkill(skillName, ctx);
        }
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (!mobManager.isPandoraMob(entity)) {
            return;
        }

        triggerSkills(entity, null, TriggerType.ON_SPAWN);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity victim = (LivingEntity) event.getEntity();

        // Pandora 怪被打
        if (mobManager.isPandoraMob(victim)) {
            LivingEntity damager = event.getDamager() instanceof LivingEntity ? (LivingEntity) event.getDamager() : null;
            triggerSkills(victim, damager, TriggerType.ON_HURT);
        }

        // Pandora 怪作为攻击者
        if (event.getDamager() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getDamager();
            if (mobManager.isPandoraMob(attacker)) {
                triggerSkills(attacker, victim, TriggerType.ON_ATTACK);
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        // Pandora 怪物自身死亡触发
        if (mobManager.isPandoraMob(entity)) {
            triggerSkills(entity, null, TriggerType.ON_DEATH);

            MobType mobType = mobManager.getMobTypeByEntity(entity);
            if (mobType != null && mobType.getOptions() != null
                && Boolean.TRUE.equals(mobType.getOptions().getPreventOtherDrops())) {
                event.getDrops().clear();
                event.setDroppedExp(0);
            }
        }

        // Pandora 怪物击杀其他实体触发 ON_KILL
        if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();
            if (damageEvent.getDamager() instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity) damageEvent.getDamager();
                if (mobManager.isPandoraMob(attacker)) {
                    triggerSkills(attacker, entity, TriggerType.ON_KILL);
                }
            }
        }
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (!mobManager.isPandoraMob(entity)) {
            return;
        }
        LivingEntity target = event.getTarget();
        triggerSkills(entity, target, TriggerType.ON_COMBAT);
    }
}
