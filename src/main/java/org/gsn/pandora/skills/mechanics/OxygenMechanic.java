package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 给/扣目标玩家氧气值（水下呼吸刻）。
 * amount|a=0  正值增加，负值减少
 */
public class OxygenMechanic implements Mechanic {

    private final int amount;

    public OxygenMechanic(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player) || target.isDead()) continue;
            Player p = (Player) target;
            p.setRemainingAir(Math.max(0, Math.min(p.getMaximumAir(), p.getRemainingAir() + amount)));
            any = true;
        }
        return any;
    }
}
