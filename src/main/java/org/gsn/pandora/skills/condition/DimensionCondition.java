package org.gsn.pandora.skills.condition;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测目标所在维度/环境。dimension|d|environment|env=NORMAL,NETHER,THE_END */
public class DimensionCondition implements Condition {
    private final Set<World.Environment> envs;

    public DimensionCondition(String list) {
        this.envs = Arrays.stream(list.split(","))
            .map(s -> {
                String name = s.trim().toUpperCase(Locale.ROOT);
                // 支持别名
                return switch (name) {
                    case "OVERWORLD", "NORMAL" -> World.Environment.NORMAL;
                    case "NETHER", "THE_NETHER" -> World.Environment.NETHER;
                    case "END", "THE_END" -> World.Environment.THE_END;
                    default -> {
                        try { yield World.Environment.valueOf(name); }
                        catch (IllegalArgumentException e) { yield null; }
                    }
                };
            })
            .filter(e -> e != null)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null || envs.isEmpty()) return false;
        return envs.contains(target.getWorld().getEnvironment());
    }
}
