package org.gsn.pandora.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

/**
 * 统一管理 Pandora 的各类 YAML 配置：
 * - 插件主配置 config.yml（通过 JavaPlugin 自带的 config 系统）
 * - mobs.yml / skills.yml / spawners.yml
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
        this.mobsConfig = loadYaml("mobs.yml");
    }

    public void reloadSkillsConfig() {
        this.skillsConfig = loadYaml("skills.yml");
    }

    public void reloadSpawnersConfig() {
        this.spawnersConfig = loadYaml("spawners.yml");
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

    private FileConfiguration loadYaml(String fileName) {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            plugin.getLogger().log(Level.WARNING, "无法创建插件数据目录: " + dataFolder.getAbsolutePath());
        }

        File file = new File(dataFolder, fileName);
        if (!file.exists()) {
            // 从 jar 里拷贝默认配置
            plugin.saveResource(fileName, false);
        }

        return YamlConfiguration.loadConfiguration(file);
    }
}
