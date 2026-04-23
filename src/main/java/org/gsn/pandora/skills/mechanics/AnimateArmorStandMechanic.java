package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.EulerAngle;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 设置盔甲架各部位姿态。
 * head|h=x,y,z  body|b=x,y,z  leftArm|la=x,y,z  rightArm|ra=x,y,z
 * leftLeg|ll=x,y,z  rightLeg|rl=x,y,z  useDegrees|ud=true
 */
public class AnimateArmorStandMechanic implements Mechanic {

    private final EulerAngle head, body, leftArm, rightArm, leftLeg, rightLeg;
    private final boolean useDegrees;

    public AnimateArmorStandMechanic(String head, String body,
                                     String leftArm, String rightArm,
                                     String leftLeg, String rightLeg,
                                     boolean useDegrees) {
        this.useDegrees = useDegrees;
        this.head = parse(head);
        this.body = parse(body);
        this.leftArm = parse(leftArm);
        this.rightArm = parse(rightArm);
        this.leftLeg = parse(leftLeg);
        this.rightLeg = parse(rightLeg);
    }

    private EulerAngle parse(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String[] parts = raw.split(",");
        if (parts.length < 3) return null;
        try {
            double x = Double.parseDouble(parts[0].trim());
            double y = Double.parseDouble(parts[1].trim());
            double z = Double.parseDouble(parts[2].trim());
            if (useDegrees) {
                x = Math.toRadians(x);
                y = Math.toRadians(y);
                z = Math.toRadians(z);
            }
            return new EulerAngle(x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof ArmorStand stand)) continue;
            if (head != null) stand.setHeadPose(head);
            if (body != null) stand.setBodyPose(body);
            if (leftArm != null) stand.setLeftArmPose(leftArm);
            if (rightArm != null) stand.setRightArmPose(rightArm);
            if (leftLeg != null) stand.setLeftLegPose(leftLeg);
            if (rightLeg != null) stand.setRightLegPose(rightLeg);
            any = true;
        }
        return any;
    }
}
