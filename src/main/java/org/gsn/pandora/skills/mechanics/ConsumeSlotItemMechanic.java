package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 消耗目标玩家指定槽位的物品。
 * slot|s=HAND  amount|a=1
 * slot 支持: HAND, OFF_HAND, HEAD, CHEST, LEGS, FEET
 */
public class ConsumeSlotItemMechanic implements Mechanic {

    private final String slot;
    private final int amount;

    public ConsumeSlotItemMechanic(String slot, int amount) {
        this.slot = slot == null ? "HAND" : slot.toUpperCase(Locale.ROOT);
        this.amount = Math.max(1, amount);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player)) continue;
            Player p = (Player) target;
            ItemStack item = switch (slot) {
                case "OFF_HAND", "OFFHAND" -> p.getInventory().getItemInOffHand();
                case "HEAD", "HELMET" -> p.getInventory().getHelmet();
                case "CHEST", "CHESTPLATE" -> p.getInventory().getChestplate();
                case "LEGS", "LEGGINGS" -> p.getInventory().getLeggings();
                case "FEET", "BOOTS" -> p.getInventory().getBoots();
                default -> p.getInventory().getItemInMainHand();
            };
            if (item == null || item.getType().isAir()) continue;
            int newAmount = item.getAmount() - amount;
            if (newAmount <= 0) {
                switch (slot) {
                    case "OFF_HAND", "OFFHAND" -> p.getInventory().setItemInOffHand(null);
                    case "HEAD", "HELMET" -> p.getInventory().setHelmet(null);
                    case "CHEST", "CHESTPLATE" -> p.getInventory().setChestplate(null);
                    case "LEGS", "LEGGINGS" -> p.getInventory().setLeggings(null);
                    case "FEET", "BOOTS" -> p.getInventory().setBoots(null);
                    default -> p.getInventory().setItemInMainHand(null);
                }
            } else {
                item.setAmount(newAmount);
            }
            any = true;
        }
        return any;
    }
}
