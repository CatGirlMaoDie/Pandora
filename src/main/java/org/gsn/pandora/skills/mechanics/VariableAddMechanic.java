package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 数值变量加法（variableadd）。
 * variable|var|v|name=<名>  value|val|amount|a=<增量>  global|g=false
 */
public class VariableAddMechanic implements Mechanic {

    private final String name;
    private final String delta;
    private final boolean global;

    public VariableAddMechanic(String name, String delta, boolean global) {
        this.name = name == null ? "" : name;
        this.delta = delta == null ? "0" : delta;
        this.global = global;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (name.isEmpty()) return false;
        boolean any = false;
        List<LivingEntity> all = new java.util.ArrayList<>();
        if (targets != null) all.addAll(targets);
        if (!global && targets != null && targets.isEmpty() && context.getCaster() != null) {
            all.add(context.getCaster());
        }
        double d;
        try { d = Double.parseDouble(delta); } catch (NumberFormatException e) { return false; }
        for (LivingEntity entity : all) {
            if (entity == null || entity.isDead()) continue;
            String old = VariableSetMechanic.getEntityVariable(entity, name);
            double base = 0;
            if (old != null) {
                try { base = Double.parseDouble(old); } catch (NumberFormatException ignored) {}
            }
            VariableSetMechanic.setEntityVariable(entity, name,
                String.valueOf(base + d), base == Math.floor(base) ? "INTEGER" : "DOUBLE");
            any = true;
        }
        if (global) {
            String old = VariableSetMechanic.GlobalVariables.get(name);
            double base = 0;
            if (old != null) {
                try { base = Double.parseDouble(old); } catch (NumberFormatException ignored) {}
            }
            VariableSetMechanic.GlobalVariables.set(name, String.valueOf(base + d),
                base == Math.floor(base) ? "INTEGER" : "DOUBLE");
            any = true;
        }
        return any;
    }
}
