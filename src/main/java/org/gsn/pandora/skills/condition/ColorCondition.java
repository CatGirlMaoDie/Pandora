package org.gsn.pandora.skills.condition;

import org.bukkit.DyeColor;
import org.bukkit.entity.*;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Locale;

/**
 * 检测目标实体的颜色（适用于羊、猫、狼等可染色实体）。
 * color|clr|c=WHITE,RED,...
 */
public class ColorCondition implements Condition {

    private final DyeColor color;

    public ColorCondition(String colorName) {
        DyeColor c = DyeColor.WHITE;
        if (colorName != null) {
            try { c = DyeColor.valueOf(colorName.toUpperCase(Locale.ROOT)); }
            catch (IllegalArgumentException ignored) {}
        }
        this.color = c;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        if (target instanceof Sheep sheep) return color.equals(sheep.getColor());
        if (target instanceof Cat) return false; // Cat 使用 CatType，无 DyeColor
        if (target instanceof Wolf wolf) return color.equals(wolf.getCollarColor());
        if (target instanceof Llama) return false; // Llama.Color 与 DyeColor 不兼容
        if (target instanceof Shulker shulker) return color.equals(shulker.getColor());
        return false;
    }
}
