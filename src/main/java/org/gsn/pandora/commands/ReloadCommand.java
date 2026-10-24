package org.gsn.pandora.commands;

import org.bukkit.command.CommandSender;
import org.gsn.pandora.Pandora;

import java.util.Collections;
import java.util.List;

public class ReloadCommand implements SubCommand {

    private final Pandora plugin;

    public ReloadCommand(Pandora plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        plugin.getConfigService().reloadAll();
//        if (plugin.getMobManager() != null) {
//            plugin.getMobManager().reload();
//        }
//        if (plugin.getSkillManager() != null) {
//            plugin.getSkillManager().reload();
//        }
//        if (plugin.getSpawnerManager() != null) {
//            plugin.getSpawnerManager().reload();
//        }
        sender.sendMessage("§aPandora 配置、技能与刷怪点已重载完成。");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getPermission() {
        return "pandora.reload";
    }

    @Override
    public String getDescription() {
        return "重载 Pandora 配置";
    }

    @Override
    public String getUsage(String label) {
        return "/" + label + " reload";
    }
}
