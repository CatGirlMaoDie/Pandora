package org.gsn.pandora.skills.condition;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/**
 * 检测目标是否在两点构成的长方体区域内。
 * location1|loc1|l1|a=x,y,z  location2|loc2|l2|b=x,y,z  relative|r=false
 */
public class CuboidCondition implements Condition {
    private final double x1, y1, z1, x2, y2, z2;
    private final boolean relative;
    private final boolean valid;

    public CuboidCondition(String loc1, String loc2, boolean relative) {
        this.relative = relative;
        double[] p1 = parseCoords(loc1);
        double[] p2 = parseCoords(loc2);
        if (p1 != null && p2 != null) {
            x1 = Math.min(p1[0], p2[0]); y1 = Math.min(p1[1], p2[1]); z1 = Math.min(p1[2], p2[2]);
            x2 = Math.max(p1[0], p2[0]); y2 = Math.max(p1[1], p2[1]); z2 = Math.max(p1[2], p2[2]);
            valid = true;
        } else {
            x1 = y1 = z1 = x2 = y2 = z2 = 0;
            valid = false;
        }
    }

    private double[] parseCoords(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String[] parts = raw.split(",");
        if (parts.length < 3) return null;
        try {
            return new double[]{
                Double.parseDouble(parts[0].trim()),
                Double.parseDouble(parts[1].trim()),
                Double.parseDouble(parts[2].trim())
            };
        } catch (NumberFormatException e) { return null; }
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        if (!valid || target == null) return false;
        Location loc = target.getLocation();
        double tx = loc.getX(), ty = loc.getY(), tz = loc.getZ();
        if (relative && context.getOrigin() != null) {
            Location origin = context.getOrigin();
            tx -= origin.getX(); ty -= origin.getY(); tz -= origin.getZ();
        }
        return tx >= x1 && tx <= x2 && ty >= y1 && ty <= y2 && tz >= z1 && tz <= z2;
    }
}
