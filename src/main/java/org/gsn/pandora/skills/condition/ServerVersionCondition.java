package org.gsn.pandora.skills.condition;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

/**
 * 检测服务器版本。
 * version|v|sv=1.21  mode: EQUALS/AFTER_OR_EQUAL/BEFORE
 */
public class ServerVersionCondition implements Condition {
    public enum Mode { EQUALS, AFTER_OR_EQUAL, BEFORE }

    private final int[] target;
    private final Mode mode;

    public ServerVersionCondition(String version, Mode mode) {
        this.mode = mode;
        this.target = parseVersion(version);
    }

    private int[] parseVersion(String v) {
        if (v == null) return new int[]{1, 21, 0};
        String clean = v.replaceAll("[^0-9.]", "");
        String[] parts = clean.split("\\.");
        int[] result = new int[3];
        for (int i = 0; i < Math.min(3, parts.length); i++) {
            try { result[i] = Integer.parseInt(parts[i]); } catch (NumberFormatException ignored) {}
        }
        return result;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        String raw = Bukkit.getBukkitVersion(); // e.g. "1.21.4-R0.1-SNAPSHOT"
        int[] current = parseVersion(raw);
        int cmp = compareVersions(current, this.target);
        return switch (mode) {
            case EQUALS -> cmp == 0;
            case AFTER_OR_EQUAL -> cmp >= 0;
            case BEFORE -> cmp < 0;
        };
    }

    private int compareVersions(int[] a, int[] b) {
        for (int i = 0; i < 3; i++) {
            if (a[i] != b[i]) return Integer.compare(a[i], b[i]);
        }
        return 0;
    }
}
