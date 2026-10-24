package org.gsn.pandora.api;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.gsn.pandora.mobs.MobType;

/**
 * Pandora 自定义实体从世界中消失（死亡以外的移除）时触发
 * （对应 MythicMobs 的 MythicMobDespawnEvent）。
 */
public class PandoraMobDespawnEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final LivingEntity entity;
    private final MobType mobType;

    public PandoraMobDespawnEvent(LivingEntity entity, MobType mobType) {
        this.entity = entity;
        this.mobType = mobType;
    }

    public LivingEntity getEntity() { return entity; }
    public MobType getMobType() { return mobType; }

    @Override
    public HandlerList getHandlers() { return HANDLERS; }

    public static HandlerList getHandlerList() { return HANDLERS; }
}
