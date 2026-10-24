package org.gsn.pandora.api;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.gsn.pandora.mobs.MobType;

/**
 * Pandora 自定义实体死亡时触发（对应 MythicMobs 的 MythicMobDeathEvent）。
 */
public class PandoraMobDeathEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final LivingEntity entity;
    private final MobType mobType;

    public PandoraMobDeathEvent(LivingEntity entity, MobType mobType) {
        this.entity = entity;
        this.mobType = mobType;
    }

    public LivingEntity getEntity() { return entity; }
    public MobType getMobType() { return mobType; }

    @Override
    public HandlerList getHandlers() { return HANDLERS; }

    public static HandlerList getHandlerList() { return HANDLERS; }
}
