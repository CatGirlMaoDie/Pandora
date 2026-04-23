package org.gsn.pandora.skills.mechanics;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 对目标脚下方块施加骨粉效果。
 * blockFace|bf|face|f=UP
 */
public class BoneMealMechanic implements Mechanic {

    private final BlockFace face;

    public BoneMealMechanic(String faceName) {
        BlockFace f = BlockFace.UP;
        if (faceName != null) {
            try { f = BlockFace.valueOf(faceName.toUpperCase(Locale.ROOT)); }
            catch (IllegalArgumentException ignored) {}
        }
        this.face = f;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null) continue;
            Block block = target.getLocation().getBlock().getRelative(face);
            if (block.getWorld() == null) continue;
            block.applyBoneMeal(face);
            any = true;
        }
        return any;
    }
}
