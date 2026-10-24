package org.gsn.pandora.mobs;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 威胁表（对应 Wiki「威胁度」）：
 * 记录每个 Pandora 实体被各玩家累计造成的伤害，开启威胁表后
 * 实体会锁定累计伤害最高的玩家为目标，避免玩家车轮战。
 */
public class ThreatTableManager {

    /** mob UUID -> (attacker UUID -> threat value) */
    private final Map<UUID, Map<UUID, Double>> tables = new ConcurrentHashMap<>();

    public void addThreat(LivingEntity mob, LivingEntity attacker, double amount) {
        if (mob == null || attacker == null || amount == 0.0) return;
        tables.computeIfAbsent(mob.getUniqueId(), k -> new ConcurrentHashMap<>())
            .merge(attacker.getUniqueId(), amount, Double::sum);
    }

    /** 按模式修改威胁值：ADD / SET / MULTIPLY */
    public void modifyThreat(LivingEntity mob, LivingEntity attacker, double amount, String mode) {
        if (mob == null || attacker == null) return;
        Map<UUID, Double> table = tables.computeIfAbsent(mob.getUniqueId(), k -> new ConcurrentHashMap<>());
        String m = mode == null ? "ADD" : mode.toUpperCase(java.util.Locale.ROOT);
        switch (m) {
            case "SET" -> table.put(attacker.getUniqueId(), amount);
            case "MULT", "MULTIPLY" -> table.merge(attacker.getUniqueId(), amount, (oldV, mul) -> oldV * mul);
            default -> table.merge(attacker.getUniqueId(), amount, Double::sum);
        }
    }

    public void clear(LivingEntity mob) {
        if (mob != null) tables.remove(mob.getUniqueId());
    }

    public double getThreat(LivingEntity mob, LivingEntity attacker) {
        if (mob == null || attacker == null) return 0.0;
        Map<UUID, Double> table = tables.get(mob.getUniqueId());
        return table == null ? 0.0 : table.getOrDefault(attacker.getUniqueId(), 0.0);
    }

    /** @return 威胁最高的有效玩家，若无则返回 null */
    public Player getTopThreat(LivingEntity mob) {
        if (mob == null) return null;
        Map<UUID, Double> table = tables.get(mob.getUniqueId());
        if (table == null || table.isEmpty()) return null;
        UUID top = null;
        double topVal = 0.0;
        for (Map.Entry<UUID, Double> e : table.entrySet()) {
            if (e.getValue() > topVal) { topVal = e.getValue(); top = e.getKey(); }
        }
        if (top == null) return null;
        return Bukkit.getPlayer(top);
    }

    /** @return 威胁表中距离 mob 在指定范围内的有效玩家数量 */
    public int countThreatWithin(LivingEntity mob, double distance) {
        if (mob == null) return 0;
        Map<UUID, Double> table = tables.get(mob.getUniqueId());
        if (table == null || table.isEmpty()) return 0;
        double sq = distance * distance;
        int count = 0;
        for (UUID id : table.keySet()) {
            Player p = Bukkit.getPlayer(id);
            if (p == null || !p.isOnline() || p.isDead()) continue;
            if (!p.getWorld().equals(mob.getWorld())) continue;
            if (p.getLocation().distanceSquared(mob.getLocation()) <= sq) count++;
        }
        return count;
    }

    /** 清理失效条目（离线/死亡玩家）以及空表。 */
    public void cleanup() {
        Iterator<Map.Entry<UUID, Map<UUID, Double>>> it = tables.entrySet().iterator();
        while (it.hasNext()) {
            Map<UUID, Double> table = it.next().getValue();
            table.keySet().removeIf(id -> {
                Player p = Bukkit.getPlayer(id);
                return p == null || !p.isOnline() || p.isDead();
            });
            if (table.isEmpty()) it.remove();
        }
    }

    public void removeAll() { tables.clear(); }
}
