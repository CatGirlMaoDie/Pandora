package org.gsn.pandora.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

/**
 * 统一管理 Pandora 的各类 YAML 配置：
 * - 插件主配置 config.yml（通过 JavaPlugin 自带的 config 系统）
 * - mobs/ skills/ spawners/ 目录下的所有 .yml 文件（合并加载）
 */
public class PandoraConfigService {

    private final JavaPlugin plugin;

    private FileConfiguration pluginConfig;
    private FileConfiguration mobsConfig;
    private FileConfiguration skillsConfig;
    private FileConfiguration spawnersConfig;

    public PandoraConfigService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 初次加载或重载所有配置文件。
     */
    public void reloadAll() {
        reloadPluginConfig();
        reloadMobsConfig();
        reloadSkillsConfig();
        reloadSpawnersConfig();
    }

    public void reloadPluginConfig() {
        plugin.reloadConfig();
        this.pluginConfig = plugin.getConfig();
    }

    public void reloadMobsConfig() {
        this.mobsConfig = loadDirectory("mobs");
    }

    public void reloadSkillsConfig() {
        this.skillsConfig = loadDirectory("skills");
    }

    public void reloadSpawnersConfig() {
        this.spawnersConfig = loadDirectory("spawners");
    }

    public FileConfiguration getPluginConfig() {
        return pluginConfig != null ? pluginConfig : plugin.getConfig();
    }

    public FileConfiguration getMobsConfig() {
        return mobsConfig;
    }

    public FileConfiguration getSkillsConfig() {
        return skillsConfig;
    }

    public FileConfiguration getSpawnersConfig() {
        return spawnersConfig;
    }

    /**
     * 扫描指定子目录下所有 .yml 文件，将其内容合并到一个 FileConfiguration 中返回。
     */
    private FileConfiguration loadDirectory(String dirName) {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            plugin.getLogger().log(Level.WARNING, "无法创建插件数据目录: " + dataFolder.getAbsolutePath());
        }

        File dir = new File(dataFolder, dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        YamlConfiguration merged = new YamlConfiguration();

        File[] files = dir.listFiles((d, name) -> name.endsWith(".yml"));
        if (files == null || files.length == 0) {
            plugin.getLogger().info(dirName + "/ 目录下没有找到任何 .yml 文件。");
            return merged;
        }

        for (File file : files) {
            try {
                YamlConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
                for (String key : fileConfig.getKeys(false)) {
                    if (merged.contains(key)) {
                        plugin.getLogger().warning("配置键 '" + key + "' 在 " + dirName + "/" + file.getName() + " 中重复定义，已跳过。");
                        continue;
                    }
                    merged.set(key, fileConfig.get(key));
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "加载配置文件失败: " + file.getName(), e);
            }
        }

        return merged;
    }
}
