package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.script.ScriptingService;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * script{file=...;func=...} @targeter
 *
 * file: scripts/skills/ 下的 JS 文件名
 * func: JS 中的函数名，默认 "execute"
 */
public class ScriptMechanic implements Mechanic {

    private final ScriptingService scriptingService;
    private final String scriptFile;
    private final String functionName;

    public ScriptMechanic(ScriptingService scriptingService, String scriptFile, String functionName) {
        this.scriptingService = scriptingService;
        this.scriptFile = scriptFile;
        this.functionName = functionName;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (scriptingService == null || !scriptingService.isEnabled()) {
            return false;
        }
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("ctx", context);
        bindings.put("targets", targets);
        Object result = scriptingService.call("skills/" + scriptFile, functionName, bindings);
        if (result instanceof Boolean) {
            return (Boolean) result;
        }
        return true;
    }
}
