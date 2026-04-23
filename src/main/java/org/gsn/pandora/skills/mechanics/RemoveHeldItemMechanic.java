package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 移除目标玩家主手持有的物品（指定数量）。
 * amount|a=1
 */
public class RemoveHeldItemMechanic implements Mechanic {

    private final int amount;

    public RemoveHeldItemMechanic(int amount) {
        this.amount = Math.max(1, amount);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player)) continue;
            Player p = (Player) target;
            var item = p.getInventory().getItemInMainHand();
            if (item.getType().isAir()) continue;
            int newAmt = item.getAmount() - amount;
            if (newAmt <= 0) p.getInventory().setItemInMainHand(null);
            else item.setAmount(newAmt);
            any = true;
        }
        return any;
    }
}
