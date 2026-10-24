package org.gsn.pandora.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * 子命令接口，所有 Pandora 子命令都需要实现此接口
 */
public interface SubCommand {

    /**
     * 执行子命令
     *
     * @param sender 命令发送者
     * @param label 命令标签
     * @param args 参数（不包含子命令名称本身）
     * @return 是否成功执行
     */
    boolean execute(CommandSender sender, String label, String[] args);

    /**
     * Tab 补全
     *
     * @param sender 命令发送者
     * @param alias 命令别名
     * @param args 参数（不包含子命令名称本身）
     * @return 补全列表
     */
    List<String> tabComplete(CommandSender sender, String alias, String[] args);

    /**
     * 获取子命令所需权限
     *
     * @return 权限节点，null 表示不需要权限
     */
    String getPermission();

    /**
     * 获取子命令的简短描述
     *
     * @return 描述文本
     */
    String getDescription();

    /**
     * 获取用法说明
     *
     * @param label 主命令标签
     * @return 用法文本
     */
    String getUsage(String label);
}
