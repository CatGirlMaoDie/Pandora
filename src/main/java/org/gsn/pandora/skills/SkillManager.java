package org.gsn.pandora.skills;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsn.pandora.config.PandoraConfigService;
import org.gsn.pandora.skills.condition.ChanceCondition;
import org.gsn.pandora.skills.condition.DistanceCondition;
import org.gsn.pandora.skills.condition.HealthAboveCondition;
import org.gsn.pandora.skills.condition.HealthBelowCondition;
import org.gsn.pandora.skills.condition.WorldCondition;
import org.gsn.pandora.skills.condition.OnBlockCondition;
import org.gsn.pandora.skills.condition.ScriptCondition;
import org.gsn.pandora.skills.mechanics.DamageMechanic;
import org.gsn.pandora.skills.mechanics.HealMechanic;
import org.gsn.pandora.skills.mechanics.MessageMechanic;
import org.gsn.pandora.skills.mechanics.ParticlesMechanic;
import org.gsn.pandora.skills.mechanics.PotionMechanic;
import org.gsn.pandora.skills.mechanics.SummonMechanic;
import org.gsn.pandora.skills.mechanics.TeleportMechanic;
import org.gsn.pandora.skills.mechanics.VelocityMechanic;
import org.gsn.pandora.skills.mechanics.ScriptMechanic;
import org.gsn.pandora.skills.targeter.AreaTargeter;
import org.gsn.pandora.skills.targeter.NearTargeter;
import org.gsn.pandora.skills.targeter.PlayersInRadiusTargeter;
import org.gsn.pandora.skills.targeter.SelfTargeter;
import org.gsn.pandora.skills.targeter.TargetTargeter;
import org.gsn.pandora.skills.targeter.ConeTargeter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;

public class SkillManager {

    private final JavaPlugin plugin;
    private final PandoraConfigService configService;
    private final org.gsn.pandora.script.ScriptingService scriptingService;

    private final Map<String, Function<Map<String, String>, Mechanic>> mechanicFactories = new HashMap<>();
    private final Map<String, Function<Map<String, String>, Targeter>> targeterFactories = new HashMap<>();
    private final Map<String, Function<Map<String, String>, Condition>> conditionFactories = new HashMap<>();

    private final Map<String, Skill> skills = new HashMap<>();
    private final Map<String, Long> cooldowns = new HashMap<>();

    public SkillManager(JavaPlugin plugin, PandoraConfigService configService,
                        org.gsn.pandora.script.ScriptingService scriptingService) {
        this.plugin = plugin;
        this.configService = configService;
        this.scriptingService = scriptingService;
        registerDefaults();
    }

