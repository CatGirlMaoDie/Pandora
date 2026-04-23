package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 将施法者指定槽位的物品给予目标玩家。
 * slot|s=HAND  doFakeLooting|fl=false
 */
public class GiveItemFromSlotMechanic implements Mechanic {

    private final String slot;

    public GiveItemFromSlotMechanic(String slot) {
        this.slot = slot == null ? "HAND" : slot.toUpperCase(Locale.ROOT);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (!(caster instanceof Player giver) || targets == null) return false;

        ItemStack item = switch (slot) {
            case "OFF_HAND", "OFFHAND" -> giver.getInventory().getItemInOffHand();
            case "HEAD", "HELMET" -> giver.getInventory().getHelmet();
            case "CHEST", "CHESTPLATE" -> giver.getInventory().getChestplate();
            case "LEGS", "LEGGINGS" -> giver.getInventory().getLeggings();
            case "FEET", "BOOTS" -> giver.getInventory().getBoots();
            default -> giver.getInventory().getItemInMainHand();
        };

        if (item == null || item.getType().isAir()) return false;

        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player receiver) || target.isDead()) continue;
            receiver.getInventory().addItem(item.clone());
            any = true;
        }
        return any;
    }
}
