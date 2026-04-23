package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 延迟执行（在 SkillManager 层面通过 scheduler 实现）。
 * 本类作为占位符，实际延迟逻辑由 SkillManager 处理。
 * ticks=20
 */
public class DelayMechanic implements Mechanic {

    private final int ticks;

    public DelayMechanic(int ticks) {
        this.ticks = Math.max(1, ticks);
    }

    public int getTicks() {
        return ticks;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        // delay 本身不执行任何操作，由 SkillManager 在解析技能行时处理
        return true;
    }
}
