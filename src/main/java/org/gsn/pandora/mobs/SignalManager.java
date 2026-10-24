package org.gsn.pandora.mobs;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实体间信号（对应 MythicMobs 的 sendsignal / ~onSignal）。
 * 记录每个实体最近一次收到的信号名，收到信号时由监听器触发 onSignal 技能。
 */
public class SignalManager {

    private final Map<UUID, String> lastSignals = new ConcurrentHashMap<>();

    public void setLastSignal(UUID entityId, String signal) {
        if (entityId == null) return;
        if (signal == null) lastSignals.remove(entityId);
        else lastSignals.put(entityId, signal);
    }

    public String getLastSignal(UUID entityId) {
        return entityId == null ? null : lastSignals.get(entityId);
    }

    public void remove(UUID entityId) {
        if (entityId != null) lastSignals.remove(entityId);
    }
}
