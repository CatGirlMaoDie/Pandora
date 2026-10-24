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
 * 负责加载 mobs/ 目录配置并在世界中生成 Pandora 自定义实体。
 */
public class MobManager {

    private final JavaPlugin plugin;
    private final PandoraConfigService configService;
    private final NamespacedKey mobIdKey;
    static final NamespacedKey FACTION_KEY = new NamespacedKey("pandora", "faction");

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
            plugin.getLogger().warning("mobs 配置尚未加载，无法构建 MobType。");
            return;
        }

        for (String id : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(id);
            if (section == null) continue;

            String typeName = section.getString("Type", "ZOMBIE");
            EntityType entityType;
            try {
                entityType = EntityType.valueOf(typeName.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                entityType = EntityUtil.matchEntityTypeByNameOrChinese(typeName);
                if (entityType == null) {
                    plugin.getLogger().log(Level.WARNING, "mobs 中 " + id + " 的 Type 无效: " + typeName, ex);
                    continue;
                }
            }

            String display = section.getString("Display");
            String faction = section.getString("Faction");
            double health = section.getDouble("Health", 20.0);
            double damage = section.getDouble("Damage", 2.0);
            double armor  = section.getDouble("Armor", 0.0);

            // --- Options ---
            ConfigurationSection optionsSection = section.getConfigurationSection("Options");
            boolean silent = false;
            Boolean preventOtherDrops = null, despawn = null, noAI = null;
            Boolean glowing = null, invisible = null, hasGravity = null, collidable = null;
            Double movementSpeed = null, followRange = null, knockbackResistance = null, attackSpeed = null;

            if (optionsSection != null) {
                silent = optionsSection.getBoolean("Silent", false);
                if (optionsSection.contains("PreventOtherDrops"))   preventOtherDrops   = optionsSection.getBoolean("PreventOtherDrops");
                if (optionsSection.contains("Despawn"))             despawn             = optionsSection.getBoolean("Despawn");
                if (optionsSection.contains("MovementSpeed"))       movementSpeed       = optionsSection.getDouble("MovementSpeed");
                if (optionsSection.contains("NoAI"))                noAI                = optionsSection.getBoolean("NoAI");
                if (optionsSection.contains("Glowing"))             glowing             = optionsSection.getBoolean("Glowing");
                if (optionsSection.contains("Invisible"))           invisible           = optionsSection.getBoolean("Invisible");
                if (optionsSection.contains("HasGravity"))          hasGravity          = optionsSection.getBoolean("HasGravity");
                if (optionsSection.contains("Collidable"))          collidable          = optionsSection.getBoolean("Collidable");
                if (optionsSection.contains("FollowRange"))         followRange         = optionsSection.getDouble("FollowRange");
                if (optionsSection.contains("KnockbackResistance")) knockbackResistance = optionsSection.getDouble("KnockbackResistance");
                if (optionsSection.contains("AttackSpeed"))         attackSpeed         = optionsSection.getDouble("AttackSpeed");
            }

            // --- Skills ---
            Map<TriggerType, List<String>> triggerSkills = new EnumMap<>(TriggerType.class);
            ConfigurationSection skillsSection = section.getConfigurationSection("Skills");
            if (skillsSection != null) {
                for (String triggerKey : skillsSection.getKeys(false)) {
                    TriggerType triggerType = TriggerType.fromConfigString(triggerKey);
                    if (triggerType == null) {
                        plugin.getLogger().warning("mobs 中 " + id + " 的 Skills 节点存在未知触发器: " + triggerKey);
                        continue;
                    }
                    List<String> skillNames = new ArrayList<>();
                    for (String rawSkill : skillsSection.getStringList(triggerKey)) {
                        if (rawSkill != null && !rawSkill.trim().isEmpty()) skillNames.add(rawSkill.trim());
                    }
                    if (!skillNames.isEmpty()) triggerSkills.put(triggerType, skillNames);
                }
            }

            // --- Drops ---
            List<MobDrop> drops = new ArrayList<>();
            for (String rawDrop : section.getStringList("Drops")) {
                MobDrop drop = MobDrop.parse(rawDrop);
                if (drop != null) drops.add(drop);
                else plugin.getLogger().warning("mobs 中 " + id + " 的 Drops 条目无法解析: " + rawDrop);
            }

            // --- Equipment ---
            MobEquipment equipment = null;
            ConfigurationSection eqSection = section.getConfigurationSection("Equipment");
            if (eqSection != null) {
                equipment = new MobEquipment(
                    MobEquipment.parseMaterial(eqSection.getString("MainHand")),
                    MobEquipment.parseMaterial(eqSection.getString("OffHand")),
                    MobEquipment.parseMaterial(eqSection.getString("Helmet")),
                    MobEquipment.parseMaterial(eqSection.getString("Chestplate")),
                    MobEquipment.parseMaterial(eqSection.getString("Leggings")),
                    MobEquipment.parseMaterial(eqSection.getString("Boots")),
                    (float) eqSection.getDouble("MainHandDropChance", 0.0),
                    (float) eqSection.getDouble("OffHandDropChance", 0.0),
                    (float) eqSection.getDouble("HelmetDropChance", 0.0),
                    (float) eqSection.getDouble("ChestplateDropChance", 0.0),
                    (float) eqSection.getDouble("LeggingsDropChance", 0.0),
                    (float) eqSection.getDouble("BootsDropChance", 0.0)
                );
            }

            // --- Timer ---
            long timerIntervalTicks = 0L;
            if (section.contains("Timer")) {
                timerIntervalTicks = parseInterval(section.getString("Timer", "0"));
            }

            // --- 威胁表 / 等级 / 伤害调整 ---
            boolean threatTable = false;
            int level = 0;
            Map<String, Double> damageModifiers = new LinkedHashMap<>();
            ConfigurationSection dmgModSection = section.getConfigurationSection("DamageModifiers");
            if (dmgModSection != null) {
                for (String cause : dmgModSection.getKeys(false)) {
                    double mult = dmgModSection.getDouble(cause, 1.0);
                    damageModifiers.put(cause.toUpperCase(Locale.ROOT), mult);
                }
            }
            if (section.contains("ThreatTable")) threatTable = section.getBoolean("ThreatTable", false);
            if (section.contains("Level")) level = Math.max(0, section.getInt("Level", 0));

            // --- AI Script ---
            String aiScript = null;
            String aiFunction = "decideSkills";
            ConfigurationSection aiSection = section.getConfigurationSection("AI");
            if (aiSection != null) {
                aiScript = aiSection.getString("Script");
                String func = aiSection.getString("Function");
                if (func != null && !func.isEmpty()) aiFunction = func;
            }

            MobType.MobOptions options = new MobType.MobOptions(
                silent, preventOtherDrops, despawn, movementSpeed,
                noAI, glowing, invisible, hasGravity, collidable,
                followRange, knockbackResistance, attackSpeed
            );

            MobType mobType = new MobType(id, entityType, display, faction, health, damage, armor,
                options, triggerSkills, drops, equipment, timerIntervalTicks, aiScript, aiFunction,
                threatTable, level, damageModifiers);
            mobTypes.put(id.toLowerCase(Locale.ROOT), mobType);
        }

        plugin.getLogger().info("已加载 " + mobTypes.size() + " 个自定义 Mob 类型。");
    }

    private long parseInterval(String raw) {
        if (raw == null || raw.isBlank()) return 0L;
        String s = raw.trim().toLowerCase(Locale.ROOT);
        try {
            if (s.endsWith("ms")) return Math.max(1L, Long.parseLong(s.substring(0, s.length() - 2)) / 50L);
            if (s.endsWith("s"))  return Math.max(1L, Long.parseLong(s.substring(0, s.length() - 1)) * 20L);
            return Math.max(1L, Long.parseLong(s));
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public MobType getMobType(String id) {
        if (id == null) return null;
        return mobTypes.get(id.toLowerCase(Locale.ROOT));
    }

    public MobType getMobTypeByEntity(Entity entity) {
        if (!(entity instanceof LivingEntity)) return null;
        String id = entity.getPersistentDataContainer().get(mobIdKey, PersistentDataType.STRING);
        if (id == null) return null;
        return getMobType(id);
    }

    public LivingEntity spawn(String id, Location location) {
        MobType mobType = getMobType(id);
        if (mobType == null) return null;
        return spawn(mobType, location);
    }

    public LivingEntity spawn(MobType mobType, Location location) {
        if (location == null) return null;
        World world = location.getWorld();
        if (world == null) return null;

        Entity entity = world.spawnEntity(location, mobType.getEntityType());
        if (!(entity instanceof LivingEntity)) {
            entity.remove();
            plugin.getLogger().warning("尝试为 MobType " + mobType.getId() + " 生成的实体不是 LivingEntity，已移除。");
            return null;
        }

        LivingEntity living = (LivingEntity) entity;
        mobType.applyTo(living);

        PersistentDataContainer pdc = living.getPersistentDataContainer();
        pdc.set(mobIdKey, PersistentDataType.STRING, mobType.getId());

        // 写入 faction，供 FactionCondition 使用
        if (mobType.getFaction() != null && !mobType.getFaction().isEmpty()) {
            pdc.set(FACTION_KEY, PersistentDataType.STRING, mobType.getFaction());
        }

        // 应用装备
        if (mobType.getEquipment() != null && living.getEquipment() != null) {
            mobType.getEquipment().applyTo(living.getEquipment());
        }

        // 应用等级（对应 Wiki「等级」：随等级提升血量与近战伤害）
        if (mobType.getLevel() > 0) {
            org.gsn.pandora.skills.mechanics.SetLevelMechanic.applyLevel(living, mobType.getLevel());
        }

        // 触发生成事件（供其他插件监听）
        try {
            new org.gsn.pandora.api.PandoraMobSpawnEvent(living, mobType)
                .callEvent();
        } catch (Throwable ignored) {}

        return living;
    }

    public boolean isPandoraMob(Entity entity) {
        if (!(entity instanceof LivingEntity)) return false;
        return entity.getPersistentDataContainer().has(mobIdKey, PersistentDataType.STRING);
    }

    public Collection<MobType> getAllMobTypes() {
        return Collections.unmodifiableCollection(mobTypes.values());
    }
}
