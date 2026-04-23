package org.gsn.pandora.skills.condition;

import org.bukkit.block.Biome;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测目标所在群系。biome 支持逗号分隔多个群系名。 */
public class BiomeCondition implements Condition {

    private final Set<String> biomes;

    public BiomeCondition(String biomeList) {
        this.biomes = Arrays.stream(biomeList.split(","))
            .map(s -> s.trim().toUpperCase(Locale.ROOT))
            .collect(Collectors.toSet());
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || target.getWorld() == null) return false;
        Biome biome = target.getLocation().getBlock().getBiome();
        String name = biome.getKey().getKey().toUpperCase(Locale.ROOT);
        // 支持带命名空间（minecraft:plains）和不带命名空间（plains）两种写法
        return biomes.contains(name) || biomes.stream().anyMatch(b -> name.endsWith(b) || b.endsWith(name));
    }
}
