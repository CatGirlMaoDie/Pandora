package org.gsn.pandora.commands;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 命令系统通用工具类
 */
public class CommandUtils {

    /**
     * 过滤以指定前缀开头的选项（不区分大小写）
     *
     * @param options 选项列表
     * @param prefix 前缀
     * @return 匹配的选项列表
     */
    public static List<String> filterPrefix(List<String> options, String prefix) {
        String lower = prefix.toLowerCase();
        return options.stream()
                .filter(s -> s.toLowerCase().startsWith(lower))
                .collect(Collectors.toList());
    }
}
