package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Strider;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 给目标实体装上/取下鞍。take|t|remove|r=false
 */
public class SaddleMechanic implements Mechanic {

    private final boolean take;

    public SaddleMechanic(boolean take) {
        this.take = take;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target instanceof Pig) {
                ((Pig) target).setSaddle(!take);
                any = true;
            } else if (target instanceof Strider) {
                ((Strider) target).setSaddle(!take);
                any = true;
            } else if (target instanceof AbstractHorse horse) {
                AbstractHorseInventory inv = horse.getInventory();
                if (take) {
                    inv.setSaddle(null);
                } else {
                    inv.setSaddle(new ItemStack(org.bukkit.Material.SADDLE));
                }
                any = true;
            }
        }
        return any;
    }
}
