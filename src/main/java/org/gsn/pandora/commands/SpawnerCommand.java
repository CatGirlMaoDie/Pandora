package org.gsn.pandora.commands;

import org.bukkit.command.CommandSender;
import org.gsn.pandora.Pandora;
import org.gsn.pandora.spawner.SpawnerCommandHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SpawnerCommand implements SubCommand {

    private final Pandora plugin;
    private final SpawnerCommandHandler handler;

    public SpawnerCommand(Pandora plugin) {
        this.plugin = plugin;
        this.handler = new SpawnerCommandHandler(plugin, plugin.getSpawnerManager());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (plugin.getSpawnerManager() == null) {
            sender.sendMessage("§cSpawnerManager 尚未初始化。");
            return true;
        }
        // 调用原有的 handler，offset = 0 因为 args 已经不包含 "spawner" 了
        return handler.handle(sender, label, args, 0);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (!sender.hasPermission("pandora.command.spawner")) {
            return Collections.emptyList();
        }

        // args[0] = spawner 子命令
        if (args.length == 1) {
            return CommandUtils.filterPrefix(List.of("list", "info", "tp", "teleport", "create", "remove"), args[0]);
        }

        String spawnerSub = args[0].toLowerCase();

        if (plugin.getSpawnerManager() != null) {
            // info/tp/teleport/remove <id>
            if ((spawnerSub.equals("info") || spawnerSub.equals("tp")
                    || spawnerSub.equals("teleport") || spawnerSub.equals("remove")) && args.length == 2) {
                List<String> ids = plugin.getSpawnerManager().getAllSpawners()
                        .stream()
                        .map(d -> d.getId())
                        .collect(Collectors.toList());
                return CommandUtils.filterPrefix(ids, args[1]);
            }

            // create <id> <mobId>
            if (spawnerSub.equals("create") && args.length == 3 && plugin.getMobManager() != null) {
                List<String> ids = plugin.getMobManager().getAllMobTypes()
                        .stream()
                        .map(t -> t.getId())
                        .collect(Collectors.toList());
                return CommandUtils.filterPrefix(ids, args[2]);
            }
        }

        return Collections.emptyList();
    }

    @Override
    public String getPermission() {
        return "pandora.command.spawner";
    }

    @Override
    public String getDescription() {
        return "管理刷怪点";
    }

    @Override
    public String getUsage(String label) {
        return "/" + label + " spawner ...";
    }
}
