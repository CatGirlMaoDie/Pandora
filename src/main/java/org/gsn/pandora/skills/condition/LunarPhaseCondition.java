package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 检测目标所在世界的月相（0-7）。
 * phases|phase|p=0,1,2  0=满月 4=新月
 */
public class LunarPhaseCondition implements Condition {
    private final Set<Long> phases;

    public LunarPhaseCondition(String phaseList) {
        this.phases = Arrays.stream(phaseList.split(","))
            .map(s -> {
                try { return Long.parseLong(s.trim()); }
                catch (NumberFormatException e) { return null; }
            })
            .filter(p -> p != null && p >= 0 && p <= 7)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null || phases.isEmpty()) return false;
        // Minecraft 月相：每天 24000 tick，8 个月相循环
        long day = target.getWorld().getFullTime() / 24000L;
        long phase = day % 8;
        return phases.contains(phase);
    }
}
