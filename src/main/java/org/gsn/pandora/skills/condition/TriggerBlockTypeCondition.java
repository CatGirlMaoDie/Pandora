package org.gsn.pandora.skills.condition;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测触发事件的方块类型（在 onHurt 触发器中有效）。types|type|t|material|mat|m=... */
public class TriggerBlockTypeCondition implements Condition {

    private final Set<Material> materials;

    public TriggerBlockTypeCondition(String typeList) {
        this.materials = Arrays.stream(typeList.split(","))
            .map(s -> {
                String name = s.trim().toUpperCase(Locale.ROOT);
                try { return Material.valueOf(name); }
                catch (IllegalArgumentException e) { return MaterialUtil.matchMaterialByNameOrChinese(s.trim()); }
            })
            .filter(m -> m != null)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || materials.isEmpty()) return false;
        var lastDmg = target.getLastDamageCause();
        if (!(lastDmg instanceof EntityDamageByBlockEvent blockEvent)) return false;
        var block = blockEvent.getDamager();
        return block != null && materials.contains(block.getType());
    }
}
