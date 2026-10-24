package org.gsn.pandora;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsn.pandora.commands.*;

import java.util.*;

public class PandoraCommand extends Command {

    private final Pandora plugin;
    private final Map<String, SubCommand> subCommands;

    public PandoraCommand(Pandora plugin) {
        super("pandora", "Pandora 主命令", "/pandora [reload|spawn|spawner|debug]",
                List.of("pd"));
        this.plugin = plugin;
        setPermission("pandora.admin");

        // 注册所有子命令
        this.subCommands = new LinkedHashMap<>();
        registerSubCommand("reload", new ReloadCommand(plugin));
        registerSubCommand("spawn", new SpawnCommand(plugin));
        registerSubCommand("spawner", new SpawnerCommand(plugin));
        registerSubCommand("debug", new DebugCommand(plugin));
    }

    private void registerSubCommand(String name, SubCommand command) {
        subCommands.put(name.toLowerCase(), command);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender, label);
            return true;
        }

        String subName = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(subName);

        if (subCommand == null) {
            sendHelp(sender, label);
            return true;
        }

        // 检查权限
        String permission = subCommand.getPermission();
        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage("§c你没有权限执行此命令。");
            return true;
        }

        // 提取子命令参数（去掉子命令名称本身）
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        // 执行子命令
        return subCommand.execute(sender, label + " " + subName, subArgs);
    }

    private void sendHelp(CommandSender sender, String label) {
        sender.sendMessage("§6Pandora 指令帮助:");
        for (Map.Entry<String, SubCommand> entry : subCommands.entrySet()) {
            SubCommand cmd = entry.getValue();
            sender.sendMessage("§e" + cmd.getUsage(label) + " §7- " + cmd.getDescription());
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (!sender.hasPermission("pandora.admin")) {
            return Collections.emptyList();
        }

        // 第一层：补全子命令名称
        if (args.length == 1) {
            return CommandUtils.filterPrefix(new ArrayList<>(subCommands.keySet()), args[0]);
        }

        // 第二层及以上：委托给对应的子命令
        String subName = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(subName);

        if (subCommand == null) {
            return Collections.emptyList();
        }

        // 提取子命令参数（去掉子命令名称本身）
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        return subCommand.tabComplete(sender, alias, subArgs);
    }
}
