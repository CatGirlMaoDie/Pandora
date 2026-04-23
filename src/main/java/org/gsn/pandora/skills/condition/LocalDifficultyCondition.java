package org.gsn.pandora.skills.condition;

import org.bukkit.Difficulty;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测目标所在世界的难度。difficulty|diff|d=EASY,NORMAL,HARD */
public class LocalDifficultyCondition implements Condition {

    private final Set<Difficulty> difficulties;

    public LocalDifficultyCondition(String list) {
        this.difficulties = Arrays.stream(list.split(","))
            .map(s -> {
                try { return Difficulty.valueOf(s.trim().toUpperCase(Locale.ROOT)); }
                catch (IllegalArgumentException e) { return null; }
            })
            .filter(d -> d != null)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null || difficulties.isEmpty()) return false;
        return difficulties.contains(target.getWorld().getDifficulty());
    }
}
