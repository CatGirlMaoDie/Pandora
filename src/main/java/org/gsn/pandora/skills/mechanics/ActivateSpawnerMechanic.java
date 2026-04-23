package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 激活 Pandora 刷怪点（通过 SpawnerManager 立即触发一次刷怪）。
 * spawner|spawners|s=<spawnerId>
 */
public class ActivateSpawnerMechanic implements Mechanic {

    private final String spawnerId;

    public ActivateSpawnerMechanic(String spawnerId) {
        this.spawnerId = spawnerId == null ? "" : spawnerId;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (spawnerId.isEmpty()) return false;
        var plugin = context.getPlugin();
        if (!(plugin instanceof org.gsn.pandora.Pandora pandora)) return false;
        var spawnerManager = pandora.getSpawnerManager();
        if (spawnerManager == null) return false;
        var def = spawnerManager.getSpawner(spawnerId);
        if (def == null) {
            plugin.getLogger().warning("[ActivateSpawner] 未找到刷怪点: " + spawnerId);
            return false;
        }
        // 通过反射或公开方法触发刷怪；SpawnerManager 已有 createSpawner，这里直接调用 spawn
        var mobManager = pandora.getMobManager();
        if (mobManager == null || def.getMobId() == null) return false;
        var loc = def.getLocation();
        if (loc == null) return false;
        mobManager.spawn(def.getMobId(), loc);
        return true;
    }
}
