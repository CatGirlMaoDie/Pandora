package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测两个字符串是否相等（支持占位符比较）。value1|val1|v1=...  value2|val2|v2=... */
public class StringEqualsCondition implements Condition {
    private final String v1, v2;
    public StringEqualsCondition(String v1, String v2) {
        this.v1 = v1 == null ? "" : v1;
        this.v2 = v2 == null ? "" : v2;
    }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        return v1.equals(v2);
    }
}
