package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 修改目标抛射物的属性。
 * trait|t=VELOCITY  action|a=MULTIPLY  value|v=0
 * trait: VELOCITY, GRAVITY
 */
public class ModifyProjectileMechanic implements Mechanic {

    public enum Trait { VELOCITY, GRAVITY }
    public enum Action { SET, ADD, MULTIPLY }

    private final Trait trait;
    private final Action action;
    private final double value;

    public ModifyProjectileMechanic(String traitName, String actionName, double value) {
        Trait t = Trait.VELOCITY;
        try { t = Trait.valueOf(traitName == null ? "VELOCITY" : traitName.toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException ignored) {}
        this.trait = t;

        Action a = Action.MULTIPLY;
        try { a = Action.valueOf(actionName == null ? "MULTIPLY" : actionName.toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException ignored) {}
        this.action = a;
        this.value = value;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Projectile proj)) continue;
            switch (trait) {
                case VELOCITY -> {
                    var v = proj.getVelocity();
                    switch (action) {
                        case SET -> proj.setVelocity(v.normalize().multiply(value));
                        case ADD -> proj.setVelocity(v.multiply(1 + value));
                        case MULTIPLY -> proj.setVelocity(v.multiply(value));
                    }
                }
                case GRAVITY -> {
                    boolean g = switch (action) {
                        case SET -> value > 0;
                        case ADD, MULTIPLY -> proj.hasGravity();
                    };
                    proj.setGravity(g);
                }
            }
            any = true;
        }
        return any;
    }
}
