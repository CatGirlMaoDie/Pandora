package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 设置目标玩家的全局冷却（攻击冷却）。
 * ticks|t=20
 */
public class GcdMechanic implements Mechanic {

    private final int ticks;

    public GcdMechanic(int ticks) {
        this.ticks = Math.max(0, ticks);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player p) || target.isDead()) continue;
            p.setCooldown(p.getInventory().getItemInMainHand().getType(), ticks);
            any = true;
        }
        return any;
    }
}
