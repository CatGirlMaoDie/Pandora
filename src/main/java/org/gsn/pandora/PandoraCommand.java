package org.gsn.pandora;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PandoraCommand extends Command {

    private final JavaPlugin plugin;

    public PandoraCommand(JavaPlugin plugin) {
        super("pandora", "Pandora 主命令", "/pandora [reload|spawn|spawner|debug]",
                List.of("pd"));
        this.plugin = plugin;
        setPermission("pandora.admin");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender, label);
            return true;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "reload":
                if (!sender.hasPermission("pandora.admin")) {
                    sender.sendMessage("§c你没有权限执行此命令。");
                    return true;
                }

                if (plugin instanceof Pandora) {
                    Pandora pandora = (Pandora) plugin;
                    pandora.getConfigService().reloadAll();
                    if (pandora.getMobManager() != null) {
                        pandora.getMobManager().reload();
                    }
                    if (pandora.getSkillManager() != null) {
                        pandora.getSkillManager().reload();
                    }
                    if (pandora.getSpawnerManager() != null) {
                        pandora.getSpawnerManager().reload();
                    }
                    sender.sendMessage("§aPandora 配置、技能与刷怪点已重载完成。");
                } else {
                    sender.sendMessage("§c内部错误：主插件实例类型不正确。");
                }
                return true;
            case "spawn":
                if (!sender.hasPermission("pandora.admin")) {
                    sender.sendMessage("§c你没有权限执行此命令。");
                    return true;
                }

                if (!(sender instanceof org.bukkit.entity.Player)) {
                    sender.sendMessage("§c该命令只能由玩家执行。");
                    return true;
                }

                if (!(plugin instanceof Pandora)) {
                    sender.sendMessage("§c内部错误：主插件实例类型不正确。");
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage("§e用法：/" + label + " spawn <mobId> [数量]");
                    return true;
                }

                org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
                Pandora pandoraPlugin = (Pandora) plugin;
                if (pandoraPlugin.getMobManager() == null) {
                    sender.sendMessage("§cMobManager 尚未初始化。");
                    return true;
                }

                String mobId = args[1];
                int amount = 1;
                if (args.length >= 3) {
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException ignored) {
                        amount = 1;
                    }
                }
                if (amount <= 0) {
                    amount = 1;
                }

                int success = 0;
                for (int i = 0; i < amount; i++) {
                    org.bukkit.entity.LivingEntity spawned = pandoraPlugin.getMobManager().spawn(mobId, player.getLocation());
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
            case "spawner":
                if (!(plugin instanceof Pandora)) {
                    sender.sendMessage("§c内部错误：主插件实例类型不正确。");
                    return true;
                }
                org.gsn.pandora.spawner.SpawnerManager spawnerManager =
                    ((Pandora) plugin).getSpawnerManager();
                if (spawnerManager == null) {
                    sender.sendMessage("§cSpawnerManager 尚未初始化。");
                    return true;
                }
                org.gsn.pandora.spawner.SpawnerCommandHandler handler =
                    new org.gsn.pandora.spawner.SpawnerCommandHandler((Pandora) plugin, spawnerManager);
                return handler.handle(sender, label, args, 1);
            case "debug":
                if (!sender.hasPermission("pandora.admin")) {
                    sender.sendMessage("§c你没有权限执行此命令。");
                    return true;
                }
                if (!(plugin instanceof Pandora)) {
                    sender.sendMessage("§c内部错误：主插件实例类型不正确。");
                    return true;
                }
                org.gsn.pandora.mobs.MobManager mobManager = ((Pandora) plugin).getMobManager();
                if (mobManager == null) {
                    sender.sendMessage("§cMobManager 尚未初始化。");
                    return true;
                }
                if (!(sender instanceof org.bukkit.entity.Player)) {
                    sender.sendMessage("§c该命令只能由玩家执行。");
                    return true;
                }
                org.bukkit.entity.Player debugPlayer = (org.bukkit.entity.Player) sender;
                org.bukkit.entity.Entity target = debugPlayer.getTargetEntity(20);
                if (!(target instanceof org.bukkit.entity.LivingEntity)) {
                    sender.sendMessage("§e请看着一只实体再使用此命令。");
                    return true;
                }
                org.bukkit.entity.LivingEntity le = (org.bukkit.entity.LivingEntity) target;
                org.gsn.pandora.mobs.MobType mobType = mobManager.getMobTypeByEntity(le);
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
            default:
                sendHelp(sender, label);
                return true;
        }
    }

    private void sendHelp(CommandSender sender, String label) {
        sender.sendMessage("§6Pandora 指令帮助:");
        sender.sendMessage("§e/" + label + " reload §7- 重载 Pandora 配置");
        sender.sendMessage("§e/" + label + " spawn <mobId> [数量] §7- 生成自定义实体");
        sender.sendMessage("§e/" + label + " spawner ... §7- 管理刷怪点");
        sender.sendMessage("§e/" + label + " debug ... §7- 调试信息");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (!sender.hasPermission("pandora.admin")) return Collections.emptyList();

        // args[0] = 子命令
        if (args.length == 1) {
            return filterPrefix(List.of("reload", "spawn", "spawner", "debug"), args[0]);
        }

        String sub = args[0].toLowerCase();

        // /pandora spawn <mobId> [amount]
        if (sub.equals("spawn")) {
            if (args.length == 2 && plugin instanceof Pandora pandora && pandora.getMobManager() != null) {
                List<String> ids = pandora.getMobManager().getAllMobTypes()
                    .stream().map(t -> t.getId()).collect(java.util.stream.Collectors.toList());
                return filterPrefix(ids, args[1]);
            }
            return Collections.emptyList();
        }

        // /pandora spawner <sub> [id] ...
        if (sub.equals("spawner")) {
            if (args.length == 2) {
                return filterPrefix(List.of("list", "info", "tp", "teleport", "create", "remove"), args[1]);
            }
            String spawnerSub = args[1].toLowerCase();
            if (plugin instanceof Pandora pandora && pandora.getSpawnerManager() != null) {
                if ((spawnerSub.equals("info") || spawnerSub.equals("tp")
                        || spawnerSub.equals("teleport") || spawnerSub.equals("remove")) && args.length == 3) {
                    List<String> ids = pandora.getSpawnerManager().getAllSpawners()
                        .stream().map(d -> d.getId()).collect(java.util.stream.Collectors.toList());
                    return filterPrefix(ids, args[2]);
                }
                if (spawnerSub.equals("create") && args.length == 4
                        && pandora.getMobManager() != null) {
                    List<String> ids = pandora.getMobManager().getAllMobTypes()
                        .stream().map(t -> t.getId()).collect(java.util.stream.Collectors.toList());
                    return filterPrefix(ids, args[3]);
                }
            }
            return Collections.emptyList();
        }

        return Collections.emptyList();
    }

    private List<String> filterPrefix(List<String> options, String prefix) {
        String lower = prefix.toLowerCase();
        List<String> result = new ArrayList<>();
        for (String s : options) {
            if (s.toLowerCase().startsWith(lower)) result.add(s);
        }
        return result;
    }
}
