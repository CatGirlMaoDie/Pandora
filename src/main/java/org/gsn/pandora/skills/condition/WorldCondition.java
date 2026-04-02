package org.gsn.pandora.skills.condition;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Condition;
import org.gsn.pandora.skills.SkillExecutionContext;

public class WorldCondition implements Condition {

    private final String worldName;

    public WorldCondition(String worldName) {
        this.worldName = worldName;
    }

    @Override
    public boolean matches(SkillExecutionContext context, LivingEntity target) {
        World world = null;
        if (target != null) {
            world = target.getWorld();
        } else if (context.getOrigin() != null) {
            world = context.getOrigin().getWorld();
        }
        if (world == null || worldName == null || worldName.isEmpty()) {
            return false;
        }
        return world.getName().equalsIgnoreCase(worldName);
    }
}
