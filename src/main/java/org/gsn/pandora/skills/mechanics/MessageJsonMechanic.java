package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 向目标玩家发送 JSON 格式消息（通过 spigot().sendMessage 实现）。
 * message|msg|m=...
 */
public class MessageJsonMechanic implements Mechanic {

    private final String json;

    public MessageJsonMechanic(String json) {
        this.json = json == null ? "" : json;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (json.isEmpty() || targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player) || target.isDead()) continue;
            try {
                net.md_5.bungee.api.chat.BaseComponent[] components =
                    net.md_5.bungee.chat.ComponentSerializer.parse(json);
                ((Player) target).spigot().sendMessage(components);
                any = true;
            } catch (Exception e) {
                // JSON 解析失败时退回普通消息
                ((Player) target).sendMessage(json);
                any = true;
            }
        }
        return any;
    }
}
