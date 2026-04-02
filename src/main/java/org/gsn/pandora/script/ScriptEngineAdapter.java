package org.gsn.pandora.script;

import javax.script.ScriptException;
import java.util.Map;

/**
 * 统一的脚本引擎适配接口，隐藏具体 JS 引擎实现。
 */
public interface ScriptEngineAdapter {

    /**
     * 初始化脚本引擎。插件启动或重载时调用一次。
     */
    void init() throws ScriptException;

    /**
     * 调用脚本中的函数。
     *
     * @param scriptPath   在 plugins/Pandora/scripts/ 下的相对路径，例如 "skills/example_skills.js"
     * @param functionName 要调用的函数名
     * @param bindings     传入脚本的上下文数据
     * @return 脚本返回值，可为 null
     */
    Object invokeFunction(String scriptPath, String functionName, Map<String, Object> bindings) throws ScriptException;

    /**
     * 重载脚本（清理缓存等）。
     */
    void reload() throws ScriptException;
}
