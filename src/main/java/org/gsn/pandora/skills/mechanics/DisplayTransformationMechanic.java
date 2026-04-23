package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.Display;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 变换 Display 实体（平移/旋转/缩放）。
 * action|a=SET  transformationType|transformation|type|tt=TRANSLATION  values|value|val=0,0,0
 */
public class DisplayTransformationMechanic implements Mechanic {

    public enum TransformType { TRANSLATION, LEFT_ROTATION, RIGHT_ROTATION, SCALE }
    public enum Action { SET, ADD }

    private final Action action;
    private final TransformType type;
    private final float x, y, z, w;

    public DisplayTransformationMechanic(String actionName, String typeName, String values) {
        Action a = Action.SET;
        try { a = Action.valueOf(actionName == null ? "SET" : actionName.toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException ignored) {}
        this.action = a;

        TransformType t = TransformType.TRANSLATION;
        try { t = TransformType.valueOf(typeName == null ? "TRANSLATION" : typeName.toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException ignored) {}
        this.type = t;

        float[] v = parseValues(values);
        this.x = v[0]; this.y = v[1]; this.z = v[2]; this.w = v.length > 3 ? v[3] : 1.0f;
    }

    private float[] parseValues(String raw) {
        if (raw == null || raw.isBlank()) return new float[]{0, 0, 0, 1};
        String[] parts = raw.split(",");
        float[] result = new float[]{0, 0, 0, 1};
        for (int i = 0; i < Math.min(4, parts.length); i++) {
            try { result[i] = Float.parseFloat(parts[i].trim()); } catch (NumberFormatException ignored) {}
        }
        return result;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Display display)) continue;
            Transformation current = display.getTransformation();
            switch (type) {
                case TRANSLATION -> {
                    Vector3f t = action == Action.ADD
                        ? current.getTranslation().add(x, y, z)
                        : new Vector3f(x, y, z);
                    display.setTransformation(new Transformation(t,
                        current.getLeftRotation(), current.getScale(), current.getRightRotation()));
                }
                case SCALE -> {
                    Vector3f s = action == Action.ADD
                        ? current.getScale().add(x, y, z)
                        : new Vector3f(x, y, z);
                    display.setTransformation(new Transformation(current.getTranslation(),
                        current.getLeftRotation(), s, current.getRightRotation()));
                }
                case LEFT_ROTATION -> {
                    Quaternionf q = new Quaternionf(x, y, z, w).normalize();
                    display.setTransformation(new Transformation(current.getTranslation(),
                        q, current.getScale(), current.getRightRotation()));
                }
                case RIGHT_ROTATION -> {
                    Quaternionf q = new Quaternionf(x, y, z, w).normalize();
                    display.setTransformation(new Transformation(current.getTranslation(),
                        current.getLeftRotation(), current.getScale(), q));
                }
            }
            any = true;
        }
        return any;
    }
}
