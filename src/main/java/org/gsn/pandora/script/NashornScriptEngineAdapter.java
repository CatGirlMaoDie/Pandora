package org.gsn.pandora.script;

import org.bukkit.plugin.java.JavaPlugin;

import javax.script.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * 基于 javax.script 的简单 JS 适配器，默认尝试使用 "nashorn" 引擎。
 *
 * 注意：在 Java 15+ 中 Nashorn 已被移除，服主需要自行通过依赖提供 JS 引擎，
 * 否则本适配器会记录警告并跳过脚本执行。
 */
public class NashornScriptEngineAdapter implements ScriptEngineAdapter {

    private final JavaPlugin plugin;
    private ScriptEngine engine;
    // 简单缓存：脚本路径 -> 已加载的标记（防止重复 eval）
    private final Map<String, Boolean> loadedScripts = new ConcurrentHashMap<>();

    public NashornScriptEngineAdapter(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        ScriptEngineManager manager = new ScriptEngineManager();
        this.engine = manager.getEngineByName("nashorn");
        if (this.engine == null) {
            plugin.getLogger().warning("未找到 JS 引擎 'nashorn'，JavaScript 功能将不可用。");
        }
    }

    @Override
    public Object invokeFunction(String scriptPath, String functionName, Map<String, Object> bindings) throws ScriptException {
        if (engine == null) {
            return null;
        }

        File scriptsDir = new File(plugin.getDataFolder(), "scripts");
        File scriptFile = new File(scriptsDir, scriptPath.replace("\\", "/"));
        if (!scriptFile.isFile()) {
            plugin.getLogger().warning("找不到脚本文件: " + scriptFile.getAbsolutePath());
            return null;
        }

        try {
            // 首次使用某个脚本文件时，先 eval 一次
            if (!loadedScripts.containsKey(scriptFile.getAbsolutePath())) {
                try (FileReader reader = new FileReader(scriptFile)) {
                    engine.eval(reader);
                }
                loadedScripts.put(scriptFile.getAbsolutePath(), Boolean.TRUE);
            }

            if (!(engine instanceof Invocable)) {
                plugin.getLogger().warning("当前脚本引擎不支持 Invocable，无法调用函数。");
                return null;
            }

            Invocable invocable = (Invocable) engine;
            Bindings engineBindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
            if (bindings != null) {
                for (Map.Entry<String, Object> entry : bindings.entrySet()) {
                    engineBindings.put(entry.getKey(), entry.getValue());
                }
            }
            return invocable.invokeFunction(functionName, new Object[]{});
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "读取脚本文件失败: " + scriptFile.getAbsolutePath(), e);
            return null;
        } catch (NoSuchMethodException e) {
            plugin.getLogger().log(Level.WARNING,
                "脚本中未找到函数 '" + functionName + "' 于 " + scriptFile.getName(), e);
            return null;
        }
    }

    @Override
    public void reload() {
        // 简单实现：清空缓存，下次调用时重新 eval
        loadedScripts.clear();
    }
}
