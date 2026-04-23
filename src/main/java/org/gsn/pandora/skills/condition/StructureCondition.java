package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 检测目标位置是否在指定结构内（通过 Paper API 的 locateNearestStructure）。
 * structures|s=village,stronghold  radius=128
 */
public class StructureCondition implements Condition {
    private final Set<String> structures;
    private final int radius;

    public StructureCondition(String structureList, int radius) {
        this.structures = Arrays.stream(structureList.split(","))
            .map(s -> s.trim().toLowerCase(Locale.ROOT))
            .collect(Collectors.toSet());
        this.radius = Math.max(1, radius);
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null || structures.isEmpty()) return false;
        var loc = target.getLocation();
        for (String structName : structures) {
            try {
                org.bukkit.StructureType type = org.bukkit.StructureType.getStructureTypes().get(structName);
                if (type == null) continue;
                var nearest = target.getWorld().locateNearestStructure(loc, type, radius, false);
                if (nearest != null) return true;
            } catch (Exception ignored) {}
        }
        return false;
    }
}