    private void registerDefaults() {
        // Mechanics
        registerMechanic("damage", args -> {
            double amount = parseDouble(args.get("a"), 1.0);
            return new DamageMechanic(amount);
        });

        registerMechanic("message", args -> {
            String msg = args.getOrDefault("m", "");
            return new MessageMechanic(msg);
        });

        registerMechanic("particles", args -> {
            String particleName = args.getOrDefault("p", "FLAME");
            int amount = (int) parseDouble(args.get("amount"), 10.0);
            double radius = parseDouble(args.get("r"), 0.0);
            return new ParticlesMechanic(particleName, amount, radius);
        });

        registerMechanic("heal", args -> {
            double amount = parseDouble(args.get("a"), 5.0);
            return new HealMechanic(amount);
        });

        registerMechanic("potion", args -> {
            String type = args.getOrDefault("type", "SPEED");
            int duration = (int) parseDouble(args.get("d"), 200.0);
            int level = (int) parseDouble(args.get("lvl"), 1.0);
            return new PotionMechanic(type, duration, Math.max(0, level - 1));
        });

        registerMechanic("velocity", args -> {
            double y = parseDouble(args.get("y"), 0.5);
            return new VelocityMechanic(y);
        });

        registerMechanic("teleport", args -> {
            double radius = parseDouble(args.get("r"), 3.0);
            return new TeleportMechanic(radius);
        });

        registerMechanic("summon", args -> {
            String type = args.getOrDefault("type", "ZOMBIE");
            int amount = (int) parseDouble(args.get("amount"), 1.0);
            double radius = parseDouble(args.get("radius"), 0.0);
            return new SummonMechanic(type, amount, radius);
        });

        // JS 技能: script{file=...;func=...}
        registerMechanic("script", args -> {
            if (scriptingService == null || !scriptingService.isEnabled()) {
                return null;
            }
            String file = args.get("file");
            if (file == null || file.isEmpty()) {
                return null;
            }
            String func = args.getOrDefault("func", "execute");
            return new ScriptMechanic(scriptingService, file, func);
        });

        // Targeters
        registerTargeter("self", args -> new SelfTargeter());
        registerTargeter("playersinradius", args -> {
            double radius = parseDouble(args.get("r"), 5.0);
            return new PlayersInRadiusTargeter(radius);
        });
        registerTargeter("target", args -> new TargetTargeter());
        registerTargeter("area", args -> {
            double radius = parseDouble(args.get("r"), 5.0);
            return new AreaTargeter(radius);
        });
        registerTargeter("near", args -> {
            double radius = parseDouble(args.get("r"), 5.0);
            int limit = (int) parseDouble(args.get("limit"), 3.0);
            return new NearTargeter(radius, limit);
        });
        registerTargeter("cone", args -> {
            double radius = parseDouble(args.get("r"), 5.0);
            double angle = parseDouble(args.get("angle"), 60.0);
            return new ConeTargeter(radius, angle);
        });

        // Conditions
        registerCondition("chance", args -> {
            double chance = parseDouble(args.get("p"), 1.0);
            return new ChanceCondition(chance);
        });
        registerCondition("healthabove", args -> {
            double threshold = parseDouble(args.get("amount"), 0.5);
            return new HealthAboveCondition(threshold);
        });
        registerCondition("healthbelow", args -> {
            double threshold = parseDouble(args.get("amount"), 0.5);
            return new HealthBelowCondition(threshold);
        });
        registerCondition("distance", args -> {
            double min = parseDouble(args.get("min"), 0.0);
            double max = parseDouble(args.get("max"), 999999.0);
            return new DistanceCondition(min, max);
        });
        registerCondition("world", args -> {
            String name = args.get("name");
            if (name == null || name.isEmpty()) {
                return null;
            }
            return new WorldCondition(name);
        });
        registerCondition("onblock", args -> {
            String type = args.get("type");
            if (type == null || type.isEmpty()) {
                return null;
            }
            return new OnBlockCondition(type);
        });

        // JS 条件: ?js{file=...;func=...}
        registerCondition("js", args -> {
            if (scriptingService == null || !scriptingService.isEnabled()) {
                return null;
            }
            String file = args.get("file");
            if (file == null || file.isEmpty()) {
                return null;
            }
            String func = args.getOrDefault("func", "test");
            return new ScriptCondition(scriptingService, file, func);
        });
    }

    private void registerMechanic(String name, Function<Map<String, String>, Mechanic> factory) {
        if (name == null || factory == null) {
            return;
        }
        mechanicFactories.put(name.toLowerCase(Locale.ROOT), factory);
    }

    private void registerTargeter(String name, Function<Map<String, String>, Targeter> factory) {
        if (name == null || factory == null) {
            return;
        }
        targeterFactories.put(name.toLowerCase(Locale.ROOT), factory);
    }

    private void registerCondition(String name, Function<Map<String, String>, Condition> factory) {
        if (name == null || factory == null) {
            return;
        }
        conditionFactories.put(name.toLowerCase(Locale.ROOT), factory);
    }

