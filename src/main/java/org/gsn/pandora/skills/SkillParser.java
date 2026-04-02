package org.gsn.pandora.skills;

import java.util.*;

/**
 * 负责把一行字符串解析成 SkillLine。
 */
public final class SkillParser {

    private SkillParser() {
    }

    public static SkillLine parseLine(String raw) {
        if (raw == null) {
            return null;
        }
        String line = raw.trim();
        if (line.isEmpty() || line.startsWith("#")) {
            return null;
        }

        String mechanicName;
        Map<String, String> mechanicArgs = Collections.emptyMap();
        String targeterName = null;
        Map<String, String> targeterArgs = Collections.emptyMap();
        TriggerType triggerType = null;
        List<SkillLine.ConditionSpec> conditions = new ArrayList<>();

        // 1. 解析 mechanicName 和 {} 参数
        int firstSpace = line.indexOf(' ');
        int braceIndex = line.indexOf('{');
        int endIndex = line.length();
        if (braceIndex != -1 && (firstSpace == -1 || braceIndex < firstSpace)) {
            mechanicName = line.substring(0, braceIndex).trim();
            int close = line.indexOf('}', braceIndex);
            if (close == -1) {
                close = line.length();
            }
            String argsPart = line.substring(braceIndex + 1, Math.min(close, line.length()));
            mechanicArgs = parseArgs(argsPart);
            if (close + 1 < line.length()) {
                line = line.substring(close + 1).trim();
            } else {
                line = "";
            }
        } else {
            if (firstSpace == -1) {
                mechanicName = line;
                line = "";
            } else {
                mechanicName = line.substring(0, firstSpace).trim();
                line = line.substring(firstSpace + 1).trim();
            }
        }

        // 2. 其余部分用空格分割，识别 @targeter / ~trigger / ?condition
        if (!line.isEmpty()) {
            String[] parts = line.split("\\s+");
            for (String part : parts) {
                if (part.startsWith("@")) {
                    String spec = part.substring(1);
                    String name;
                    Map<String, String> args = Collections.emptyMap();
                    int idx = spec.indexOf('{');
                    if (idx != -1 && spec.endsWith("}")) {
                        name = spec.substring(0, idx);
                        String inside = spec.substring(idx + 1, spec.length() - 1);
                        args = parseArgs(inside);
                    } else {
                        name = spec;
                    }
                    targeterName = name;
                    targeterArgs = args;
                } else if (part.startsWith("~")) {
                    triggerType = TriggerType.fromConfigString(part);
                } else if (part.startsWith("?")) {
                    String spec = part.substring(1);
                    String name;
                    Map<String, String> args = Collections.emptyMap();
                    int idx = spec.indexOf('{');
                    if (idx != -1 && spec.endsWith("}")) {
                        name = spec.substring(0, idx);
                        String inside = spec.substring(idx + 1, spec.length() - 1);
                        args = parseArgs(inside);
                    } else {
                        name = spec;
                    }
                    conditions.add(new SkillLine.ConditionSpec(name, args));
                }
            }
        }

        return new SkillLine(mechanicName, mechanicArgs, targeterName, targeterArgs, triggerType, conditions);
    }

    /**
     * 把形如 "a=5;b=test" 的片段解析为 Map。
     */
    public static Map<String, String> parseArgs(String raw) {
        Map<String, String> result = new HashMap<>();
        if (raw == null || raw.trim().isEmpty()) {
            return result;
        }
        String[] parts = raw.split(";");
        for (String part : parts) {
            String s = part.trim();
            if (s.isEmpty()) {
                continue;
            }
            int eq = s.indexOf('=');
            if (eq == -1) {
                result.put(s, "true");
            } else {
                String key = s.substring(0, eq).trim();
                String value = s.substring(eq + 1).trim();
                // 去掉首尾引号
                if (value.length() >= 2 && ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'")))) {
                    value = value.substring(1, value.length() - 1);
                }
                result.put(key, value);
            }
        }
        return result;
    }
}
