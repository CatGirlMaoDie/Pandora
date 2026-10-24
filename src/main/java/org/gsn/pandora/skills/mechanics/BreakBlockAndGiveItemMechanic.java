package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.List;
import java.util.Locale;

/**
 * 破坏目标脚下方块并给予玩家物品。
 * doDrops|drops|d=true  doEffect|effect|e=true
 * items|item|i=<材质>  amount|a=1
 */
public class BreakBlockAndGiveItemMechanic implements Mechanic {

    private final boolean doDrops;
    private final boolean doEffect;
    private final ItemStack giveItem;

    public BreakBlockAndGiveItemMechanic(boolean doDrops, boolean doEffect,
                                         String materialName, int amount) {
        this.doDrops = doDrops;
        this.doEffect = doEffect;
        org.bukkit.Material mat = null;
        if (materialName != null && !materialName.isBlank()) {
            String name = materialName.contains(":")
                ? materialName.substring(materialName.indexOf(':') + 1).toUpperCase(Locale.ROOT)
                : materialName.toUpperCase(Locale.ROOT);
            try { mat = org.bukkit.Material.valueOf(name); }
            catch (IllegalArgumentException e) { mat = MaterialUtil.matchMaterialByNameOrChinese(materialName); }
        }
        this.giveItem = mat != null ? new ItemStack(mat, Math.max(1, amount)) : null;
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
            if (doDrops) block.breakNaturally();
            else block.setType(org.bukkit.Material.AIR);
            // 给予物品
            if (giveItem != null && target instanceof Player p) {
                p.getInventory().addItem(giveItem.clone());
            }
            any = true;
        }
        return any;
    }
}
