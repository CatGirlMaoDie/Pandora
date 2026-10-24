package org.gsn.pandora.skills.mechanics;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 删除变量（对应 MythicMobs 的 unsetvariable / varunset）。
 * variable|var|v|name=<名字>  global|g=false
 */
public class VariableRemoveMechanic implements Mechanic {

    private final String name;
    private final boolean global;

    public VariableRemoveMechanic(String name, boolean global) {
        this.name = name == null ? "" : name;
        this.global = global;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (name.isEmpty()) return false;
        boolean any = false;
        if (global) {
            VariableSetMechanic.GlobalVariables.remove(name);
            any = true;
        }
        java.util.List<LivingEntity> all = new java.util.ArrayList<>();
        if (targets != null) all.addAll(targets);
        if (!global && targets != null && targets.isEmpty() && context.getCaster() != null) {
            all.add(context.getCaster());
        }
        for (LivingEntity target : all) {
            if (target == null) continue;
            NamespacedKey key = new NamespacedKey("pandora", "var_" + name.toLowerCase(Locale.ROOT));
            target.getPersistentDataContainer().remove(key);
            any = true;
        }
        return any;
    }
}
