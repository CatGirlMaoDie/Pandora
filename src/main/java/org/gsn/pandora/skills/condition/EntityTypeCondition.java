package org.gsn.pandora.skills.condition;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** 检测目标实体类型。type 支持逗号分隔多个类型。 */
public class EntityTypeCondition implements Condition {

    private final Set<EntityType> types;

    public EntityTypeCondition(String typeList) {
        this.types = Arrays.stream(typeList.split(","))
            .map(s -> {
                try { return EntityType.valueOf(s.trim().toUpperCase(Locale.ROOT)); }
                catch (IllegalArgumentException e) { return null; }
            })
            .filter(t -> t != null)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || types.isEmpty()) return false;
        return types.contains(target.getType());
    }
}
