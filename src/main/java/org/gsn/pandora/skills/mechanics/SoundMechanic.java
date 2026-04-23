package org.gsn.pandora.skills.mechanics;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 在目标位置播放音效。
 * sound|s=ENTITY_GENERIC_EXPLODE  volume|v=1.0  pitch|p=1.0
 */
public class SoundMechanic implements Mechanic {

    private final Sound sound;
    private final String customSound;
    private final float volume;
    private final float pitch;

    public SoundMechanic(String soundName, float volume, float pitch) {
        Sound s = null;
        String custom = null;
        if (soundName != null && !soundName.isEmpty()) {
            NamespacedKey key = soundName.contains(":")
                ? NamespacedKey.fromString(soundName.toLowerCase(Locale.ROOT))
                : NamespacedKey.minecraft(soundName.toLowerCase(Locale.ROOT));
            if (key != null) s = Registry.SOUNDS.get(key);
            if (s == null) custom = soundName;
        }
        this.sound = s;
        this.customSound = custom;
        this.volume = Math.max(0f, volume);
        this.pitch = Math.max(0.5f, Math.min(2.0f, pitch));
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (target == null) continue;
            Location loc = target.getLocation();
            if (loc.getWorld() == null) continue;
            if (sound != null) {
                loc.getWorld().playSound(loc, sound, volume, pitch);
            } else if (customSound != null) {
                loc.getWorld().playSound(loc, customSound, volume, pitch);
            }
            any = true;
        }
        return any;
    }
}
