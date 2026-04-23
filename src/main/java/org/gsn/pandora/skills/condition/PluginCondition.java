package org.gsn.pandora.skills.condition;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测指定插件是否已加载。plugin|p|pl=... */
public class PluginCondition implements Condition {
    private final String pluginName;
    public PluginCondition(String name) { this.pluginName = name == null ? "" : name; }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (pluginName.isEmpty()) return false;
        return Bukkit.getPluginManager().getPlugin(pluginName) != null
            && Bukkit.getPluginManager().isPluginEnabled(pluginName);
    }
}
