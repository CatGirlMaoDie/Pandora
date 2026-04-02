package org.gsn.pandora.skills;

import java.util.List;

/**
 * 一个可复用的技能定义，对应 skills.yml 里的一个条目。
 */
public class Skill {

    private final String name;
    private final double cooldown;
    private final List<SkillLine> lines;

    public Skill(String name, double cooldown, List<SkillLine> lines) {
        this.name = name;
        this.cooldown = cooldown;
        this.lines = lines;
    }

    public String getName() {
        return name;
    }

    public double getCooldown() {
        return cooldown;
    }

    public double getCooldownSeconds() {
        return cooldown;
    }

    public List<SkillLine> getLines() {
        return lines;
    }
}
