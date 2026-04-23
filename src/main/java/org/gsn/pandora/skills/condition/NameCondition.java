package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.Locale;

/** 检测目标的自定义名称是否包含指定字符串（不区分大小写）。 */
public class NameCondition implements Condition {

    private final String name;

    public NameCondition(String name) {
        this.name = name == null ? "" : name.toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null || name.isEmpty()) return false;
        String customName = target.getCustomName();
        if (customName == null) customName = target.getName();
        return customName.toLowerCase(Locale.ROOT).contains(name);
    }
}
