package org.gsn.pandora.mobs;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsn.pandora.config.PandoraConfigService;
import org.gsn.pandora.skills.TriggerType;
import org.gsn.pandora.util.EntityUtil;

import java.util.*;
import java.util.logging.Level;

/**
 * 负责加载 mobs.yml 并在世界中生成 Pandora 自定义实体。
 */
public class MobManager {

    private final JavaPlugin plugin;
    private final PandoraConfigService configService;
    private final NamespacedKey mobIdKey;

    private final Map<String, MobType> mobTypes = new HashMap<>();

    public MobManager(JavaPlugin plugin, PandoraConfigService configService) {
        this.plugin = plugin;
        this.configService = configService;
        this.mobIdKey = new NamespacedKey(plugin, "mob-id");
    }

    public void reload() {
        mobTypes.clear();

        FileConfiguration config = configService.getMobsConfig();
        if (config == null) {
            plugin.getLogger().warning("mobs.yml 尚未加载，无法构建 MobType。");
            return;
        }

        for (String id : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(id);
            if (section == null) {
                continue;
            }

            String typeName = section.getString("Type", "ZOMBIE");
            EntityType entityType;
            try {
                entityType = EntityType.valueOf(typeName.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                // 先按原版 ID 解析失败，再尝试按英文名/中文名匹配
                entityType = EntityUtil.matchEntityTypeByNameOrChinese(typeName);
                if (entityType == null) {
                    plugin.getLogger().log(Level.WARNING, "mobs.yml 中 " + id + " 的 Type 无效: " + typeName, ex);
                    continue;
                }
            }

            String display = section.getString("Display");
            String faction = section.getString("Faction");
            double health = section.getDouble("Health", 20.0);
            double damage = section.getDouble("Damage", 2.0);
            double armor = section.getDouble("Armor", 0.0);


            ConfigurationSection optionsSection = section.getConfigurationSection("Options");
            boolean silent = false;
            Boolean preventOtherDrops = null;
            Boolean despawn = null;
            Double movementSpeed = null;
            Boolean noAI = null;
            Boolean glowing = null;
            Boolean invisible = null;
            Boolean hasGravity = null;
            Boolean collidable = null;
            Double followRange = null;
            Double knockbackResistance = null;
            Double attackSpeed = null;
            Map<TriggerType, List<String>> triggerSkills = new EnumMap<>(TriggerType.class);

            if (optionsSection != null) {
                silent = optionsSection.getBoolean("Silent", false);
                if (optionsSection.contains("PreventOtherDrops")) {
                    preventOtherDrops = optionsSection.getBoolean("PreventOtherDrops");
                }
                if (optionsSection.contains("Despawn")) {
                    despawn = optionsSection.getBoolean("Despawn");
                }
                if (optionsSection.contains("MovementSpeed")) {
                    movementSpeed = optionsSection.getDouble("MovementSpeed");
                }
                if (optionsSection.contains("NoAI")) {
                    noAI = optionsSection.getBoolean("NoAI");
                }
                if (optionsSection.contains("Glowing")) {
                    glowing = optionsSection.getBoolean("Glowing");
                }
                if (optionsSection.contains("Invisible")) {
                    invisible = optionsSection.getBoolean("Invisible");
                }
                if (optionsSection.contains("HasGravity")) {
                    hasGravity = optionsSection.getBoolean("HasGravity");
                }
                if (optionsSection.contains("Collidable")) {
                    collidable = optionsSection.getBoolean("Collidable");
                }
                if (optionsSection.contains("FollowRange")) {
                    followRange = optionsSection.getDouble("FollowRange");
                }
                if (optionsSection.contains("KnockbackResistance")) {
                    knockbackResistance = optionsSection.getDouble("KnockbackResistance");
                }
                if (optionsSection.contains("AttackSpeed")) {
                    attackSpeed = optionsSection.getDouble("AttackSpeed");
                }
            }

            // 解析每个触发器绑定的技能列表
            ConfigurationSection skillsSection = section.getConfigurationSection("Skills");
            if (skillsSection != null) {
                for (String triggerKey : skillsSection.getKeys(false)) {
                    TriggerType triggerType = TriggerType.fromConfigString(triggerKey);
                    if (triggerType == null) {
                        plugin.getLogger().warning("mobs.yml 中 " + id + " 的 Skills 节点存在未知触发器: " + triggerKey);
                        continue;
                    }
                    List<String> rawList = skillsSection.getStringList(triggerKey);
                    if (rawList == null || rawList.isEmpty()) {
                        continue;
                    }
                    List<String> skillNames = new ArrayList<>();
                    for (String rawSkill : rawList) {
                        if (rawSkill == null) {
                            continue;
                        }
                        String trimmed = rawSkill.trim();
                        if (!trimmed.isEmpty()) {
                            skillNames.add(trimmed);
                        }
                    }
                    if (!skillNames.isEmpty()) {
                        triggerSkills.put(triggerType, skillNames);
                    }
                }
            }

            MobType.MobOptions options = new MobType.MobOptions(
                silent,
                preventOtherDrops,
                despawn,
                movementSpeed,
                noAI,
                glowing,
                invisible,
                hasGravity,
                collidable,
                followRange,
                knockbackResistance,
                attackSpeed
            );

            String key = id.toLowerCase(Locale.ROOT);
            MobType mobType = new MobType(id, entityType, display, faction, health, damage, armor, options, triggerSkills);
            mobTypes.put(key, mobType);
        }

        plugin.getLogger().info("已加载 " + mobTypes.size() + " 个自定义 Mob 类型。");
    }

    public MobType getMobType(String id) {
        if (id == null) {
            return null;
        }
        return mobTypes.get(id.toLowerCase(Locale.ROOT));
    }

    public MobType getMobTypeByEntity(Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            return null;
        }
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        String id = pdc.get(mobIdKey, PersistentDataType.STRING);
        if (id == null) {
            return null;
        }
        return getMobType(id);
    }

    /**
     * 在指定位置生成指定 ID 的 Mob。
     *
     * @return 生成成功的实体，失败返回 null
     */
    public LivingEntity spawn(String id, Location location) {
        MobType mobType = getMobType(id);
        if (mobType == null) {
            return null;
        }
        return spawn(mobType, location);
    }

    public LivingEntity spawn(MobType mobType, Location location) {
        if (location == null) {
            return null;
        }

        World world = location.getWorld();
        if (world == null) {
            return null;
        }

        Entity entity = world.spawnEntity(location, mobType.getEntityType());
        if (!(entity instanceof LivingEntity)) {
            entity.remove();
            plugin.getLogger().warning("尝试为 MobType " + mobType.getId() + " 生成的实体不是 LivingEntity，已移除。");
            return null;
        }

        LivingEntity living = (LivingEntity) entity;
        // 应用静态属性
        mobType.applyTo(living);

        // 标记为 Pandora Mob
        PersistentDataContainer pdc = living.getPersistentDataContainer();
        pdc.set(mobIdKey, PersistentDataType.STRING, mobType.getId());

        return living;
    }

    public boolean isPandoraMob(Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            return false;
        }
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        return pdc.has(mobIdKey, PersistentDataType.STRING);
    }
}
