package org.gsn.pandora.skills.targeter;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.skills.Targeter;

import java.util.ArrayList;
import java.util.List;

public class PlayersInRadiusTargeter implements Targeter {

    private final double radius;

    public PlayersInRadiusTargeter(double radius) {
        this.radius = radius;
    }

    @Override
    public List<LivingEntity> resolve(SkillExecutionContext context) {
        List<LivingEntity> result = new ArrayList<>();
        Location origin = context.getOrigin();
        if (origin == null || origin.getWorld() == null) {
            return result;
        }
        for (Player player : origin.getWorld().getPlayers()) {
            if (player.getLocation().distanceSquared(origin) <= radius * radius) {
                result.add(player);
            }
        }
        return result;
    }
}
