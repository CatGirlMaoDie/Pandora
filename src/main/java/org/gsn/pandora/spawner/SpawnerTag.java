package org.gsn.pandora.spawner;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 为 Spawner 生成的实体打上简单标记，便于未来统计和区分。
 */
public final class SpawnerTag {

    private final NamespacedKey key;

    public SpawnerTag(JavaPlugin plugin) {
        this.key = new NamespacedKey(plugin, "spawner-id");
    }

    public NamespacedKey getKey() {
        return key;
    }
}
