package org.gsn.pandora.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.Pandora;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SpawnCommand implements SubCommand {

    private final Pandora plugin;

    public SpawnCommand(Pandora plugin) {
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

        if (args.length < 1) {
            sender.sendMessage("§e用法：" + getUsage(label));
            return true;
        }

        Player player = (Player) sender;
        String mobId = args[0];
        int amount = 1;

        if (args.length >= 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
                amount = 1;
            }
        }

        if (amount <= 0) {
            amount = 1;
        }

        int success = 0;
        for (int i = 0; i < amount; i++) {
            LivingEntity spawned = plugin.getMobManager().spawn(mobId, player.getLocation());
            if (spawned != null) {
                success++;
            }
        }

        if (success == 0) {
            sender.sendMessage("§c生成失败，可能是 mobId 不存在：" + mobId);
        } else {
            sender.sendMessage("§a已在你的位置生成 §e" + success + "§a 个实体：" + mobId);
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1 && plugin.getMobManager() != null) {
            List<String> ids = plugin.getMobManager().getAllMobTypes()
                    .stream()
                    .map(t -> t.getId())
                    .collect(Collectors.toList());
            return CommandUtils.filterPrefix(ids, args[0]);
        }
        return Collections.emptyList();
    }

    @Override
    public String getPermission() {
        return "pandora.command.spawn";
    }

    @Override
    public String getDescription() {
        return "生成自定义实体";
    }

    @Override
    public String getUsage(String label) {
        return "/" + label + " spawn <mobId> [数量]";
    }
}
