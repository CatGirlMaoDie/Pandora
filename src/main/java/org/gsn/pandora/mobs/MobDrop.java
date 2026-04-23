package org.gsn.pandora.mobs;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 代表一条掉落配置。
 * 格式：<material> [minAmount[-maxAmount]] [chance]
 * 例：minecraft:diamond 1-3 0.1
 */
public class MobDrop {

    private final Material material;
    private final int minAmount;
    private final int maxAmount;
    private final double chance; // 0-1

    public MobDrop(Material material, int minAmount, int maxAmount, double chance) {
        this.material = material;
        this.minAmount = Math.max(1, minAmount);
        this.maxAmount = Math.max(this.minAmount, maxAmount);
        this.chance = Math.max(0.0, Math.min(1.0, chance));
    }

    /**
     * 解析一行掉落配置字符串，失败返回 null。
     * 格式：<material> [minAmount[-maxAmount]] [chance]
     */
    public static MobDrop parse(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String[] parts = raw.trim().split("\\s+");
        if (parts.length == 0) return null;

        // 解析材质（支持 minecraft:diamond 和 DIAMOND 两种写法）
        String matName = parts[0].contains(":")
            ? parts[0].substring(parts[0].indexOf(':') + 1).toUpperCase(Locale.ROOT)
            : parts[0].toUpperCase(Locale.ROOT);
        Material material;
        try {
            material = Material.valueOf(matName);
        } catch (IllegalArgumentException e) {
            return null;
        }

        int minAmount = 1;
        int maxAmount = 1;
        double chance = 1.0;

        if (parts.length >= 2) {
            // 数量：支持 "1" 或 "1-3"
            String amountPart = parts[1];
            if (amountPart.contains("-")) {
                String[] range = amountPart.split("-", 2);
                try { minAmount = Integer.parseInt(range[0]); } catch (NumberFormatException ignored) {}
                try { maxAmount = Integer.parseInt(range[1]); } catch (NumberFormatException ignored) {}
            } else {
                try {
                    minAmount = Integer.parseInt(amountPart);
                    maxAmount = minAmount;
                } catch (NumberFormatException ignored) {}
            }
        }

        if (parts.length >= 3) {
            try { chance = Double.parseDouble(parts[2]); } catch (NumberFormatException ignored) {}
        }

        return new MobDrop(material, minAmount, maxAmount, chance);
    }

    /**
     * 根据概率决定是否掉落，并返回 ItemStack（不掉落时返回 null）。
     */
    public ItemStack roll() {
        if (ThreadLocalRandom.current().nextDouble() > chance) return null;
        int amount = minAmount == maxAmount
            ? minAmount
            : ThreadLocalRandom.current().nextInt(minAmount, maxAmount + 1);
        return new ItemStack(material, amount);
    }

    public Material getMaterial() { return material; }
    public int getMinAmount() { return minAmount; }
    public int getMaxAmount() { return maxAmount; }
    public double getChance() { return chance; }
}
