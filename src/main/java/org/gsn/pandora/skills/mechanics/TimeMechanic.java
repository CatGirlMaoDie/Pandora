package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 修改目标所在世界的时间。
 * mode|m=ADD  amount|amt|ticks|t=20
 */
public class TimeMechanic implements Mechanic {

    private final boolean set; // true=SET, false=ADD
    private final long amount;

    public TimeMechanic(String mode, long amount) {
        this.set = "SET".equalsIgnoreCase(mode == null ? "ADD" : mode.trim());
        this.amount = amount;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null || target.getWorld() == null) continue;
            org.bukkit.World world = target.getWorld();
            if (set) {
                world.setTime(amount);
            } else {
                world.setTime(world.getTime() + amount);
            }
            any = true;
            break; // 世界级操作只做一次
        }
        return any;
    }
}
