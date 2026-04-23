package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/** 检测目标玩家是否拥有指定权限。permission|p=... */
public class HasPermissionCondition implements Condition {
    private final String permission;
    public HasPermissionCondition(String permission) { this.permission = permission == null ? "" : permission; }
    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (!(target instanceof Player p) || permission.isEmpty()) return false;
        return p.hasPermission(permission);
    }
}
