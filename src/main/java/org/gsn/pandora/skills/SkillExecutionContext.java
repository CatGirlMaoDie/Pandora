package org.gsn.pandora.skills;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 一次技能触发时的上下文信息。
 */
public class SkillExecutionContext {

    private final JavaPlugin plugin;
    private final LivingEntity caster;
    private final LivingEntity target;
    private final TriggerType triggerType;
    private final Location origin;

    public SkillExecutionContext(JavaPlugin plugin,
                                 LivingEntity caster,
                                 LivingEntity target,
                                 TriggerType triggerType,
                                 Location origin) {
        this.plugin = plugin;
        this.caster = caster;
        this.target = target;
        this.triggerType = triggerType;
        this.origin = origin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public LivingEntity getCaster() {
        return caster;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    /**
     * 技能释放的“中心位置”，如果未显式指定则使用施法者/目标。
     */
    public Location getOrigin() {
        if (origin != null) {
            return origin;
        }
        if (caster != null) {
            return caster.getLocation();
        }
        if (target != null) {
            return target.getLocation();
        }
        return null;
    }
}
