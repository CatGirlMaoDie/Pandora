package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 在目标位置喷射物品实体。
 * items|item|i=<材质>  amount|a=10  force|f|velocity|v=1  yForce|yf|yv=1
 * yOffset|yo|y=1  gravity|g=true  allowPickup|ap=false
 */
public class EffectItemSprayMechanic implements Mechanic {

    private final ItemStack item;
    private final int amount;
    private final double force;
    private final double yForce;
    private final double yOffset;
    private final boolean gravity;
    private final boolean allowPickup;

    public EffectItemSprayMechanic(String materialName, int amount, double force,
                                   double yForce, double yOffset, boolean gravity, boolean allowPickup) {
        Material mat = null;
        if (materialName != null && !materialName.isBlank()) {
            String name = materialName.contains(":")
                ? materialName.substring(materialName.indexOf(':') + 1).toUpperCase(Locale.ROOT)
                : materialName.toUpperCase(Locale.ROOT);
            try { mat = Material.valueOf(name); }
            catch (IllegalArgumentException e) { mat = MaterialUtil.matchMaterialByNameOrChinese(materialName); }
        }
        this.item = mat != null ? new ItemStack(mat) : new ItemStack(Material.STONE);
        this.amount = Math.max(1, amount);
        this.force = force;
        this.yForce = yForce;
        this.yOffset = yOffset;
        this.gravity = gravity;
        this.allowPickup = allowPickup;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        for (LivingEntity target : targets) {
            if (target == null || target.getWorld() == null) continue;
            Location loc = target.getLocation().clone().add(0, yOffset, 0);
            for (int i = 0; i < amount; i++) {
                Item dropped = target.getWorld().dropItem(loc, item.clone());
                dropped.setGravity(gravity);
                dropped.setPickupDelay(allowPickup ? 10 : Integer.MAX_VALUE);
                double vx = (rng.nextDouble() * 2 - 1) * force;
                double vz = (rng.nextDouble() * 2 - 1) * force;
                dropped.setVelocity(new Vector(vx, yForce, vz));
            }
            any = true;
        }
        return any;
    }
}
