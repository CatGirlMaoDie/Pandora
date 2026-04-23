package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 重置目标玩家的个人天气/时间为世界默认。
 */
public class ResetWeatherMechanic implements Mechanic {

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player)) continue;
            ((Player) target).resetPlayerWeather();
            any = true;
        }
        return any;
    }
}
