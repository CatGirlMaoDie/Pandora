package org.gsn.pandora.skills.condition;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/**
 * 检测目标在指定轴方向的速度范围。
 * x=min~max  absx=false  y=min~max  absy=false  z=min~max  absz=false
 * relative|rel=false（相对于实体朝向）
 */
public class DirectionalVelocityCondition implements Condition {

    private final Double minX, maxX, minY, maxY, minZ, maxZ;
    private final boolean absX, absY, absZ;

    public DirectionalVelocityCondition(Double minX, Double maxX, boolean absX,
                                         Double minY, Double maxY, boolean absY,
                                         Double minZ, Double maxZ, boolean absZ) {
        this.minX = minX; this.maxX = maxX; this.absX = absX;
        this.minY = minY; this.maxY = maxY; this.absY = absY;
        this.minZ = minZ; this.maxZ = maxZ; this.absZ = absZ;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (target == null) return false;
        Vector v = target.getVelocity();
        if (minX != null || maxX != null) {
            double vx = absX ? Math.abs(v.getX()) : v.getX();
            if (minX != null && vx < minX) return false;
            if (maxX != null && vx > maxX) return false;
        }
        if (minY != null || maxY != null) {
            double vy = absY ? Math.abs(v.getY()) : v.getY();
            if (minY != null && vy < minY) return false;
            if (maxY != null && vy > maxY) return false;
        }
        if (minZ != null || maxZ != null) {
            double vz = absZ ? Math.abs(v.getZ()) : v.getZ();
            if (minZ != null && vz < minZ) return false;
            if (maxZ != null && vz > maxZ) return false;
        }
        return true;
    }
}
