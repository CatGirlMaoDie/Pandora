package org.gsn.pandora.spawner;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsn.pandora.Pandora;

/**
 * 处理 /pandora spawner 子命令的简单实现：只做 list 占位和基本信息查看。
 */
public class SpawnerCommandHandler {

    private final Pandora plugin;
    private final SpawnerManager spawnerManager;

    public SpawnerCommandHandler(Pandora plugin, SpawnerManager spawnerManager) {
        this.plugin = plugin;
        this.spawnerManager = spawnerManager;
    }

    public boolean handle(CommandSender sender, String label, String[] args, int offset) {
        if (!sender.hasPermission("pandora.admin")) {
            sender.sendMessage("§c你没有权限执行此命令。");
            return true;
        }

        if (args.length <= offset) {
            sendHelp(sender, label);
            return true;
        }

        String sub = args[offset].toLowerCase();
        switch (sub) {
            case "list":
                sender.sendMessage("§6当前已加载的刷怪点：");
                if (spawnerManager.getAllSpawners().isEmpty()) {
                    sender.sendMessage("§7(无)");
                    return true;
                }
                spawnerManager.getAllSpawners().forEach(def -> {
                    sender.sendMessage("§e" + def.getId() + " §7-> Mob=§f" + def.getMobId()
                        + " §7World=§f" + def.getWorldName()
                        + " §7Radius=§f" + def.getRadius()
                        + " §7Max=§f" + def.getMaxMobs());
                });
                return true;
            case "info":
                if (args.length <= offset + 1) {
                    sender.sendMessage("§e用法：/" + label + " spawner info <id>");
                    return true;
                }
                String id = args[offset + 1];
                SpawnerDefinition def = spawnerManager.getSpawner(id);
                if (def == null) {
                    sender.sendMessage("§c未找到刷怪点：" + id);
                    return true;
                }
                sender.sendMessage("§6刷怪点详情：" + def.getId());
                sender.sendMessage("§7Mob: §f" + def.getMobId());
                sender.sendMessage("§7World: §f" + def.getWorldName());
                sender.sendMessage("§7Radius: §f" + def.getRadius());
                sender.sendMessage("§7MaxMobs: §f" + def.getMaxMobs());
                sender.sendMessage("§7IntervalTicks: §f" + def.getIntervalTicks());
                Location loc = def.getLocation();
                if (loc != null) {
                    sender.sendMessage("§7Location: §f" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                }
                return true;
            case "teleport":
            case "tp":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§c该命令只能由玩家执行。");
                    return true;
                }
                if (args.length <= offset + 1) {
                    sender.sendMessage("§e用法：/" + label + " spawner tp <id>");
                    return true;
                }
                String tpId = args[offset + 1];
                SpawnerDefinition tpDef = spawnerManager.getSpawner(tpId);
                if (tpDef == null) {
                    sender.sendMessage("§c未找到刷怪点：" + tpId);
                    return true;
                }
                Location tpLoc = tpDef.getLocation();
                if (tpLoc == null) {
                    sender.sendMessage("§c刷怪点世界尚未加载：" + tpDef.getWorldName());
                    return true;
                }
                ((Player) sender).teleport(tpLoc);
                sender.sendMessage("§a已传送到刷怪点：" + tpDef.getId());
                return true;
            case "create":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§c该命令只能由玩家执行。");
                    return true;
                }
                if (args.length <= offset + 2) {
                    sender.sendMessage("§e用法：/" + label + " spawner create <id> <mobId> [radius] [interval]");
                    return true;
                }
                String newId = args[offset + 1];
                String mobId = args[offset + 2];
                double radius = 10.0;
                if (args.length > offset + 3) {
                    try {
                        radius = Double.parseDouble(args[offset + 3]);
                    } catch (NumberFormatException ignored) {
                    }
                }
                long intervalTicks = spawnerManager.parseInterval("10s");
                if (args.length > offset + 4) {
                    intervalTicks = spawnerManager.parseInterval(args[offset + 4]);
                }
                Player player = (Player) sender;
                SpawnerDefinition created = spawnerManager.createSpawner(newId, mobId, player.getLocation(), radius, 5, intervalTicks);
                if (created == null) {
                    sender.sendMessage("§c创建刷怪点失败，请检查参数。");
                } else {
                    sender.sendMessage("§a已在你的位置创建刷怪点：" + created.getId());
                }
                return true;
            case "remove":
                if (args.length <= offset + 1) {
                    sender.sendMessage("§e用法：/" + label + " spawner remove <id>");
                    return true;
                }
                String removeId = args[offset + 1];
                if (spawnerManager.removeSpawner(removeId)) {
                    sender.sendMessage("§a已移除刷怪点：" + removeId);
                } else {
                    sender.sendMessage("§c未找到刷怪点：" + removeId);
                }
                return true;
            default:
                sendHelp(sender, label);
                return true;
        }
    }

    private void sendHelp(CommandSender sender, String label) {
        sender.sendMessage("§6刷怪点子命令：");
        sender.sendMessage("§e/" + label + " spawner list §7- 列出所有刷怪点");
        sender.sendMessage("§e/" + label + " spawner info <id> §7- 查看刷怪点详情");
        sender.sendMessage("§e/" + label + " spawner tp <id> §7- 传送到刷怪点位置");
        sender.sendMessage("§e/" + label + " spawner create <id> <mobId> [radius] [interval] §7- 在当前位置创建刷怪点");
        sender.sendMessage("§e/" + label + " spawner remove <id> §7- 移除刷怪点");
    }
}
