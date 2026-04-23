package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标所在世界的游戏时间范围（0-23999）。支持跨越 0 点的区间。 */
public class WorldTimeCondition implements Condition {

    private final long from;
    private final long to;

    public WorldTimeCondition(long from, long to) {
        this.from = Math.max(0L, Math.min(23999L, from));
        this.to = Math.max(0L, Math.min(23999L, to));
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        long time = target.getWorld().getTime();
        if (from <= to) return time >= from && time <= to;
        return time >= from || time <= to;
    }
}
