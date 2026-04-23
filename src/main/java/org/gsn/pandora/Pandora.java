package org.gsn.pandora;

import org.bukkit.plugin.java.JavaPlugin;
import org.gsn.pandora.config.PandoraConfigService;
import org.gsn.pandora.listener.EntityListener;
import org.gsn.pandora.mobs.MobManager;
import org.gsn.pandora.skills.SkillManager;

public final class Pandora extends JavaPlugin {

    private PandoraConfigService configService;
    private MobManager mobManager;
    private SkillManager skillManager;
    private org.gsn.pandora.spawner.SpawnerManager spawnerManager;
    private org.gsn.pandora.script.ScriptingService scriptingService;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // 保存默认配置文件
        saveDefaultConfig();
        saveDefaultDirectoryResource("mobs", "example_mob.yml");
        saveDefaultDirectoryResource("skills", "example_skill.yml");
        saveDefaultDirectoryResource("spawners", "example_spawner.yml");

        // 初始化配置服务
        this.configService = new PandoraConfigService(this);
        this.configService.reloadAll();

        // 初始化脚本服务
        boolean jsEnabled = getConfig().getBoolean("javascript.enabled", true);
        org.gsn.pandora.script.ScriptEngineAdapter adapter =
            new org.gsn.pandora.script.NashornScriptEngineAdapter(this);
        this.scriptingService = new org.gsn.pandora.script.ScriptingService(this, adapter, jsEnabled);

        // 初始化 MobManager
        this.mobManager = new MobManager(this, this.configService);
        this.mobManager.reload();

        // 初始化 SkillManager
        this.skillManager = new SkillManager(this, this.configService, this.scriptingService);
        this.skillManager.reload();

        // 初始化 SpawnerManager
        this.spawnerManager = new org.gsn.pandora.spawner.SpawnerManager(this, this.configService, this.mobManager, this.scriptingService);
        this.spawnerManager.reload();
        this.spawnerManager.startTask();

        // 注册事件监听器
        getServer().getPluginManager().registerEvents(
            new EntityListener(this, mobManager, skillManager), this
        );

        // 注册主命令
        PandoraCommand pandoraCmd = new PandoraCommand(this);
        getServer().getCommandMap().register("pandora", pandoraCmd);
    }

    public PandoraConfigService getConfigService() {
        return configService;
    }

    public MobManager getMobManager() {
        return mobManager;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public org.gsn.pandora.spawner.SpawnerManager getSpawnerManager() {
        return spawnerManager;
    }

    public org.gsn.pandora.script.ScriptingService getScriptingService() {
        return scriptingService;
    }

    @Override
    public void onDisable() {
        if (spawnerManager != null) {
            spawnerManager.stopTask();
        }
    }

    /**
     * 若目录下不存在该文件，则从 jar 内复制默认示例文件到对应子目录。
     */
    private void saveDefaultDirectoryResource(String dirName, String fileName) {
        java.io.File dir = new java.io.File(getDataFolder(), dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        java.io.File target = new java.io.File(dir, fileName);
        if (!target.exists()) {
            saveResource(dirName + "/" + fileName, false);
        }
    }
}
