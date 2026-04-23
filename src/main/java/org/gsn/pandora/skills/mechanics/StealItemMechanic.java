package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 从目标玩家背包中偷取主手物品给施法者。
 */
public class StealItemMechanic implements Mechanic {

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        LivingEntity caster = context.getCaster();
        if (!(caster instanceof Player thief) || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player victim)) continue;
            ItemStack held = victim.getInventory().getItemInMainHand();
            if (held.getType().isAir()) continue;
            ItemStack stolen = held.clone();
            victim.getInventory().setItemInMainHand(null);
            thief.getInventory().addItem(stolen);
            any = true;
        }
        return any;
    }
}
