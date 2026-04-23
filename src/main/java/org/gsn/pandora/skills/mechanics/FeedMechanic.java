package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 给目标玩家食物/饱食度。
 * amount|a=1  saturation|s=0  overFeed|of|o=false
 */
public class FeedMechanic implements Mechanic {

    private final int amount;
    private final float saturation;

    public FeedMechanic(int amount, float saturation) {
        this.amount = amount;
        this.saturation = saturation;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player) || target.isDead()) continue;
            Player p = (Player) target;
            p.setFoodLevel(Math.max(0, Math.min(20, p.getFoodLevel() + amount)));
            if (saturation != 0) {
                p.setSaturation(Math.max(0f, Math.min(p.getFoodLevel(), p.getSaturation() + saturation)));
            }
            any = true;
        }
        return any;
    }
}
