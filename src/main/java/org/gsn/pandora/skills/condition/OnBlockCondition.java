package org.gsn.pandora.skills.condition;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.Locale;

/**
 * 判断目标脚下方块类型是否匹配。
 */
public class OnBlockCondition implements Condition {

    private final Material type;

    public OnBlockCondition(String typeName) {
        Material parsed = null;
        if (typeName != null && !typeName.isEmpty()) {
            // 先尝试按枚举 ID 解析（如 STONE）
            try {
                parsed = Material.valueOf(typeName.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ignored) {
                // 若失败，再尝试按显示名/中文名解析
                parsed = MaterialUtil.matchMaterialByNameOrChinese(typeName);
            }
        }
        this.type = parsed;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (type == null) {
            return false;
        }
        Location loc = null;
        if (target != null) {
            loc = target.getLocation();
        } else if (context.getOrigin() != null) {
            loc = context.getOrigin();
        }
        if (loc == null || loc.getWorld() == null) {
            return false;
        }
        Block block = loc.clone().add(0, -1, 0).getBlock();
        return block.getType() == type;
    }
}
