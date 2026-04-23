package org.gsn.pandora.skills.condition;

import org.bukkit.block.Biome;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 检测目标所在群系的类型分类（温暖/寒冷/干旱等）。
 * type|t=WARM,COLD,SNOWY,OCEAN,JUNGLE,FOREST,PLAINS,DESERT,NETHER,END
 */
public class BiomeTypeCondition implements Condition {

    private final Set<String> types;

    public BiomeTypeCondition(String typeList) {
        this.types = Arrays.stream(typeList.split(","))
            .map(s -> s.trim().toUpperCase(Locale.ROOT))
            .collect(Collectors.toSet());
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null || types.isEmpty()) return false;
        Biome biome = target.getLocation().getBlock().getBiome();
        String name = biome.getKey().getKey().toUpperCase(Locale.ROOT);
        // 简单分类匹配
        for (String type : types) {
            boolean match = switch (type) {
                case "SNOWY" -> name.contains("SNOWY") || name.contains("FROZEN") || name.contains("ICE");
                case "COLD" -> name.contains("COLD") || name.contains("TAIGA") || name.contains("STONY");
                case "WARM" -> name.contains("WARM") || name.contains("JUNGLE") || name.contains("BAMBOO")
                    || name.contains("MANGROVE");
                case "DRY", "DESERT" -> name.contains("DESERT") || name.contains("BADLANDS")
                    || name.contains("SAVANNA") || name.contains("ERODED");
                case "OCEAN" -> name.contains("OCEAN") || name.contains("BEACH") || name.contains("RIVER");
                case "FOREST" -> name.contains("FOREST") || name.contains("BIRCH") || name.contains("DARK_FOREST");
                case "PLAINS" -> name.contains("PLAINS") || name.contains("MEADOW") || name.contains("SUNFLOWER");
                case "NETHER" -> name.contains("NETHER") || name.contains("BASALT") || name.contains("SOUL")
                    || name.contains("CRIMSON") || name.contains("WARPED");
                case "END" -> name.contains("END");
                case "MUSHROOM" -> name.contains("MUSHROOM");
                case "SWAMP" -> name.contains("SWAMP");
                default -> name.equals(type);
            };
            if (match) return true;
        }
        return false;
    }
}
