package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 创建目标玩家的分身（复制外观的盔甲架）。
 * hasNamePlate|nameplate=true  usePlayerName|upn=false
 */
public class DoppelgangerMechanic implements Mechanic {

    private final boolean hasNamePlate;
    private final boolean usePlayerName;

    public DoppelgangerMechanic(boolean hasNamePlate, boolean usePlayerName) {
        this.hasNamePlate = hasNamePlate;
        this.usePlayerName = usePlayerName;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player p)) continue;
            var loc = p.getLocation();
            if (loc.getWorld() == null) continue;

            // 生成盔甲架模拟分身
            var stand = loc.getWorld().spawn(loc, org.bukkit.entity.ArmorStand.class);
            stand.setVisible(true);
            stand.setGravity(true);
            stand.setArms(true);
            stand.setBasePlate(false);

            // 复制玩家装备
            var eq = p.getInventory();
            var standEq = stand.getEquipment();
            if (standEq != null) {
                if (eq.getHelmet() != null) standEq.setHelmet(eq.getHelmet().clone());
                if (eq.getChestplate() != null) standEq.setChestplate(eq.getChestplate().clone());
                if (eq.getLeggings() != null) standEq.setLeggings(eq.getLeggings().clone());
                if (eq.getBoots() != null) standEq.setBoots(eq.getBoots().clone());
                standEq.setItemInMainHand(eq.getItemInMainHand().clone());
            }

            // 设置名称
            if (hasNamePlate) {
                String name = usePlayerName ? p.getName() : "_";
                stand.setCustomName(name);
                stand.setCustomNameVisible(true);
            }
            any = true;
        }
        return any;
    }
}
