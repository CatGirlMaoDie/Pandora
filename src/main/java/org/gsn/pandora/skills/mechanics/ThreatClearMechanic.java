package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/** 清空施法者的威胁表（对应 MythicMobs 的 threatclear）。 */
public class ThreatClearMechanic implements Mechanic {

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        var plugin = context.getPlugin();
        if (!(plugin instanceof org.gsn.pandora.Pandora pandora)) return false;
        var threat = pandora.getThreatTableManager();
        if (threat == null) return false;
        LivingEntity caster = context.getCaster();
        if (caster == null) return false;
        threat.clear(caster);
        return true;
    }
}
