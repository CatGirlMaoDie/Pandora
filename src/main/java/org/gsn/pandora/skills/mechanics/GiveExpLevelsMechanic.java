package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 给/扣目标玩家经验等级。
 * amount|a=1  正值给予，负值扣除
 */
public class GiveExpLevelsMechanic implements Mechanic {

    private final int amount;

    public GiveExpLevelsMechanic(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player) || target.isDead()) continue;
            Player p = (Player) target;
            p.setLevel(Math.max(0, p.getLevel() + amount));
            any = true;
        }
        return any;
    }
}
