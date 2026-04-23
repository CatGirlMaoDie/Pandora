package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.Interaction;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 设置 Interaction 实体的碰撞箱大小。
 * height|h=1.0  width|w=1.0
 */
public class SetInteractionSizeMechanic implements Mechanic {

    private final float height;
    private final float width;

    public SetInteractionSizeMechanic(float height, float width) {
        this.height = Math.max(0.01f, height);
        this.width = Math.max(0.01f, width);
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Interaction interaction)) continue;
            interaction.setInteractionHeight(height);
            interaction.setInteractionWidth(width);
            any = true;
        }
        return any;
    }
}