    private double parseDouble(String raw, double def) {
        if (raw == null || raw.isEmpty()) {
            return def;
        }
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException ex) {
            return def;
        }
    }

    public void reload() {
        skills.clear();
        FileConfiguration config = configService.getSkillsConfig();
        if (config == null) {
            plugin.getLogger().warning("skills.yml 配置尚未加载。");
            return;
        }

        for (String id : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(id);
            if (section == null) {
                continue;
            }
            double cooldown = section.getDouble("Cooldown", 0.0);
            List<String> rawLines = section.getStringList("Skills");
            List<SkillLine> lines = new ArrayList<>();
            for (String raw : rawLines) {
                SkillLine line = SkillParser.parseLine(raw);
                if (line != null) {
                    lines.add(line);
                }
            }
            Skill skill = new Skill(id, cooldown, lines);
            skills.put(id.toLowerCase(Locale.ROOT), skill);
        }

        plugin.getLogger().info("已加载 " + skills.size() + " 个技能。");
    }

    public void castSkill(String name, SkillExecutionContext baseContext) {
        if (name == null || baseContext == null) {
            return;
        }
        Skill skill = skills.get(name.toLowerCase(Locale.ROOT));
        if (skill == null) {
            return;
        }

        LivingEntity caster = baseContext.getCaster();
        if (caster != null && skill.getCooldownSeconds() > 0) {
            String key = skill.getName().toLowerCase(Locale.ROOT) + ":" + caster.getUniqueId();
            long now = System.currentTimeMillis();
            Long last = cooldowns.get(key);
            if (last != null) {
                double elapsed = (now - last) / 1000.0;
                if (elapsed < skill.getCooldownSeconds()) {
                    return;
                }
            }
            cooldowns.put(key, now);
        }

        for (SkillLine line : skill.getLines()) {
            executeLine(line, baseContext);
        }
    }

    private void executeLine(SkillLine line, SkillExecutionContext ctx) {
        if (line.getMechanicName() == null || line.getMechanicName().isEmpty()) {
            return;
        }

        Function<Map<String, String>, Mechanic> mechFactory =
            mechanicFactories.get(line.getMechanicName().toLowerCase(Locale.ROOT));
        if (mechFactory == null) {
            plugin.getLogger().warning("未注册的 Mechanic: " + line.getMechanicName());
            return;
        }
        Mechanic mechanic = mechFactory.apply(line.getMechanicArgs());
        if (mechanic == null) {
            return;
        }

        Targeter targeter;
        if (line.getTargeterName() != null && !line.getTargeterName().isEmpty()) {
            Function<Map<String, String>, Targeter> targeterFactory =
                targeterFactories.get(line.getTargeterName().toLowerCase(Locale.ROOT));
            if (targeterFactory == null) {
                plugin.getLogger().warning("未注册的 Targeter: " + line.getTargeterName());
                return;
            }
            targeter = targeterFactory.apply(line.getTargeterArgs());
        } else {
            targeter = new SelfTargeter();
        }

        List<LivingEntity> targets = targeter.resolve(ctx);
        if (targets.isEmpty()) {
            return;
        }

        List<Condition> conditions = new ArrayList<>();
        for (SkillLine.ConditionSpec spec : line.getConditions()) {
            Function<Map<String, String>, Condition> condFactory =
                conditionFactories.get(spec.getName().toLowerCase(Locale.ROOT));
            if (condFactory == null) {
                plugin.getLogger().warning("未注册的 Condition: " + spec.getName());
                continue;
            }
            Condition condition = condFactory.apply(spec.getArgs());
            if (condition != null) {
                conditions.add(condition);
            }
        }

        if (!conditions.isEmpty()) {
            List<LivingEntity> filtered = new ArrayList<>();
            for (LivingEntity target : targets) {
                boolean ok = true;
                for (Condition condition : conditions) {
                    if (!condition.matches(ctx, target)) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    filtered.add(target);
                }
            }
            targets = filtered;
            if (targets.isEmpty()) {
                return;
            }
        }

        try {
            mechanic.execute(ctx, targets);
        } catch (Exception ex) {
            plugin.getLogger().log(Level.WARNING,
                "执行技能行时发生异常, mechanic=" + line.getMechanicName(), ex);
        }
    }
}
