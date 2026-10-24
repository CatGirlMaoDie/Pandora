package org.gsn.pandora.skills.mechanics;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;
import java.util.Locale;

/**
 * 变量机制（对应 MythicMobs 的 variableset）。
 * variable|var|v|name=<名字>  value|val=<值>  type|t=STRING/INTEGER/DOUBLE/BOOLEAN  scope|s=SKULL/全局
 * 实体变量存储在实体 PDC；全局变量存储在全局静态 Map（供 variable 条件与占位符使用）。
 */
public class VariableSetMechanic implements Mechanic {

    public static final NamespacedKey VAR_PREFIX = new NamespacedKey("pandora", "var");

    private final String name;
    private final String value;
    private final String type;
    private final boolean global;

    public VariableSetMechanic(String name, String value, String type, boolean global) {
        this.name = name == null ? "" : name;
        this.value = value == null ? "" : value;
        this.type = type == null ? "STRING" : type;
        this.global = global;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (name.isEmpty()) return false;
        boolean any = false;
        if (global) {
            GlobalVariables.set(name, value, type);
            any = true;
        }
        if (targets != null) {
            for (LivingEntity target : targets) {
                if (target == null || target.isDead()) continue;
                setEntityVariable(target, name, value, type);
                any = true;
            }
        }
        // 无目标时默认对施法者生效
        if (!global && (targets == null || targets.isEmpty()) && context.getCaster() != null) {
            setEntityVariable(context.getCaster(), name, value, type);
            any = true;
        }
        return any;
    }

    public static void setEntityVariable(LivingEntity entity, String name, String value, String type) {
        if (entity == null || name == null || name.isEmpty()) return;
        String keyName = name.toLowerCase(Locale.ROOT);
        NamespacedKey key = new NamespacedKey("pandora", "var_" + keyName);
        String t = type == null ? "STRING" : type.toUpperCase(Locale.ROOT);
        switch (t) {
            case "INTEGER", "INT" -> {
                try { entity.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, Integer.parseInt(value)); }
                catch (NumberFormatException ignored) {}
            }
            case "DOUBLE", "FLOAT", "NUMERIC" -> {
                try { entity.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, Double.parseDouble(value)); }
                catch (NumberFormatException ignored) {}
            }
            case "BOOLEAN", "BOOL" ->
                entity.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) (Boolean.parseBoolean(value) ? 1 : 0));
            default -> entity.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        }
    }

    public static String getEntityVariable(LivingEntity entity, String name) {
        if (entity == null || name == null) return null;
        NamespacedKey key = new NamespacedKey("pandora", "var_" + name.toLowerCase(Locale.ROOT));
        var pdc = entity.getPersistentDataContainer();
        if (pdc.has(key, PersistentDataType.STRING)) return pdc.get(key, PersistentDataType.STRING);
        if (pdc.has(key, PersistentDataType.INTEGER)) return String.valueOf(pdc.get(key, PersistentDataType.INTEGER));
        if (pdc.has(key, PersistentDataType.DOUBLE)) return String.valueOf(pdc.get(key, PersistentDataType.DOUBLE));
        if (pdc.has(key, PersistentDataType.BYTE)) {
            byte b = pdc.get(key, PersistentDataType.BYTE);
            return b != 0 ? "true" : "false";
        }
        return null;
    }

    /** 全局变量存储 */
    public static class GlobalVariables {
        private static final java.util.Map<String, String> GLOBALS = new java.util.concurrent.ConcurrentHashMap<>();

        public static void set(String name, String value, String type) {
            if (name == null) return;
            String t = type == null ? "STRING" : type.toUpperCase(Locale.ROOT);
            String v = value == null ? "" : value;
            switch (t) {
                case "INTEGER", "INT", "DOUBLE", "FLOAT", "NUMERIC", "BOOLEAN", "BOOL" -> {
                    // 归一化布尔
                    if (t.startsWith("BOOL")) v = Boolean.parseBoolean(v) ? "true" : "false";
                }
            }
            GLOBALS.put(name.toLowerCase(Locale.ROOT), v);
        }

        public static String get(String name) {
            return name == null ? null : GLOBALS.get(name.toLowerCase(Locale.ROOT));
        }

        public static void remove(String name) {
            if (name != null) GLOBALS.remove(name.toLowerCase(Locale.ROOT));
        }
    }
}
