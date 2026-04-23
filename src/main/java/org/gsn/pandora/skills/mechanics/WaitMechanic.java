package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 等待条件满足后继续执行后续技能行。
 * 本类作为数据载体，实际等待逻辑由 SkillManager 处理。
 * conditions|cond|c|until=<conditionSpec>
 * interval|i=1  timeout|timeouttime|tt=200  cancelSkill|cancel|cs=false
 * cancelConditions|cc|unless=<conditionSpec>
 */
public class WaitMechanic implements Mechanic {

    private final int intervalTicks;
    private final int timeoutTicks;
    private final boolean cancelOnTimeout;

    public WaitMechanic(int intervalTicks, int timeoutTicks, boolean cancelOnTimeout) {
        this.intervalTicks = Math.max(1, intervalTicks);
        this.timeoutTicks = Math.max(1, timeoutTicks);
        this.cancelOnTimeout = cancelOnTimeout;
    }

    public int getIntervalTicks() { return intervalTicks; }
    public int getTimeoutTicks() { return timeoutTicks; }
    public boolean isCancelOnTimeout() { return cancelOnTimeout; }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        // wait 本身不执行操作，由 SkillManager 在解析技能行时处理
        return true;
    }
}
