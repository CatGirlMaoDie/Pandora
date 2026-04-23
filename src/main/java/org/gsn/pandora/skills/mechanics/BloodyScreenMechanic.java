package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 给目标玩家施加/取消血屏效果（通过伤害事件模拟）。
 * duration|d=60  cancel|c=false
 */
public class BloodyScreenMechanic implements Mechanic {

    private final int durationTicks;
    private final boolean cancel;

    public BloodyScreenMechanic(int durationTicks, boolean cancel) {
        this.durationTicks = Math.max(1, durationTicks);
        this.cancel = cancel;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player p) || target.isDead()) continue;
            if (cancel) {
                // 取消效果：恢复满血量显示
                p.setHealth(Math.min(p.getHealth() + 0.001, p.getMaxHealth()));
            } else {
                // 通过设置极低血量触发血屏（不实际扣血）
                double original = p.getHealth();
                p.setHealth(Math.max(0.5, original * 0.1));
                org.gsn.pandora.util.FoliaScheduler.runEntityTaskLater(
                    context.getPlugin(), p, () -> {
                        if (!p.isDead()) p.setHealth(Math.min(original, p.getMaxHealth()));
                    }, durationTicks);
            }
            any = true;
        }
        return any;
    }
}
