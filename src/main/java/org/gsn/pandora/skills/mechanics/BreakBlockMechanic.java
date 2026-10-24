package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 破坏目标脚下方块。
 * doDrops|drops|d=true  doEffect|effect|e=true
 */
public class BreakBlockMechanic implements Mechanic {

    private final boolean doDrops;
    private final boolean doEffect;

    public BreakBlockMechanic(boolean doDrops, boolean doEffect) {
        this.doDrops = doDrops;
        this.doEffect = doEffect;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null) continue;
            Location loc = target.getLocation();
            if (loc.getWorld() == null) continue;
            Block block = loc.clone().subtract(0, 1, 0).getBlock();
            if (block.getType().isAir()) continue;
            if (doEffect) {
                block.getWorld().spawnParticle(org.bukkit.Particle.BLOCK,
                block.getLocation().add(0.5, 0.5, 0.5), 20,
                block.getBlockData());
            }
            if (doDrops) {
                block.breakNaturally();
            } else {
                block.setType(org.bukkit.Material.AIR);
            }
            any = true;
        }
        return any;
    }
}
