package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.EntityUtil;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class SummonMechanic implements Mechanic {

    private final EntityType entityType;
    private final int amount;
    private final double radius;

    public SummonMechanic(String typeName, int amount, double radius) {
        EntityType parsed = null;
        if (typeName != null && !typeName.isEmpty()) {
            // 先尝试按枚举 ID 解析（如 ZOMBIE）
            try {
                parsed = EntityType.valueOf(typeName.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ignored) {
                // 若失败，再尝试按显示名/中文名解析
                parsed = EntityUtil.matchEntityTypeByNameOrChinese(typeName);
            }
        }
        this.entityType = parsed;
        this.amount = Math.max(1, amount);
        this.radius = Math.max(0.0, radius);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        Location origin = context.getOrigin();
        if (entityType == null || origin == null) {
            return false;
        }
        World world = origin.getWorld();
        if (world == null) {
            return false;
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        boolean any = false;
        for (int i = 0; i < amount; i++) {
            Location spawnLoc = origin.clone();
            if (radius > 0.0) {
                double dx = (random.nextDouble() * 2.0 - 1.0) * radius;
                double dz = (random.nextDouble() * 2.0 - 1.0) * radius;
                spawnLoc.add(dx, 0.0, dz);
            }
            world.spawnEntity(spawnLoc, entityType);
            any = true;
        }
        return any;
    }
}
