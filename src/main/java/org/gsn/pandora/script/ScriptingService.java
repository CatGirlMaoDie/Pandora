package org.gsn.pandora.script;

import org.bukkit.plugin.java.JavaPlugin;

import javax.script.ScriptException;
import java.util.Map;
import java.util.logging.Level;

/**
 * 封装脚本调用，对外提供简单的 call 接口。
 */
public class ScriptingService {

    private final JavaPlugin plugin;
    private final ScriptEngineAdapter adapter;
    private final boolean enabled;

    public ScriptingService(JavaPlugin plugin, ScriptEngineAdapter adapter, boolean enabled) {
        this.plugin = plugin;
        this.adapter = adapter;
        this.enabled = enabled && adapter != null;
        if (this.enabled && this.adapter != null) {
            try {
                this.adapter.init();
            } catch (ScriptException e) {
                plugin.getLogger().log(Level.WARNING, "初始化脚本引擎失败", e);
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Object call(String scriptPath, String functionName, Map<String, Object> bindings) {
        if (!enabled || adapter == null) {
            return null;
        }
        try {
            return adapter.invokeFunction(scriptPath, functionName, bindings);
        } catch (ScriptException e) {
            plugin.getLogger().log(Level.WARNING,
                "执行脚本函数失败: " + scriptPath + "#" + functionName, e);
            return null;
        }
    }

    public void reload() {
        if (!enabled || adapter == null) {
            return;
        }
        try {
            adapter.reload();
        } catch (ScriptException e) {
            plugin.getLogger().log(Level.WARNING, "重载脚本失败", e);
        }
    }
}
