package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 向目标实体发送信号（对应 MythicMobs 的 sendsignal）。
 * signal|s|sig=<信号名>
 * 收到信号的实体会触发其 onSignal 技能，并且可用 signal 条件检测。
 */
public class SendSignalMechanic implements Mechanic {

    private final String signal;

    public SendSignalMechanic(String signal) {
        this.signal = signal == null ? "" : signal;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (signal.isEmpty() || targets == null || targets.isEmpty()) return false;
        var plugin = context.getPlugin();
        if (!(plugin instanceof org.gsn.pandora.Pandora pandora)) return false;
        org.gsn.pandora.mobs.SignalManager signals = pandora.getSignalManager();
        org.gsn.pandora.skills.SkillManager skillManager = pandora.getSkillManager();
        if (signals == null || skillManager == null) return false;

        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.isDead()) continue;
            signals.setLastSignal(target.getUniqueId(), signal);
            // 触发该实体的 onSignal 技能
            var mobManager = pandora.getMobManager();
            if (mobManager != null && mobManager.isPandoraMob(target)) {
                var mobType = mobManager.getMobTypeByEntity(target);
                if (mobType != null) {
                    var ctx = new org.gsn.pandora.skills.SkillExecutionContext(
                        context.getPlugin(), target, context.getCaster(),
                        org.gsn.pandora.skills.TriggerType.ON_SIGNAL, target.getLocation());
                    for (String skillName : mobType.getSkillsForTrigger(org.gsn.pandora.skills.TriggerType.ON_SIGNAL)) {
                        skillManager.castSkill(skillName, ctx);
                    }
                }
            }
            any = true;
        }
        return any;
    }
}
