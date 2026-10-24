package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/**
 * 检测变量值（对应 MythicMobs 的 variable 条件）。
 * variable|var|v|name=<名字>  value|val=<期望值>  global|g=false
 * 数值类型自动做数值比较，其余做字符串比较。
 */
public class VariableCondition implements Condition {

    private final String name;
    private final String expected;
    private final boolean global;

    public VariableCondition(String name, String expected, boolean global) {
        this.name = name == null ? "" : name;
        this.expected = expected == null ? "" : expected;
        this.global = global;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (name.isEmpty()) return false;
        String actual;
        if (global) {
            actual = org.gsn.pandora.skills.mechanics.VariableSetMechanic.GlobalVariables.get(name);
        } else {
            LivingEntity e = target != null ? target : context.getCaster();
            actual = org.gsn.pandora.skills.mechanics.VariableSetMechanic.getEntityVariable(e, name);
        }
        if (actual == null) return false;
        // 尝试数值比较
        try {
            double a = Double.parseDouble(actual);
            double b = Double.parseDouble(expected);
            return a == b;
        } catch (NumberFormatException ignored) {
            return actual.equalsIgnoreCase(expected);
        }
    }
}
