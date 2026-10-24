package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 修改施法者威胁表中目标的威胁值。
 * action|mode|a=ADD (ADD/SET/MULTIPLY)  amount|a=1
 * 无目标时对当前目标生效。
 */
public class ThreatChangeMechanic implements Mechanic {

    private final String action;
    private final double amount;

    public ThreatChangeMechanic(String action, double amount) {
        this.action = action == null ? "ADD" : action;
        this.amount = amount;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        var plugin = context.getPlugin();
        if (!(plugin instanceof org.gsn.pandora.Pandora pandora)) return false;
        var threat = pandora.getThreatTableManager();
        if (threat == null) return false;
        LivingEntity caster = context.getCaster();
        if (caster == null) return false;

        boolean any = false;
        if (targets != null) {
            for (LivingEntity t : targets) {
                if (t == null) continue;
                threat.modifyThreat(caster, t, amount, action);
                any = true;
            }
        }
        // 无目标时使用当前目标
        if (!any && context.getTarget() != null) {
            threat.modifyThreat(caster, context.getTarget(), amount, action);
            any = true;
        }
        return any;
    }
}
