package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/**
 * 检测目标实体最近一次收到的信号是否匹配（配合 sendsignal / onSignal）。
 * signal|s|sig=<信号名>
 */
public class SignalCondition implements Condition {

    private final String signal;

    public SignalCondition(String signal) {
        this.signal = signal == null ? "" : signal;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || signal.isEmpty()) return false;
        var plugin = context.getPlugin();
        if (!(plugin instanceof org.gsn.pandora.Pandora pandora)) return false;
        var signals = pandora.getSignalManager();
        if (signals == null) return false;
        String last = signals.getLastSignal(target.getUniqueId());
        return last != null && last.equalsIgnoreCase(signal);
    }
}
