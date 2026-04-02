package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.script.ScriptingService;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.HashMap;
import java.util.Map;

/**
 * ?js{file=...;func=...}
 *
 * file: scripts/conditions/ 下的 JS 文件名
 * func: JS 中的函数名，默认 "test"
 */
public class ScriptCondition implements Condition {

    private final ScriptingService scriptingService;
    private final String scriptFile;
    private final String functionName;

    public ScriptCondition(ScriptingService scriptingService, String scriptFile, String functionName) {
        this.scriptingService = scriptingService;
        this.scriptFile = scriptFile;
        this.functionName = functionName;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (scriptingService == null || !scriptingService.isEnabled()) {
            return false;
        }
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("ctx", context);
        bindings.put("target", target);
        Object result = scriptingService.call("conditions/" + scriptFile, functionName, bindings);
        return result instanceof Boolean && (Boolean) result;
    }
}
