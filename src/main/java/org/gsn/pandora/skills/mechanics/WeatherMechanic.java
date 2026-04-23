package org.gsn.pandora.skills.mechanics;

import org.bukkit.WeatherType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 改变天气（对玩家目标设置个人天气，或对世界设置全局天气）。
 * type|t=CLEAR|DOWNFALL  duration|d=6000  personal=true
 */
public class WeatherMechanic implements Mechanic {

    private final WeatherType weatherType;
    private final int durationTicks;
    private final boolean personal;

    public WeatherMechanic(String typeName, int durationTicks, boolean personal) {
        WeatherType wt = WeatherType.CLEAR;
        if (typeName != null) {
            switch (typeName.toUpperCase(Locale.ROOT)) {
                case "RAIN": case "DOWNFALL": case "STORM": wt = WeatherType.DOWNFALL; break;
            }
        }
        this.weatherType = wt;
        this.durationTicks = Math.max(0, durationTicks);
        this.personal = personal;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null) continue;
            if (personal && target instanceof Player) {
                ((Player) target).setPlayerWeather(weatherType);
                any = true;
            } else if (!personal && target.getWorld() != null) {
                boolean storm = weatherType == WeatherType.DOWNFALL;
                target.getWorld().setStorm(storm);
                if (durationTicks > 0) target.getWorld().setWeatherDuration(durationTicks);
                any = true;
            }
        }
        return any;
    }
}
