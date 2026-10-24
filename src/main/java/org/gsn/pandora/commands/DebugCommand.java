package org.gsn.pandora.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.Pandora;
import org.gsn.pandora.mobs.MobType;

import java.util.Collections;
import java.util.List;

public class DebugCommand implements SubCommand {

    private final Pandora plugin;

    public DebugCommand(Pandora plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c该命令只能由玩家执行。");
            return true;
        }

        if (plugin.getMobManager() == null) {
            sender.sendMessage("§cMobManager 尚未初始化。");
            return true;
        }

        Player player = (Player) sender;
        Entity target = player.getTargetEntity(20);

        if (!(target instanceof LivingEntity)) {
            sender.sendMessage("§e请看着一只实体再使用此命令。");
            return true;
        }

        LivingEntity le = (LivingEntity) target;
        MobType mobType = plugin.getMobManager().getMobTypeByEntity(le);

        if (mobType == null) {
            sender.sendMessage("§e该实体不是 Pandora 自定义 Mob。");
            return true;
        }

        sender.sendMessage("§6Pandora Mob 调试信息:");
        sender.sendMessage("§7ID: §f" + mobType.getId());
        sender.sendMessage("§7Type: §f" + mobType.getEntityType().name());
        sender.sendMessage("§7Health: §f" + le.getHealth() + " / " + le.getMaxHealth());
        sender.sendMessage("§7World: §f" + le.getWorld().getName());
        sender.sendMessage("§7Location: §f" + le.getLocation().getBlockX() + ", "
                + le.getLocation().getBlockY() + ", " + le.getLocation().getBlockZ());

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getPermission() {
        return "pandora.command.debug";
    }

    @Override
    public String getDescription() {
        return "调试信息";
    }

    @Override
    public String getUsage(String label) {
        return "/" + label + " debug";
    }
}
