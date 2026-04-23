package org.gsn.pandora.skills;

import java.util.List;

/**
 * 一个可复用的技能定义，对应 skills/ 目录里的一个条目。
 */
public class Skill {

    private final String name;
    private final double cooldown;
    private final List<SkillLine> lines;
    /** Js.Script 路径（相对 scripts/），null 表示不使用 JS 整体执行 */
    private final String jsScript;
    /** Js.Function 函数名 */
    private final String jsFunction;

    public Skill(String name, double cooldown, List<SkillLine> lines,
                 String jsScript, String jsFunction) {
        this.name = name;
        this.cooldown = cooldown;
        this.lines = lines;
        this.jsScript = jsScript;
        this.jsFunction = jsFunction;
    }

    public String getName() { return name; }
    public double getCooldown() { return cooldown; }
    public double getCooldownSeconds() { return cooldown; }
    public List<SkillLine> getLines() { return lines; }
    public String getJsScript() { return jsScript; }
    public String getJsFunction() { return jsFunction; }
    public boolean hasJsScript() { return jsScript != null && !jsScript.isEmpty(); }
}
