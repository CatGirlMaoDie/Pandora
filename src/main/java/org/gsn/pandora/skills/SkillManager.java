package org.gsn.pandora.skills;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsn.pandora.config.PandoraConfigService;
import org.gsn.pandora.skills.condition.AltitudeCondition;
import org.gsn.pandora.skills.condition.BiomeCondition;
import org.gsn.pandora.skills.condition.ChanceCondition;
import org.gsn.pandora.skills.condition.DistanceCondition;
import org.gsn.pandora.skills.condition.EntityTypeCondition;
import org.gsn.pandora.skills.condition.FactionCondition;
import org.gsn.pandora.skills.condition.FieldOfViewCondition;
import org.gsn.pandora.skills.condition.HasPotionEffectCondition;
import org.gsn.pandora.skills.condition.HasTagCondition;
import org.gsn.pandora.skills.condition.HealthAboveCondition;
import org.gsn.pandora.skills.condition.HealthBelowCondition;
import org.gsn.pandora.skills.condition.HealthPercentCondition;
import org.gsn.pandora.skills.condition.HoldingCondition;
import org.gsn.pandora.skills.condition.LightLevelCondition;
import org.gsn.pandora.skills.condition.LivingInRadiusCondition;
import org.gsn.pandora.skills.condition.MobsInRadiusCondition;
import org.gsn.pandora.skills.condition.MovingCondition;
import org.gsn.pandora.skills.condition.NameCondition;
import org.gsn.pandora.skills.condition.OnBlockCondition;
import org.gsn.pandora.skills.condition.PitchCondition;
import org.gsn.pandora.skills.condition.PlayerNotWithinCondition;
import org.gsn.pandora.skills.condition.PlayersInWorldCondition;
import org.gsn.pandora.skills.condition.PlayersOnlineCondition;
import org.gsn.pandora.skills.condition.PlayerWithinCondition;
import org.gsn.pandora.skills.condition.ScoreCondition;
import org.gsn.pandora.skills.condition.ScriptCondition;
import org.gsn.pandora.skills.condition.TargetWithinCondition;
import org.gsn.pandora.skills.condition.VelocityCondition;
import org.gsn.pandora.skills.condition.WearingCondition;
import org.gsn.pandora.skills.condition.WorldCondition;
import org.gsn.pandora.skills.condition.WorldTimeCondition;
import org.gsn.pandora.skills.condition.YawCondition;
import org.gsn.pandora.skills.mechanics.ActionBarMechanic;
import org.gsn.pandora.skills.mechanics.AddTradeMechanic;
import org.gsn.pandora.skills.mechanics.AnimateArmorStandMechanic;
import org.gsn.pandora.skills.mechanics.ArrowVolleyMechanic;
import org.gsn.pandora.skills.mechanics.AttributeMechanic;
import org.gsn.pandora.skills.mechanics.BossBarMechanic;
import org.gsn.pandora.skills.mechanics.BreakBlockMechanic;
import org.gsn.pandora.skills.mechanics.CommandMechanic;
import org.gsn.pandora.skills.mechanics.ConsumeSlotItemMechanic;
import org.gsn.pandora.skills.mechanics.CquipMechanic;
import org.gsn.pandora.skills.mechanics.DamageMechanic;
import org.gsn.pandora.skills.mechanics.DisengageMechanic;
import org.gsn.pandora.skills.mechanics.DropItemMechanic;
import org.gsn.pandora.skills.mechanics.ExplosionMechanic;
import org.gsn.pandora.skills.mechanics.FeedMechanic;
import org.gsn.pandora.skills.mechanics.FireworkMechanic;
import org.gsn.pandora.skills.mechanics.FreezeMechanic;
import org.gsn.pandora.skills.mechanics.ForcePullMechanic;
import org.gsn.pandora.skills.mechanics.GiveExpLevelsMechanic;
import org.gsn.pandora.skills.mechanics.GiveItemMechanic;
import org.gsn.pandora.skills.mechanics.HealMechanic;
import org.gsn.pandora.skills.mechanics.HealPercentMechanic;
import org.gsn.pandora.skills.mechanics.HologramMechanic;
import org.gsn.pandora.skills.mechanics.IgniteMechanic;
import org.gsn.pandora.skills.mechanics.JumpMechanic;
import org.gsn.pandora.skills.mechanics.LeapMechanic;
import org.gsn.pandora.skills.mechanics.LightningMechanic;
import org.gsn.pandora.skills.mechanics.LogMechanic;
import org.gsn.pandora.skills.mechanics.LookMechanic;
import org.gsn.pandora.skills.mechanics.LungeMechanic;
import org.gsn.pandora.skills.mechanics.MessageMechanic;
import org.gsn.pandora.skills.mechanics.MessageJsonMechanic;
import org.gsn.pandora.skills.mechanics.ModifyGlobalScoreMechanic;
import org.gsn.pandora.skills.mechanics.ModifyScoreMechanic;
import org.gsn.pandora.skills.mechanics.ModifyTargetScoreMechanic;
import org.gsn.pandora.skills.mechanics.OxygenMechanic;
import org.gsn.pandora.skills.mechanics.ParticlesMechanic;
import org.gsn.pandora.skills.mechanics.PotionMechanic;
import org.gsn.pandora.skills.mechanics.RemoveHeldItemMechanic;
import org.gsn.pandora.skills.mechanics.SaddleMechanic;
import org.gsn.pandora.skills.mechanics.TeleportToMechanic;
import org.gsn.pandora.skills.mechanics.WolfSitMechanic;
import org.gsn.pandora.skills.mechanics.PushMechanic;
import org.gsn.pandora.skills.mechanics.RemoveMechanic;
import org.gsn.pandora.skills.mechanics.ResetWeatherMechanic;
import org.gsn.pandora.skills.mechanics.ScriptMechanic;
import org.gsn.pandora.skills.mechanics.SetRotationMechanic;
import org.gsn.pandora.skills.mechanics.SoundMechanic;
import org.gsn.pandora.skills.mechanics.SummonMechanic;
import org.gsn.pandora.skills.mechanics.TeleportMechanic;
import org.gsn.pandora.skills.mechanics.TimeMechanic;
import org.gsn.pandora.skills.mechanics.TitleMechanic;
import org.gsn.pandora.skills.mechanics.VelocityMechanic;
import org.gsn.pandora.skills.mechanics.WeatherMechanic;
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
        // damage{amount|a=1}
        registerMechanic("damage", args -> {
            double amount = parseDouble(getArg(args, "amount", "a"), 1.0);
            return new DamageMechanic(amount);
        });

        // message{message|msg|m=""}
        registerMechanic("message", args -> {
            String msg = getArg(args, "message", "msg", "m");
            return new MessageMechanic(msg == null ? "" : msg);
        });

        // particles{particle|p=FLAME;amount|count|c=10;radius|r=0}
        registerMechanic("particles", args -> {
            String particleName = getArg(args, "particle", "p");
            if (particleName == null) particleName = "FLAME";
            int amount = (int) parseDouble(getArg(args, "amount", "count", "c"), 10.0);
            double radius = parseDouble(getArg(args, "radius", "r"), 0.0);
            return new ParticlesMechanic(particleName, amount, radius);
        });

        // heal{amount|a=5}
        registerMechanic("heal", args -> {
            double amount = parseDouble(getArg(args, "amount", "a"), 5.0);
            return new HealMechanic(amount);
        });

        // potion{type|t=SPEED;duration|d|ticks=200;level|lvl|amplifier|amp=1}
        registerMechanic("potion", args -> {
            String type = getArg(args, "type", "t");
            if (type == null) type = "SPEED";
            int duration = (int) parseDouble(getArg(args, "duration", "d", "ticks"), 200.0);
            int level = (int) parseDouble(getArg(args, "level", "lvl", "amplifier", "amp"), 1.0);
            return new PotionMechanic(type, duration, Math.max(0, level - 1));
        });

        // velocity{y|yv=0.5}
        registerMechanic("velocity", args -> {
            double y = parseDouble(getArg(args, "y", "yv"), 0.5);
            return new VelocityMechanic(y);
        });

        // teleport{radius|r=3}
        registerMechanic("teleport", args -> {
            double radius = parseDouble(getArg(args, "radius", "r"), 3.0);
            return new TeleportMechanic(radius);
        });

        // summon{type|t=ZOMBIE;amount|a=1;radius|r=0}
        registerMechanic("summon", args -> {
            String type = getArg(args, "type", "t");
            if (type == null) type = "ZOMBIE";
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            double radius = parseDouble(getArg(args, "radius", "r"), 0.0);
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
            String func = getArg(args, "func", "function");
            return new ScriptMechanic(scriptingService, file, func == null ? "execute" : func);
        });

        // ignite{ticks|duration|d|t=60}
        registerMechanic("ignite", args -> {
            int ticks = (int) parseDouble(getArg(args, "ticks", "duration", "d", "t"), 60.0);
            return new IgniteMechanic(ticks);
        });
        registerMechanic("setfire", args -> {
            int ticks = (int) parseDouble(getArg(args, "ticks", "duration", "d", "t"), 60.0);
            return new IgniteMechanic(ticks);
        });

        // freeze{ticks|duration|d|t=60}
        registerMechanic("freeze", args -> {
            int ticks = (int) parseDouble(getArg(args, "ticks", "duration", "d", "t"), 60.0);
            return new FreezeMechanic(ticks);
        });

        // lightning{damage|d=0}
        registerMechanic("lightning", args -> {
            double dmg = parseDouble(getArg(args, "damage", "d"), 0.0);
            return new LightningMechanic(dmg);
        });
        registerMechanic("strike", args -> {
            double dmg = parseDouble(getArg(args, "damage", "d"), 0.0);
            return new LightningMechanic(dmg);
        });

        // explosion{yield|y=0;blockdamage|bd=false;fire|f=false}
        registerMechanic("explosion", args -> {
            float yield = (float) parseDouble(getArg(args, "yield", "y"), 0.0);
            boolean bd = "true".equalsIgnoreCase(getArg(args, "blockdamage", "bd"));
            boolean fire = "true".equalsIgnoreCase(getArg(args, "fire", "f", "ft"));
            return new ExplosionMechanic(yield, bd, fire);
        });
        registerMechanic("explode", args -> {
            float yield = (float) parseDouble(getArg(args, "yield", "y"), 0.0);
            boolean bd = "true".equalsIgnoreCase(getArg(args, "blockdamage", "bd"));
            boolean fire = "true".equalsIgnoreCase(getArg(args, "fire", "f", "ft"));
            return new ExplosionMechanic(yield, bd, fire);
        });

        // command{command|cmd|c=...;asCaster|ac=false;asTarget|at=false;asOp|op=false}
        registerMechanic("command", args -> {
            String cmd = getArg(args, "command", "cmd", "c");
            if (cmd == null || cmd.isEmpty()) return null;
            boolean asCaster = "true".equalsIgnoreCase(getArg(args, "asCaster", "caster", "ac", "sudo", "asmob"));
            boolean asTarget = "true".equalsIgnoreCase(getArg(args, "asTarget", "target", "at", "sudotarget"));
            boolean asOp = "true".equalsIgnoreCase(getArg(args, "asOp", "op"));
            return new CommandMechanic(cmd, asCaster, asTarget, asOp);
        });
        registerMechanic("cmd", args -> {
            String cmd = getArg(args, "command", "cmd", "c");
            if (cmd == null || cmd.isEmpty()) return null;
            boolean asCaster = "true".equalsIgnoreCase(getArg(args, "asCaster", "caster", "ac", "sudo", "asmob"));
            boolean asTarget = "true".equalsIgnoreCase(getArg(args, "asTarget", "target", "at", "sudotarget"));
            boolean asOp = "true".equalsIgnoreCase(getArg(args, "asOp", "op"));
            return new CommandMechanic(cmd, asCaster, asTarget, asOp);
        });

        // forcepull{spread|s=0;spreadV|vspread|vs=0}
        registerMechanic("forcepull", args -> {
            double spread = parseDouble(getArg(args, "spread", "s"), 0.0);
            double spreadV = parseDouble(getArg(args, "spreadV", "vspread", "vs"), 0.0);
            return new ForcePullMechanic(spread, spreadV);
        });
        registerMechanic("pull", args -> {
            double spread = parseDouble(getArg(args, "spread", "s"), 0.0);
            double spreadV = parseDouble(getArg(args, "spreadV", "vspread", "vs"), 0.0);
            return new ForcePullMechanic(spread, spreadV);
        });

        // push{velocity|v|magnitude=1;velocityY|yvelocity|vy|yv=0}
        registerMechanic("push", args -> {
            double v = parseDouble(getArg(args, "velocity", "v", "magnitude"), 1.0);
            double vy = parseDouble(getArg(args, "velocityY", "yvelocity", "vy", "yv"), 0.0);
            return new PushMechanic(v, vy);
        });
        registerMechanic("knockback", args -> {
            double v = parseDouble(getArg(args, "velocity", "v", "magnitude"), 1.0);
            double vy = parseDouble(getArg(args, "velocityY", "yvelocity", "vy", "yv"), 0.0);
            return new PushMechanic(v, vy);
        });

        // jump{velocity|v=1}
        registerMechanic("jump", args -> {
            double v = parseDouble(getArg(args, "velocity", "v"), 1.0);
            return new JumpMechanic(v);
        });

        // lunge{velocity|v|magnitude=1;velocityY|yvelocity|vy|yv=0.5}
        registerMechanic("lunge", args -> {
            double v = parseDouble(getArg(args, "velocity", "v", "magnitude"), 1.0);
            double vy = parseDouble(getArg(args, "velocityY", "yvelocity", "vy", "yv"), 0.5);
            return new LungeMechanic(v, vy);
        });

        // disengage{velocity|v|magnitude=1;velocityY|yvelocity|vy|yv=0.5}
        registerMechanic("disengage", args -> {
            double v = parseDouble(getArg(args, "velocity", "v", "magnitude"), 1.0);
            double vy = parseDouble(getArg(args, "velocityY", "yvelocity", "vy", "yv"), 0.5);
            return new DisengageMechanic(v, vy);
        });

        // firework{type|t=BALL;power|p|duration|d=2;flicker|f=false;trail|tr=false;colors|color|c=#FFFFFF;fadedColors|fcolors|fc=#FFFFFF}
        registerMechanic("firework", args -> {
            String type = getArg(args, "type", "t");
            if (type == null) type = "BALL";
            int power = (int) parseDouble(getArg(args, "power", "p", "duration", "d"), 2.0);
            boolean flicker = "true".equalsIgnoreCase(getArg(args, "flicker", "f"));
            boolean trail = "true".equalsIgnoreCase(getArg(args, "trail", "tr"));
            String color = getArg(args, "colors", "color", "c");
            String fade = getArg(args, "fadedColors", "fcolors", "fc");
            return new FireworkMechanic(type, power, flicker, trail,
                color == null ? "#FFFFFF" : color, fade == null ? "#FFFFFF" : fade);
        });

        // arrowvolley{amount|arrows|a=20;spread|s=45;fireTicks|ft|f=0;removeDelay|rd|r=200;velocity|v=20;pickup|canPickup=true}
        registerMechanic("arrowvolley", args -> {
            int amount = (int) parseDouble(getArg(args, "amount", "arrows", "a"), 20.0);
            double spread = parseDouble(getArg(args, "spread", "s"), 45.0);
            int fireTicks = (int) parseDouble(getArg(args, "fireTicks", "ft", "f"), 0.0);
            int removeDelay = (int) parseDouble(getArg(args, "removeDelay", "rd", "r"), 200.0);
            double velocity = parseDouble(getArg(args, "velocity", "v"), 20.0);
            boolean pickup = !"false".equalsIgnoreCase(getArg(args, "pickup", "canPickup"));
            return new ArrowVolleyMechanic(amount, spread, fireTicks, removeDelay, velocity, pickup);
        });

        // modifyscore{objective|obj|o=...;entry|e|name|n=dummy;action|a=SET;value|v=0}
        registerMechanic("modifyscore", args -> {
            String obj = getArg(args, "objective", "obj", "o");
            if (obj == null || obj.isEmpty()) return null;
            String entry = getArg(args, "entry", "e", "name", "n");
            String action = getArg(args, "action", "a");
            int value = (int) parseDouble(getArg(args, "value", "v"), 0.0);
            return new ModifyScoreMechanic(obj, entry == null ? "dummy" : entry,
                action == null ? "SET" : action, value);
        });

        // actionbar{message|msg|m=...}
        registerMechanic("actionbar", args -> {
            String msg = getArg(args, "message", "msg", "m");
            return new ActionBarMechanic(msg == null ? "" : msg);
        });
        registerMechanic("actionmessage", args -> {
            String msg = getArg(args, "message", "msg", "m");
            return new ActionBarMechanic(msg == null ? "" : msg);
        });

        // title{title|t=...;subtitle|sub|s=...;fadeIn|fi=10;stay=70;fadeOut|fo=20}
        registerMechanic("title", args -> {
            String t = getArg(args, "title", "t");
            String sub = getArg(args, "subtitle", "sub", "s");
            int fi = (int) parseDouble(getArg(args, "fadeIn", "fi"), 10.0);
            int stay = (int) parseDouble(getArg(args, "stay"), 70.0);
            int fo = (int) parseDouble(getArg(args, "fadeOut", "fo"), 20.0);
            return new TitleMechanic(t == null ? "" : t, sub == null ? "" : sub, fi, stay, fo);
        });
        registerMechanic("titlemessage", args -> {
            String t = getArg(args, "title", "t");
            String sub = getArg(args, "subtitle", "sub", "s");
            int fi = (int) parseDouble(getArg(args, "fadeIn", "fi"), 10.0);
            int stay = (int) parseDouble(getArg(args, "stay"), 70.0);
            int fo = (int) parseDouble(getArg(args, "fadeOut", "fo"), 20.0);
            return new TitleMechanic(t == null ? "" : t, sub == null ? "" : sub, fi, stay, fo);
        });

        // sound{sound|s=ENTITY_GENERIC_EXPLODE;volume|v=1.0;pitch|p=1.0}
        registerMechanic("sound", args -> {
            String s = getArg(args, "sound", "s");
            if (s == null) s = "ENTITY_GENERIC_EXPLODE";
            float vol = (float) parseDouble(getArg(args, "volume", "v"), 1.0);
            float pitch = (float) parseDouble(getArg(args, "pitch", "p"), 1.0);
            return new SoundMechanic(s, vol, pitch);
        });
        registerMechanic("playsound", args -> {
            String s = getArg(args, "sound", "s");
            if (s == null) s = "ENTITY_GENERIC_EXPLODE";
            float vol = (float) parseDouble(getArg(args, "volume", "v"), 1.0);
            float pitch = (float) parseDouble(getArg(args, "pitch", "p"), 1.0);
            return new SoundMechanic(s, vol, pitch);
        });

        // giveitem{items|item|i=<材质>;amount|a=1}
        registerMechanic("giveitem", args -> {
            String mat = getArg(args, "items", "item", "i");
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            return new GiveItemMechanic(mat, amount);
        });

        // dropitem{items|item|i=<材质>;amount|a=1}
        registerMechanic("dropitem", args -> {
            String mat = getArg(args, "items", "item", "i");
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            return new DropItemMechanic(mat, amount);
        });

        // feed{amount|a=1;saturation|s=0}
        registerMechanic("feed", args -> {
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            float sat = (float) parseDouble(getArg(args, "saturation", "s"), 0.0);
            return new FeedMechanic(amount, sat);
        });

        // oxygen{amount|a=0}
        registerMechanic("oxygen", args -> {
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 0.0);
            return new OxygenMechanic(amount);
        });

        // giveexperiencelevels{amount|a=1}
        registerMechanic("giveexperiencelevels", args -> {
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            return new GiveExpLevelsMechanic(amount);
        });
        registerMechanic("giveexplevel", args -> {
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            return new GiveExpLevelsMechanic(amount);
        });
        registerMechanic("takeexperiencelevels", args -> {
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            return new GiveExpLevelsMechanic(-amount);
        });
        registerMechanic("takeexplevel", args -> {
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            return new GiveExpLevelsMechanic(-amount);
        });

        // log{message|m=...;level|l=INFO}
        registerMechanic("log", args -> {
            String msg = getArg(args, "message", "m");
            String lvl = getArg(args, "level", "l");
            return new LogMechanic(msg, lvl);
        });

        // remove{}
        registerMechanic("remove", args -> new RemoveMechanic());

        // look{headOnly|ho=false}
        registerMechanic("look", args -> {
            boolean headOnly = "true".equalsIgnoreCase(getArg(args, "headOnly", "ho"));
            return new LookMechanic(headOnly);
        });

        // setrotation{yaw|y=0;pitch|p=0;relative|rel|r=false}
        registerMechanic("setrotation", args -> {
            float yaw = (float) parseDouble(getArg(args, "yaw", "y"), 0.0);
            float pitch = (float) parseDouble(getArg(args, "pitch", "p"), 0.0);
            boolean rel = "true".equalsIgnoreCase(getArg(args, "relative", "rel", "r"));
            return new SetRotationMechanic(yaw, pitch, rel);
        });

        // attribute{attribute|attr=GENERIC_MAX_HEALTH;amount|amt|a=0;duration|dur=0;operation|op=ADD_NUMBER}
        registerMechanic("attribute", args -> {
            String attr = getArg(args, "attribute", "attr");
            double amount = parseDouble(getArg(args, "amount", "amt", "a"), 0.0);
            int dur = (int) parseDouble(getArg(args, "duration", "dur"), 0.0);
            String op = getArg(args, "operation", "op");
            return new AttributeMechanic(attr, amount, dur, op);
        });

        // hologram{text|t=...;stayTime|time|stay=100;yOffset|yo|y=0}
        registerMechanic("hologram", args -> {
            String text = getArg(args, "text", "t");
            int stay = (int) parseDouble(getArg(args, "stayTime", "time", "stay"), 100.0);
            double yo = parseDouble(getArg(args, "yOffset", "yo", "y"), 0.0);
            return new HologramMechanic(text, stay, yo);
        });

        // weather{type|t=CLEAR;duration|d=6000;personal=true}
        registerMechanic("weather", args -> {
            String type = getArg(args, "type", "t");
            int dur = (int) parseDouble(getArg(args, "duration", "d"), 6000.0);
            boolean personal = !"false".equalsIgnoreCase(getArg(args, "personal"));
            return new WeatherMechanic(type, dur, personal);
        });
        registerMechanic("resetweather", args -> new ResetWeatherMechanic());

        // time{mode|m=ADD;amount|amt|ticks|t=20}
        registerMechanic("time", args -> {
            String mode = getArg(args, "mode", "m");
            long amount = (long) parseDouble(getArg(args, "amount", "amt", "ticks", "t"), 20.0);
            return new TimeMechanic(mode, amount);
        });
        registerMechanic("settime", args -> {
            long amount = (long) parseDouble(getArg(args, "amount", "amt", "ticks", "t"), 0.0);
            return new TimeMechanic("SET", amount);
        });

        // modifytargetscore{objective|obj|o=...;action|a=SET;value|v=0}
        registerMechanic("modifytargetscore", args -> {
            String obj = getArg(args, "objective", "obj", "o");
            if (obj == null || obj.isEmpty()) return null;
            String action = getArg(args, "action", "a");
            int value = (int) parseDouble(getArg(args, "value", "v"), 0.0);
            return new ModifyTargetScoreMechanic(obj, action, value);
        });

        // modifyglobalscore{objective|obj|o=...;entry|e=dummy;action|a=ADD;value|v=0}
        registerMechanic("modifyglobalscore", args -> {
            String obj = getArg(args, "objective", "obj", "o");
            if (obj == null || obj.isEmpty()) return null;
            String entry = getArg(args, "entry", "e");
            String action = getArg(args, "action", "a");
            int value = (int) parseDouble(getArg(args, "value", "v"), 0.0);
            return new ModifyGlobalScoreMechanic(obj, entry, action, value);
        });

        // messagejson{message|msg|m=...}
        registerMechanic("messagejson", args -> {
            String msg = getArg(args, "message", "msg", "m");
            return new MessageJsonMechanic(msg);
        });

        // healpercent{multiplier|m|a|amount=0.1}
        registerMechanic("healpercent", args -> {
            double mult = parseDouble(getArg(args, "multiplier", "m", "a", "amount"), 0.1);
            return new HealPercentMechanic(mult);
        });

        // teleportto{location|to|loc|l|coordinates|c=x,y,z;world|w=...;yaw|y=0;pitch|p=0;relative|m=false}
        registerMechanic("teleportto", args -> {
            String coords = getArg(args, "location", "target", "to", "loc", "l", "coordinates", "c");
            String world = getArg(args, "world", "w");
            float yaw = (float) parseDouble(getArg(args, "yaw", "y"), 0.0);
            float pitch = (float) parseDouble(getArg(args, "pitch", "p"), 0.0);
            boolean rel = "true".equalsIgnoreCase(getArg(args, "relative", "m"));
            return new TeleportToMechanic(coords, world, yaw, pitch, rel);
        });

        // wolfsit{sit|state|value=true}
        registerMechanic("wolfsit", args -> {
            boolean sit = !"false".equalsIgnoreCase(getArg(args, "sit", "state", "value"));
            return new WolfSitMechanic(sit);
        });

        // saddle{take|t|remove|r=false}
        registerMechanic("saddle", args -> {
            boolean take = "true".equalsIgnoreCase(getArg(args, "take", "t", "remove", "r"));
            return new SaddleMechanic(take);
        });

        // breakblock{doDrops|drops|d=true;doEffect|effect|e=true}
        registerMechanic("breakblock", args -> {
            boolean drops = !"false".equalsIgnoreCase(getArg(args, "doDrops", "drops", "d"));
            boolean effect = !"false".equalsIgnoreCase(getArg(args, "doEffect", "effect", "e"));
            return new BreakBlockMechanic(drops, effect);
        });

        // consumeslotitem{slot|s=HAND;amount|a=1}
        registerMechanic("consumeslotitem", args -> {
            String slot = getArg(args, "slot", "s");
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            return new ConsumeSlotItemMechanic(slot, amount);
        });

        // cquip{item|i=...;slot|s=0}
        registerMechanic("cquip", args -> {
            String mat = getArg(args, "item", "i");
            int slot = (int) parseDouble(getArg(args, "slot", "s"), 0.0);
            return new CquipMechanic(mat, slot);
        });

        // removehelditem{amount|a=1}
        registerMechanic("removehelditem", args -> {
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            return new RemoveHeldItemMechanic(amount);
        });

        // leap{velocity|v=1.0;noise|n=0}
        registerMechanic("leap", args -> {
            double v = parseDouble(getArg(args, "velocity", "v"), 1.0);
            double n = parseDouble(getArg(args, "noise", "n"), 0.0);
            return new LeapMechanic(v, n);
        });

        // barcreate{name|n=infobar;display|d=...;value|v=1.0;color|c=RED;style|s=SOLID}
        registerMechanic("barcreate", args -> {
            String name = getArg(args, "name", "n");
            String display = getArg(args, "display", "d");
            double value = parseDouble(getArg(args, "value", "v"), 1.0);
            String color = getArg(args, "color", "c");
            String style = getArg(args, "style", "s");
            return new BossBarMechanic(BossBarMechanic.Mode.CREATE, name, display, value, color, style);
        });
        registerMechanic("barset", args -> {
            String name = getArg(args, "name", "n");
            String display = getArg(args, "display", "d");
            double value = parseDouble(getArg(args, "value", "v"), 1.0);
            String color = getArg(args, "color", "c");
            String style = getArg(args, "style", "s");
            return new BossBarMechanic(BossBarMechanic.Mode.SET, name, display, value, color, style);
        });
        registerMechanic("barremove", args -> {
            String name = getArg(args, "name", "n");
            return new BossBarMechanic(BossBarMechanic.Mode.REMOVE, name, null, 1.0, null, null);
        });

        // addtrade{action|mode|m=ADD;slot|s|index=0;ingredient|item|i=STONE;ingredient2|item2|i2=...;result|r=STONE;maxuses|uses|u=MAX;expReward|exp=false;villagerExperience|villExp|vexp=0}
        registerMechanic("addtrade", args -> {
            String action = getArg(args, "action", "mode", "m");
            int slot = (int) parseDouble(getArg(args, "slot", "s", "index"), 0.0);
            String ing1 = getArg(args, "ingredient", "item", "ingredient1", "item1", "i", "i1");
            String ing2 = getArg(args, "ingredient2", "item2", "i2");
            String res = getArg(args, "result", "r");
            int maxUses = (int) parseDouble(getArg(args, "maxuses", "uses", "u"), 0.0);
            boolean expReward = "true".equalsIgnoreCase(getArg(args, "expReward", "experienceReward", "exp", "dropExp"));
            int villExp = (int) parseDouble(getArg(args, "villagerExperience", "villExp", "vexp"), 0.0);
            return new AddTradeMechanic(action, slot, ing1, ing2, res, maxUses, expReward, villExp);
        });

        // animatearmorstand{head|h=x,y,z;body|b=...;leftArm|la=...;rightArm|ra=...;leftLeg|ll=...;rightLeg|rl=...;useDegrees|ud=true}
        registerMechanic("animatearmorstand", args -> {
            boolean ud = !"false".equalsIgnoreCase(getArg(args, "useDegrees", "ud"));
            return new AnimateArmorStandMechanic(
                getArg(args, "head", "h"),
                getArg(args, "body", "b"),
                getArg(args, "leftArm", "la"),
                getArg(args, "rightArm", "ra"),
                getArg(args, "leftLeg", "ll"),
                getArg(args, "rightLeg", "rl"),
                ud);
        });

        // delay{ticks=20}
        registerMechanic("delay", args -> {
            int ticks = (int) parseDouble(getArg(args, "ticks", "t"), 20.0);
            return new org.gsn.pandora.skills.mechanics.DelayMechanic(ticks);
        });

        // goto{speed|s=1;spreadH|sh=0;spreadV|sv=0}
        registerMechanic("goto", args -> {
            double speed = parseDouble(getArg(args, "speed", "s"), 1.0);
            double sh = parseDouble(getArg(args, "spreadH", "sh"), 0.0);
            double sv = parseDouble(getArg(args, "spreadV", "sv"), 0.0);
            return new org.gsn.pandora.skills.mechanics.GotoMechanic(speed, sh, sv);
        });

        // matchrotation{} — 朝向目标
        registerMechanic("matchrotation", args -> new org.gsn.pandora.skills.mechanics.MatchRotationMechanic());

        // rotatetowards{yaw|y=...;pitch|p=...;relative|rel|r=false}
        registerMechanic("rotatetowards", args -> {
            String yawStr = getArg(args, "yaw", "y");
            String pitchStr = getArg(args, "pitch", "p");
            Float yaw = yawStr != null ? (float) parseDouble(yawStr, 0.0) : null;
            Float pitch = pitchStr != null ? (float) parseDouble(pitchStr, 0.0) : null;
            boolean rel = "true".equalsIgnoreCase(getArg(args, "relative", "rel", "r"));
            return new org.gsn.pandora.skills.mechanics.RotateTowardsMechanic(yaw, pitch, rel);
        });

        // bonemeal{blockFace|bf|face|f=UP}
        registerMechanic("bonemeal", args -> {
            String face = getArg(args, "blockFace", "bf", "face", "f");
            return new org.gsn.pandora.skills.mechanics.BoneMealMechanic(face);
        });

        // enderdragonsetphase{phase|p=CIRCLING}
        registerMechanic("enderdragonsetphase", args -> {
            String phase = getArg(args, "phase", "p");
            return new org.gsn.pandora.skills.mechanics.EnderDragonPhaseMechanic(phase);
        });

        // fillchest{items|item|i=<材质>;amount|a=1;shouldEmptyChest|empty=false}
        registerMechanic("fillchest", args -> {
            String mat = getArg(args, "items", "item", "i");
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            boolean empty = "true".equalsIgnoreCase(getArg(args, "shouldEmptyChest", "empty"));
            return new org.gsn.pandora.skills.mechanics.FillChestMechanic(mat, amount, empty);
        });

        // stealitem{}
        registerMechanic("stealitem", args -> new org.gsn.pandora.skills.mechanics.StealItemMechanic());

        // settextdisplay{text|t=...}
        registerMechanic("settextdisplay", args -> {
            String text = getArg(args, "text", "t");
            return new org.gsn.pandora.skills.mechanics.SetTextDisplayMechanic(text);
        });

        // effect:itemspray{items|item|i=...;amount|a=10;force|f|velocity|v=1;yForce|yf|yv=1;yOffset|yo|y=1;gravity|g=true;allowPickup|ap=false}
        registerMechanic("effect:itemspray", args -> {
            String mat = getArg(args, "items", "item", "i");
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 10.0);
            double force = parseDouble(getArg(args, "force", "f", "velocity", "v"), 1.0);
            double yf = parseDouble(getArg(args, "yForce", "yf", "yvelocity", "yv"), 1.0);
            double yo = parseDouble(getArg(args, "yOffset", "yo", "y"), 1.0);
            boolean gravity = !"false".equalsIgnoreCase(getArg(args, "gravity", "g"));
            boolean pickup = "true".equalsIgnoreCase(getArg(args, "allowPickup", "ap"));
            return new org.gsn.pandora.skills.mechanics.EffectItemSprayMechanic(mat, amount, force, yf, yo, gravity, pickup);
        });
        registerMechanic("itemspray", args -> {
            String mat = getArg(args, "items", "item", "i");
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 10.0);
            double force = parseDouble(getArg(args, "force", "f", "velocity", "v"), 1.0);
            double yf = parseDouble(getArg(args, "yForce", "yf", "yvelocity", "yv"), 1.0);
            double yo = parseDouble(getArg(args, "yOffset", "yo", "y"), 1.0);
            boolean gravity = !"false".equalsIgnoreCase(getArg(args, "gravity", "g"));
            boolean pickup = "true".equalsIgnoreCase(getArg(args, "allowPickup", "ap"));
            return new org.gsn.pandora.skills.mechanics.EffectItemSprayMechanic(mat, amount, force, yf, yo, gravity, pickup);
        });

        // volley{amount|a=5;radius|r=3;yOffset|y=0;pickup|canPickup=false}
        registerMechanic("volley", args -> {
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 5.0);
            double radius = parseDouble(getArg(args, "radius", "r"), 3.0);
            double yo = parseDouble(getArg(args, "yOffset", "y"), 0.0);
            boolean pickup = "true".equalsIgnoreCase(getArg(args, "pickup", "canPickup"));
            return new org.gsn.pandora.skills.mechanics.VolleyMechanic(amount, radius, yo, pickup);
        });

        // gcd{ticks|t=20}
        registerMechanic("gcd", args -> {
            int ticks = (int) parseDouble(getArg(args, "ticks", "t"), 20.0);
            return new org.gsn.pandora.skills.mechanics.GcdMechanic(ticks);
        });

        // giveitemfromslot{slot|s=HAND}
        registerMechanic("giveitemfromslot", args -> {
            String slot = getArg(args, "slot", "s");
            return new org.gsn.pandora.skills.mechanics.GiveItemFromSlotMechanic(slot);
        });

        // giveitemfromtarget{items|item|i=<材质>;amount|a=1}
        registerMechanic("giveitemfromtarget", args -> {
            String mat = getArg(args, "items", "item", "i");
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            return new org.gsn.pandora.skills.mechanics.GiveItemFromTargetMechanic(mat, amount);
        });

        // metaskill{skill=<skillName>}
        registerMechanic("metaskill", args -> {
            String skill = getArg(args, "skill");
            if (skill == null || skill.isEmpty()) return null;
            return new org.gsn.pandora.skills.mechanics.MetaSkillMechanic(skill, this);
        });

        // physicaldamage{multiplier|m=1.0;forcedDamage|fd=0}
        registerMechanic("physicaldamage", args -> {
            double mult = parseDouble(getArg(args, "multiplier", "m"), 1.0);
            double forced = parseDouble(getArg(args, "forcedDamage", "fd"), 0.0);
            return new org.gsn.pandora.skills.mechanics.PhysicalDamageMechanic(mult, forced);
        });

        // recoil{duration|d=20}
        registerMechanic("recoil", args -> {
            int dur = (int) parseDouble(getArg(args, "duration", "d"), 20.0);
            return new org.gsn.pandora.skills.mechanics.RecoilMechanic(dur);
        });

        // setdisplayentityitem{item|i|type|t|material|mat|m=...}
        registerMechanic("setdisplayentityitem", args -> {
            String mat = getArg(args, "item", "i", "type", "t", "material", "mat", "m");
            return new org.gsn.pandora.skills.mechanics.SetDisplayEntityItemMechanic(mat);
        });

        // setinteractionsize{height|h=1.0;width|w=1.0}
        registerMechanic("setinteractionsize", args -> {
            float h = (float) parseDouble(getArg(args, "height", "h"), 1.0);
            float w = (float) parseDouble(getArg(args, "width", "w"), 1.0);
            return new org.gsn.pandora.skills.mechanics.SetInteractionSizeMechanic(h, w);
        });

        // displaytransformation{action|a=SET;transformationType|transformation|type|tt=TRANSLATION;values|value|val=0,0,0}
        registerMechanic("displaytransformation", args -> {
            String action = getArg(args, "action", "a");
            String type = getArg(args, "transformationType", "transformation", "type", "tt");
            String values = getArg(args, "values", "value", "val");
            return new org.gsn.pandora.skills.mechanics.DisplayTransformationMechanic(action, type, values);
        });

        // effect:enderbeam{duration|d=60;yOffset|yo|y=0}
        registerMechanic("effect:enderbeam", args -> {
            int dur = (int) parseDouble(getArg(args, "duration", "d"), 60.0);
            double yo = parseDouble(getArg(args, "yOffset", "yo", "y"), 0.0);
            return new org.gsn.pandora.skills.mechanics.EffectEnderBeamMechanic(dur, yo);
        });
        registerMechanic("enderbeam", args -> {
            int dur = (int) parseDouble(getArg(args, "duration", "d"), 60.0);
            double yo = parseDouble(getArg(args, "yOffset", "yo", "y"), 0.0);
            return new org.gsn.pandora.skills.mechanics.EffectEnderBeamMechanic(dur, yo);
        });

        // effect:geyser{height|h=3;type|t=water}
        registerMechanic("effect:geyser", args -> {
            int h = (int) parseDouble(getArg(args, "height", "h"), 3.0);
            String type = getArg(args, "type", "t");
            return new org.gsn.pandora.skills.mechanics.EffectGeyserMechanic(h, type);
        });
        registerMechanic("geyser", args -> {
            int h = (int) parseDouble(getArg(args, "height", "h"), 3.0);
            String type = getArg(args, "type", "t");
            return new org.gsn.pandora.skills.mechanics.EffectGeyserMechanic(h, type);
        });

        // effect:guardianbeam{duration|maxduration|md|d=400;startYOffset|syo=1;targetYOffset|tyo=1}
        registerMechanic("effect:guardianbeam", args -> {
            int dur = (int) parseDouble(getArg(args, "duration", "maxduration", "md", "d"), 400.0);
            double syo = parseDouble(getArg(args, "startYOffset", "syo"), 1.0);
            double tyo = parseDouble(getArg(args, "targetYOffset", "tyo"), 1.0);
            return new org.gsn.pandora.skills.mechanics.EffectGuardianBeamMechanic(dur, syo, tyo);
        });
        registerMechanic("guardianbeam", args -> {
            int dur = (int) parseDouble(getArg(args, "duration", "maxduration", "md", "d"), 400.0);
            double syo = parseDouble(getArg(args, "startYOffset", "syo"), 1.0);
            double tyo = parseDouble(getArg(args, "targetYOffset", "tyo"), 1.0);
            return new org.gsn.pandora.skills.mechanics.EffectGuardianBeamMechanic(dur, syo, tyo);
        });

        // effect:lightning{localized|l=false;localizedRadius|lr|r=128}
        registerMechanic("effect:lightning", args -> {
            boolean loc = "true".equalsIgnoreCase(getArg(args, "localized", "l"));
            double r = parseDouble(getArg(args, "localizedRadius", "lr", "r"), 128.0);
            return new org.gsn.pandora.skills.mechanics.EffectLightningMechanic(loc, r);
        });

        // enderdragonspawnportal{withPortals|wp|p=false}
        registerMechanic("enderdragonspawnportal", args -> {
            boolean wp = "true".equalsIgnoreCase(getArg(args, "withPortals", "wp", "p"));
            return new org.gsn.pandora.skills.mechanics.EnderDragonSpawnPortalMechanic(wp);
        });

        // equip{items|item|i|equipment|equip|e=<材质>:<槽位>}
        registerMechanic("equip", args -> {
            String spec = getArg(args, "items", "item", "i", "equipment", "equip", "e");
            if (spec == null || spec.isEmpty()) return null;
            return new org.gsn.pandora.skills.mechanics.EquipMechanic(spec);
        });

        // breakblockandgiveitem{doDrops|drops|d=true;doEffect|effect|e=true;items|item|i=...;amount|a=1}
        registerMechanic("breakblockandgiveitem", args -> {
            boolean drops = !"false".equalsIgnoreCase(getArg(args, "doDrops", "drops", "d"));
            boolean effect = !"false".equalsIgnoreCase(getArg(args, "doEffect", "effect", "e"));
            String mat = getArg(args, "items", "item", "i");
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            return new org.gsn.pandora.skills.mechanics.BreakBlockAndGiveItemMechanic(drops, effect, mat, amount);
        });

        // activatespawner{spawner|spawners|s=<spawnerId>}
        registerMechanic("activatespawner", args -> {
            String id = getArg(args, "spawner", "spawners", "s");
            if (id == null || id.isEmpty()) return null;
            return new org.gsn.pandora.skills.mechanics.ActivateSpawnerMechanic(id);
        });

        // attributemodifier{attribute|attr=GENERIC_MAX_HEALTH;operation|op=ADD_NUMBER;name|modifierName=...;amount|amt|a=0;duration|dur=0}
        registerMechanic("attributemodifier", args -> {
            String attr = getArg(args, "attribute", "attr");
            String op = getArg(args, "operation", "op");
            String name = getArg(args, "name", "modifierName");
            double amount = parseDouble(getArg(args, "amount", "amt", "a"), 0.0);
            int dur = (int) parseDouble(getArg(args, "duration", "dur"), 0.0);
            return new org.gsn.pandora.skills.mechanics.AttributeModifierMechanic(attr, op, name, amount, dur);
        });

        // basedamage{multiplier|m=1.0}
        registerMechanic("basedamage", args -> {
            double mult = parseDouble(getArg(args, "multiplier", "m"), 1.0);
            return new org.gsn.pandora.skills.mechanics.BaseDamageMechanic(mult);
        });

        // bloodyscreen{duration|d=60;cancel|c=false}
        registerMechanic("bloodyscreen", args -> {
            int dur = (int) parseDouble(getArg(args, "duration", "d"), 60.0);
            boolean cancel = "true".equalsIgnoreCase(getArg(args, "cancel", "c"));
            return new org.gsn.pandora.skills.mechanics.BloodyScreenMechanic(dur, cancel);
        });

        // bossborder{radius|r=64}
        registerMechanic("bossborder", args -> {
            double r = parseDouble(getArg(args, "radius", "r"), 64.0);
            return new org.gsn.pandora.skills.mechanics.BossBorderMechanic(r);
        });

        // damagetag{tag|t=<tagName>}
        registerMechanic("damagetag", args -> {
            String tag = getArg(args, "tag", "t");
            if (tag == null || tag.isEmpty()) return null;
            return new org.gsn.pandora.skills.mechanics.DamageTagMechanic(tag);
        });

        // doppleganger{hasNamePlate|nameplate=true;usePlayerName|upn=false}
        registerMechanic("doppleganger", args -> {
            boolean nameplate = !"false".equalsIgnoreCase(getArg(args, "hasNamePlate", "nameplate"));
            boolean upn = "true".equalsIgnoreCase(getArg(args, "usePlayerName", "upn"));
            return new org.gsn.pandora.skills.mechanics.DoppelgangerMechanic(nameplate, upn);
        });

        // enderdragonsetrespawnphase{phase|p=NONE}
        registerMechanic("enderdragonsetrespawnphase", args -> {
            String phase = getArg(args, "phase", "p");
            return new org.gsn.pandora.skills.mechanics.EnderDragonSetRespawnPhaseMechanic(phase);
        });

        // modifyprojectile{trait|t=VELOCITY;action|a=MULTIPLY;value|v=0}
        registerMechanic("modifyprojectile", args -> {
            String trait = getArg(args, "trait", "t");
            String action = getArg(args, "action", "a");
            double value = parseDouble(getArg(args, "value", "v"), 0.0);
            return new org.gsn.pandora.skills.mechanics.ModifyProjectileMechanic(trait, action, value);
        });

        // setprojectilebulletmodel{value|v|model|m=0}
        registerMechanic("setprojectilebulletmodel", args -> {
            int value = (int) parseDouble(getArg(args, "value", "v", "model", "m"), 0.0);
            return new org.gsn.pandora.skills.mechanics.SetProjectileBulletModelMechanic(value);
        });

        // wait{conditions|cond|c|until=...;interval|i=1;timeout|timeouttime|tt=200;cancelSkill|cancel|cs=false}
        registerMechanic("wait", args -> {
            int interval = (int) parseDouble(getArg(args, "interval", "i"), 1.0);
            int timeout = (int) parseDouble(getArg(args, "timeout", "timeouttime", "tt"), 200.0);
            boolean cancel = "true".equalsIgnoreCase(getArg(args, "cancelSkill", "cancel", "cs"));
            return new org.gsn.pandora.skills.mechanics.WaitMechanic(interval, timeout, cancel);
        });

        // consume{damage|dmg|d=1;heal|h=1}
        registerMechanic("consume", args -> {
            double dmg = parseDouble(getArg(args, "damage", "dmg", "d"), 1.0);
            double heal = parseDouble(getArg(args, "heal", "h"), 1.0);
            return new org.gsn.pandora.skills.mechanics.ConsumeMechanic(dmg, heal);
        });

        // damagepercent{percent|p=0.1;currentHealth|current|ch|c=false}
        registerMechanic("damagepercent", args -> {
            double pct = parseDouble(getArg(args, "percent", "p"), 0.1);
            boolean cur = "true".equalsIgnoreCase(getArg(args, "currentHealth", "current", "ch", "c"));
            return new org.gsn.pandora.skills.mechanics.DamagePercentMechanic(pct, cur);
        });

        // projectile{type=NORMAL;velocity|v=5;maxRange|mr=40;gravity|g=0;horizontalRadius|hradius|hr|r=1.25;
        //            hitPlayers|hp=true;hitNonPlayers|hnp=false;hitSelf=false;startYOffset|syo=1;onHitSkill|onhit|oh=...}
        registerMechanic("projectile", args -> {
            String type = getArg(args, "type");
            double vel = parseDouble(getArg(args, "velocity", "v"), 5.0);
            double range = parseDouble(getArg(args, "maxRange", "mr"), 40.0);
            double grav = parseDouble(getArg(args, "gravity", "g"), 0.0);
            double hr = parseDouble(getArg(args, "horizontalRadius", "hradius", "hr", "r"), 1.25);
            boolean hp = !"false".equalsIgnoreCase(getArg(args, "hitPlayers", "hp"));
            boolean hnp = "true".equalsIgnoreCase(getArg(args, "hitNonPlayers", "hnp"));
            boolean hs = "true".equalsIgnoreCase(getArg(args, "hitSelf"));
            double syo = parseDouble(getArg(args, "startYOffset", "syo"), 1.0);
            String onHit = getArg(args, "onHitSkill", "onhit", "oh");
            return new org.gsn.pandora.skills.mechanics.ProjectileMechanic(type, vel, range, grav, hr, hp, hnp, hs, syo, onHit);
        });

        // totem{velocity|v=5;maxRange|mr=40;hitRadius|r=1.25;hitPlayers|hp=true;hitNonPlayers|hnp=false;onHitSkill|onhit|oh=...}
        registerMechanic("totem", args -> {
            double vel = parseDouble(getArg(args, "velocity", "v"), 5.0);
            double range = parseDouble(getArg(args, "maxRange", "mr"), 40.0);
            double hr = parseDouble(getArg(args, "hitRadius", "hradius", "hr", "r"), 1.25);
            boolean hp = !"false".equalsIgnoreCase(getArg(args, "hitPlayers", "hp"));
            boolean hnp = "true".equalsIgnoreCase(getArg(args, "hitNonPlayers", "hnp"));
            String onHit = getArg(args, "onHitSkill", "onhit", "oh");
            return new org.gsn.pandora.skills.mechanics.TotemMechanic(vel, range, hr, hp, hnp, onHit);
        });

        // metavariableskill{skill=<skillName>} — 与 metaskill 相同，支持变量占位符
        registerMechanic("metavariableskill", args -> {
            String skill = getArg(args, "skill");
            if (skill == null || skill.isEmpty()) return null;
            return new org.gsn.pandora.skills.mechanics.MetaSkillMechanic(skill, this);
        });

        // sendsignal{signal|s|sig=<信号名>}
        registerMechanic("sendsignal", args -> {
            String sig = getArg(args, "signal", "s", "sig");
            if (sig == null || sig.isEmpty()) return null;
            return new org.gsn.pandora.skills.mechanics.SendSignalMechanic(sig);
        });

        // threatchange / threatmod{action|mode|a=ADD;amount|a=1}
        registerMechanic("threatchange", args -> {
            String action = getArg(args, "action", "mode", "m");
            double amount = parseDouble(getArg(args, "amount", "a", "value", "v"), 1.0);
            return new org.gsn.pandora.skills.mechanics.ThreatChangeMechanic(action, amount);
        });
        registerMechanic("threatmod", args -> {
            String action = getArg(args, "action", "mode", "m");
            double amount = parseDouble(getArg(args, "amount", "a", "value", "v"), 1.0);
            return new org.gsn.pandora.skills.mechanics.ThreatChangeMechanic(action, amount);
        });

        // threatclear{}
        registerMechanic("threatclear", args -> new org.gsn.pandora.skills.mechanics.ThreatClearMechanic());

        // setlevel{level|l=<等级>}
        registerMechanic("setlevel", args -> {
            int lvl = (int) parseDouble(getArg(args, "level", "l"), 0.0);
            return new org.gsn.pandora.skills.mechanics.SetLevelMechanic(lvl);
        });

        // variableset{variable|var|v|name=<名>;value|val=<值>;type|t=STRING;global|g=false}
        registerMechanic("variableset", args -> {
            String name = getArg(args, "variable", "var", "v", "name");
            String value = getArg(args, "value", "val");
            String type = getArg(args, "type", "t");
            boolean global = "true".equalsIgnoreCase(getArg(args, "global", "g"));
            if (name == null || name.isEmpty()) return null;
            return new org.gsn.pandora.skills.mechanics.VariableSetMechanic(name, value == null ? "" : value, type, global);
        });
        registerMechanic("variableadd", args -> {
            String name = getArg(args, "variable", "var", "v", "name");
            String value = getArg(args, "value", "val", "amount", "a");
            boolean global = "true".equalsIgnoreCase(getArg(args, "global", "g"));
            if (name == null || name.isEmpty() || value == null) return null;
            return new org.gsn.pandora.skills.mechanics.VariableAddMechanic(name, value, global);
        });

        // unsetvariable / varunset{variable|var|v|name=<名>;global|g=false}
        registerMechanic("unsetvariable", args -> {
            String name = getArg(args, "variable", "var", "v", "name");
            boolean global = "true".equalsIgnoreCase(getArg(args, "global", "g"));
            if (name == null || name.isEmpty()) return null;
            return new org.gsn.pandora.skills.mechanics.VariableRemoveMechanic(name, global);
        });
        registerMechanic("varunset", args -> {
            String name = getArg(args, "variable", "var", "v", "name");
            boolean global = "true".equalsIgnoreCase(getArg(args, "global", "g"));
            if (name == null || name.isEmpty()) return null;
            return new org.gsn.pandora.skills.mechanics.VariableRemoveMechanic(name, global);
        });
        registerMechanic("variableremove", args -> {
            String name = getArg(args, "variable", "var", "v", "name");
            boolean global = "true".equalsIgnoreCase(getArg(args, "global", "g"));
            if (name == null || name.isEmpty()) return null;
            return new org.gsn.pandora.skills.mechanics.VariableRemoveMechanic(name, global);
        });

        // Targeters
        registerTargeter("self", args -> new SelfTargeter());
        // playersinradius|pir{radius|r=5}
        registerTargeter("playersinradius", args -> {
            double radius = parseDouble(getArg(args, "radius", "r"), 5.0);
            return new PlayersInRadiusTargeter(radius);
        });
        registerTargeter("pir", args -> {
            double radius = parseDouble(getArg(args, "radius", "r"), 5.0);
            return new PlayersInRadiusTargeter(radius);
        });
        registerTargeter("target", args -> new TargetTargeter());
        // area|eir|entitiesinradius{radius|r=5}
        registerTargeter("area", args -> {
            double radius = parseDouble(getArg(args, "radius", "r"), 5.0);
            return new AreaTargeter(radius);
        });
        registerTargeter("eir", args -> {
            double radius = parseDouble(getArg(args, "radius", "r"), 5.0);
            return new AreaTargeter(radius);
        });
        registerTargeter("entitiesinradius", args -> {
            double radius = parseDouble(getArg(args, "radius", "r"), 5.0);
            return new AreaTargeter(radius);
        });
        // near{radius|r=5;limit|l=3}
        registerTargeter("near", args -> {
            double radius = parseDouble(getArg(args, "radius", "r"), 5.0);
            int limit = (int) parseDouble(getArg(args, "limit", "l"), 3.0);
            return new NearTargeter(radius, limit);
        });
        // cone{radius|r=5;angle|a=60}
        registerTargeter("cone", args -> {
            double radius = parseDouble(getArg(args, "radius", "r"), 5.0);
            double angle = parseDouble(getArg(args, "angle", "a"), 60.0);
            return new ConeTargeter(radius, angle);
        });

        // Conditions
        // chance{chance|c|p=1}
        registerCondition("chance", args -> {
            double chance = parseDouble(getArg(args, "chance", "c", "p"), 1.0);
            return new ChanceCondition(chance);
        });
        // healthabove{amount|a=0.5}
        registerCondition("healthabove", args -> {
            double threshold = parseDouble(getArg(args, "amount", "a"), 0.5);
            return new HealthAboveCondition(threshold);
        });
        // healthbelow{amount|a=0.5}
        registerCondition("healthbelow", args -> {
            double threshold = parseDouble(getArg(args, "amount", "a"), 0.5);
            return new HealthBelowCondition(threshold);
        });
        // distance{min=0;max|d=999999}
        registerCondition("distance", args -> {
            double min = parseDouble(getArg(args, "min"), 0.0);
            double max = parseDouble(getArg(args, "max", "d"), 999999.0);
            return new DistanceCondition(min, max);
        });
        // world{world|w|name=...}
        registerCondition("world", args -> {
            String name = getArg(args, "world", "w", "name");
            if (name == null || name.isEmpty()) {
                return null;
            }
            return new WorldCondition(name);
        });
        // onblock{type|types|block|b|t|material|mat|m=...}
        registerCondition("onblock", args -> {
            String type = getArg(args, "type", "types", "block", "b", "t", "material", "mat", "m");
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
            String func = getArg(args, "func", "function");
            return new ScriptCondition(scriptingService, file, func == null ? "test" : func);
        });

        // altitude{height|altitude|a|h=minHeight;maxHeight|mH=maxHeight}
        registerCondition("altitude", args -> {
            double min = parseDouble(getArg(args, "height", "altitude", "a", "h"), -64.0);
            double max = parseDouble(getArg(args, "maxHeight", "mH"), 320.0);
            return new AltitudeCondition(min, max);
        });

        // biome{biome|b=...}
        registerCondition("biome", args -> {
            String b = getArg(args, "biome", "b");
            if (b == null || b.isEmpty()) return null;
            return new BiomeCondition(b);
        });

        // entitytype{type|types|t=...}
        registerCondition("entitytype", args -> {
            String t = getArg(args, "type", "types", "t");
            if (t == null || t.isEmpty()) return null;
            return new EntityTypeCondition(t);
        });

        // faction{faction|f=...}
        registerCondition("faction", args -> {
            String f = getArg(args, "faction", "f");
            if (f == null || f.isEmpty()) return null;
            return new FactionCondition(f);
        });

        // fieldofview{angle|a=90;rotation|r=0}
        registerCondition("fieldofview", args -> {
            double angle = parseDouble(getArg(args, "angle", "a"), 90.0);
            return new FieldOfViewCondition(angle);
        });
        registerCondition("fov", args -> {
            double angle = parseDouble(getArg(args, "angle", "a"), 90.0);
            return new FieldOfViewCondition(angle);
        });

        // haspotioneffect{type|t=...;level|lvl|l=1-1;duration|d=0-99999}
        registerCondition("haspotioneffect", args -> {
            String t = getArg(args, "type", "t");
            if (t == null || t.isEmpty()) return null;
            int minLvl = (int) parseDouble(getArg(args, "level", "lvl", "l"), 1.0);
            int maxLvl = (int) parseDouble(getArg(args, "maxLevel", "maxLvl"), minLvl);
            int minDur = (int) parseDouble(getArg(args, "minDuration", "minD"), 0.0);
            int maxDur = (int) parseDouble(getArg(args, "duration", "d"), 99999.0);
            return new HasPotionEffectCondition(t, minLvl, Math.max(minLvl, maxLvl), minDur, maxDur);
        });

        // hastag{tag|t=...}
        registerCondition("hastag", args -> {
            String tag = getArg(args, "tag", "t");
            if (tag == null || tag.isEmpty()) return null;
            return new HasTagCondition(tag);
        });

        // healthpercent{percent|p=minPercent;maxPercent=1}
        registerCondition("healthpercent", args -> {
            double min = parseDouble(getArg(args, "minPercent", "min"), 0.0);
            double max = parseDouble(getArg(args, "percent", "p", "maxPercent", "max"), 1.0);
            return new HealthPercentCondition(min, max);
        });

        // holding{types|type|t|material|mat|m|item|i=...}
        registerCondition("holding", args -> {
            String t = getArg(args, "types", "type", "t", "material", "mat", "m", "item", "i");
            if (t == null || t.isEmpty()) return null;
            return new HoldingCondition(t);
        });

        // lightlevel{level|l=minLevel;maxLevel=15}
        registerCondition("lightlevel", args -> {
            int min = (int) parseDouble(getArg(args, "level", "l"), 0.0);
            int max = (int) parseDouble(getArg(args, "maxLevel"), 15.0);
            return new LightLevelCondition(min, Math.max(min, max));
        });

        // livinginradius{amount|a=minAmount;radius|r=16;maxAmount=maxAmount}
        registerCondition("livinginradius", args -> {
            int min = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            int max = (int) parseDouble(getArg(args, "maxAmount"), Integer.MAX_VALUE);
            double r = parseDouble(getArg(args, "radius", "r"), 16.0);
            return new LivingInRadiusCondition(r, min, Math.max(min, max));
        });

        // mobsinradius{types|t=...;amount|a=minAmount;radius|r=16;maxAmount=maxAmount}
        registerCondition("mobsinradius", args -> {
            String t = getArg(args, "types", "t");
            int min = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            int max = (int) parseDouble(getArg(args, "maxAmount"), Integer.MAX_VALUE);
            double r = parseDouble(getArg(args, "radius", "r"), 16.0);
            return new MobsInRadiusCondition(t == null ? "" : t, r, min, Math.max(min, max));
        });

        // moving{}
        registerCondition("moving", args -> new MovingCondition());

        // name{name|n=...}
        registerCondition("name", args -> {
            String n = getArg(args, "name", "n");
            if (n == null || n.isEmpty()) return null;
            return new NameCondition(n);
        });

        // pitch{pitch|p=minPitch;maxPitch=maxPitch}
        registerCondition("pitch", args -> {
            float min = (float) parseDouble(getArg(args, "pitch", "p"), -90.0);
            float max = (float) parseDouble(getArg(args, "maxPitch"), 90.0);
            return new PitchCondition(min, Math.max(min, max));
        });

        // playernotwithin{distance|d=...}
        registerCondition("playernotwithin", args -> {
            double d = parseDouble(getArg(args, "distance", "d"), 32.0);
            return new PlayerNotWithinCondition(d);
        });

        // playersinworld{amount|a=minAmount;maxAmount=maxAmount}
        registerCondition("playersinworld", args -> {
            int min = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            int max = (int) parseDouble(getArg(args, "maxAmount"), Integer.MAX_VALUE);
            return new PlayersInWorldCondition(min, Math.max(min, max));
        });

        // playersonline{amount|a=minAmount;maxAmount=maxAmount}
        registerCondition("playersonline", args -> {
            int min = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            int max = (int) parseDouble(getArg(args, "maxAmount"), Integer.MAX_VALUE);
            return new PlayersOnlineCondition(min, Math.max(min, max));
        });

        // playerwithin{distance|d=...}
        registerCondition("playerwithin", args -> {
            double d = parseDouble(getArg(args, "distance", "d"), 32.0);
            return new PlayerWithinCondition(d);
        });

        // score{objective|o=...;value|v=minValue;maxValue=maxValue}
        registerCondition("score", args -> {
            String obj = getArg(args, "objective", "o");
            if (obj == null || obj.isEmpty()) return null;
            int min = (int) parseDouble(getArg(args, "value", "v"), Integer.MIN_VALUE / 2.0);
            int max = (int) parseDouble(getArg(args, "maxValue"), Integer.MAX_VALUE / 2.0);
            return new ScoreCondition(obj, min, Math.max(min, max));
        });

        // targetwithin{distance|d=...}
        registerCondition("targetwithin", args -> {
            double d = parseDouble(getArg(args, "distance", "d"), 16.0);
            return new TargetWithinCondition(d);
        });

        // velocity{velocity|v=minVelocity;maxVelocity=maxVelocity}
        registerCondition("velocity", args -> {
            double min = parseDouble(getArg(args, "velocity", "v"), 0.0);
            double max = parseDouble(getArg(args, "maxVelocity"), Double.MAX_VALUE);
            return new VelocityCondition(min, Math.max(min, max));
        });

        // wearing{armorslot|slot|s=HEAD;types|type|t|material|mat|m|item|i=...}
        registerCondition("wearing", args -> {
            String slot = getArg(args, "armorslot", "slot", "s");
            if (slot == null) slot = "HEAD";
            String t = getArg(args, "types", "type", "t", "material", "mat", "m", "item", "i");
            if (t == null || t.isEmpty()) return null;
            return new WearingCondition(slot, t);
        });

        // worldtime{time|t=from-to}  支持 "from~to" 或单值作为 from
        registerCondition("worldtime", args -> {
            String raw = getArg(args, "time", "t");
            long from = 0, to = 23999;
            if (raw != null && !raw.isEmpty()) {
                if (raw.contains("~")) {
                    String[] parts = raw.split("~", 2);
                    from = (long) parseDouble(parts[0].trim(), 0.0);
                    to = (long) parseDouble(parts[1].trim(), 23999.0);
                } else {
                    from = (long) parseDouble(raw, 0.0);
                    to = (long) parseDouble(getArg(args, "to"), 23999.0);
                }
            }
            return new WorldTimeCondition(from, to);
        });

        // yaw{yaw|y=minYaw;maxYaw=maxYaw}
        registerCondition("yaw", args -> {
            float min = (float) parseDouble(getArg(args, "yaw", "y"), -180.0);
            float max = (float) parseDouble(getArg(args, "maxYaw"), 180.0);
            return new YawCondition(min, Math.max(min, max));
        });

        // foodlevel{amount|a=minLevel;maxLevel=20}
        registerCondition("foodlevel", args -> {
            int min = (int) parseDouble(getArg(args, "amount", "a"), 0.0);
            int max = (int) parseDouble(getArg(args, "maxLevel"), 20.0);
            return new org.gsn.pandora.skills.condition.FoodLevelCondition(min, Math.max(min, max));
        });

        // height{height|h=min;maxHeight=max}
        registerCondition("height", args -> {
            double min = parseDouble(getArg(args, "height", "h"), -64.0);
            double max = parseDouble(getArg(args, "maxHeight"), 320.0);
            return new org.gsn.pandora.skills.condition.HeightCondition(min, Math.max(min, max));
        });
        registerCondition("heightabove", args -> {
            double h = parseDouble(getArg(args, "height", "h"), 0.0);
            return new org.gsn.pandora.skills.condition.HeightCondition(h, Double.MAX_VALUE);
        });
        registerCondition("heightbelow", args -> {
            double h = parseDouble(getArg(args, "height", "h"), 0.0);
            return new org.gsn.pandora.skills.condition.HeightCondition(-Double.MAX_VALUE, h);
        });

        // hasitem{types|type|t|material|mat|m|item|i=...;amount|a=1}
        registerCondition("hasitem", args -> {
            String t = getArg(args, "types", "type", "t", "material", "mat", "m", "item", "i");
            if (t == null || t.isEmpty()) return null;
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            return new org.gsn.pandora.skills.condition.HasItemCondition(t, amount);
        });

        // hasenchantment{type|t|enchantment|ench|e=...;level|lvl|l=1}
        registerCondition("hasenchantment", args -> {
            String t = getArg(args, "type", "t", "enchantment", "ench", "e");
            if (t == null || t.isEmpty()) return null;
            int level = (int) parseDouble(getArg(args, "level", "lvl", "l"), 1.0);
            return new org.gsn.pandora.skills.condition.HasEnchantmentCondition(t, level);
        });

        // haspermission{permission|p=...}
        registerCondition("haspermission", args -> {
            String perm = getArg(args, "permission", "p");
            if (perm == null || perm.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.HasPermissionCondition(perm);
        });

        // lastdamagecause / damagecause{damagecause|cause|c=...}
        registerCondition("lastdamagecause", args -> {
            String c = getArg(args, "damagecause", "cause", "c");
            if (c == null || c.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.LastDamageCauseCondition(c);
        });
        registerCondition("damagecause", args -> {
            String c = getArg(args, "damagecause", "cause", "c");
            if (c == null || c.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.LastDamageCauseCondition(c);
        });

        // distancefromspawn{distance|d=min;maxDistance=max}
        registerCondition("distancefromspawn", args -> {
            double min = parseDouble(getArg(args, "distance", "d"), 0.0);
            double max = parseDouble(getArg(args, "maxDistance"), Double.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.DistanceFromSpawnCondition(min, Math.max(min, max));
        });

        // motionx/y/z{velocity|v=min;maxVelocity=max}
        registerCondition("motionx", args -> {
            double min = parseDouble(getArg(args, "velocity", "v"), -Double.MAX_VALUE);
            double max = parseDouble(getArg(args, "maxVelocity"), Double.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.MotionCondition(
                org.gsn.pandora.skills.condition.MotionCondition.Axis.X, min, max);
        });
        registerCondition("motiony", args -> {
            double min = parseDouble(getArg(args, "velocity", "v"), -Double.MAX_VALUE);
            double max = parseDouble(getArg(args, "maxVelocity"), Double.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.MotionCondition(
                org.gsn.pandora.skills.condition.MotionCondition.Axis.Y, min, max);
        });
        registerCondition("motionz", args -> {
            double min = parseDouble(getArg(args, "velocity", "v"), -Double.MAX_VALUE);
            double max = parseDouble(getArg(args, "maxVelocity"), Double.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.MotionCondition(
                org.gsn.pandora.skills.condition.MotionCondition.Axis.Z, min, max);
        });

        // mobsinworld{amount|a=min;maxAmount=max}
        registerCondition("mobsinworld", args -> {
            int min = (int) parseDouble(getArg(args, "amount", "a"), 0.0);
            int max = (int) parseDouble(getArg(args, "maxAmount"), Integer.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.MobsInWorldCondition(min, Math.max(min, max));
        });

        // playerkills{kills|k=min;maxKills=max}
        registerCondition("playerkills", args -> {
            int min = (int) parseDouble(getArg(args, "kills", "k"), 0.0);
            int max = (int) parseDouble(getArg(args, "maxKills"), Integer.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.PlayerKillsCondition(min, Math.max(min, max));
        });

        // lunarphase{phases|phase|p=0,1,2}
        registerCondition("lunarphase", args -> {
            String p = getArg(args, "phases", "phase", "p");
            if (p == null || p.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.LunarPhaseCondition(p);
        });

        // structure{structures|s=village;radius|r=128}
        registerCondition("structure", args -> {
            String s = getArg(args, "structures", "s");
            if (s == null || s.isEmpty()) return null;
            int radius = (int) parseDouble(getArg(args, "radius", "r"), 128.0);
            return new org.gsn.pandora.skills.condition.StructureCondition(s, radius);
        });

        // cuboid{location1|loc1|l1|a=x,y,z;location2|loc2|l2|b=x,y,z;relative|r=false}
        registerCondition("cuboid", args -> {
            String l1 = getArg(args, "location1", "loc1", "l1", "a");
            String l2 = getArg(args, "location2", "loc2", "l2", "b");
            if (l1 == null || l2 == null) return null;
            boolean rel = "true".equalsIgnoreCase(getArg(args, "relative", "r"));
            return new org.gsn.pandora.skills.condition.CuboidCondition(l1, l2, rel);
        });

        // timealive{time|t=min;maxTime=max}
        registerCondition("timealive", args -> {
            int min = (int) parseDouble(getArg(args, "time", "t"), 0.0);
            int max = (int) parseDouble(getArg(args, "maxTime"), Integer.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.TimeAliveCondition(min, Math.max(min, max));
        });

        // targetnotwithin{distance|d=16}
        registerCondition("targetnotwithin", args -> {
            double d = parseDouble(getArg(args, "distance", "d"), 16.0);
            return new org.gsn.pandora.skills.condition.TargetNotWithinCondition(d);
        });

        // threatwithin{distance|d=16}
        registerCondition("threatwithin", args -> {
            double d = parseDouble(getArg(args, "distance", "d"), 16.0);
            return new org.gsn.pandora.skills.condition.ThreatWithinCondition(d);
        });

        // stringequals{value1|val1|v1=...;value2|val2|v2=...}
        registerCondition("stringequals", args -> {
            String v1 = getArg(args, "value1", "val1", "v1");
            String v2 = getArg(args, "value2", "val2", "v2");
            return new org.gsn.pandora.skills.condition.StringEqualsCondition(v1, v2);
        });
        registerCondition("stringempty", args -> {
            String v = getArg(args, "value", "val", "v");
            return (ctx, target) -> v == null || v.isEmpty();
        });
        registerCondition("stringnotempty", args -> {
            String v = getArg(args, "value", "val", "v");
            return (ctx, target) -> v != null && !v.isEmpty();
        });

        // dimension{dimension|d|environment|env=NORMAL,NETHER,THE_END}
        registerCondition("dimension", args -> {
            String d = getArg(args, "dimension", "d", "environment", "env");
            if (d == null || d.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.DimensionCondition(d);
        });

        // fallspeed{speed|s=min;maxSpeed=max}
        registerCondition("fallspeed", args -> {
            double min = parseDouble(getArg(args, "speed", "s"), 0.0);
            double max = parseDouble(getArg(args, "maxSpeed"), Double.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.FallSpeedCondition(min, Math.max(min, max));
        });

        // foodsaturation{amount|a|food|f|saturation|s=min;maxSaturation=max}
        registerCondition("foodsaturation", args -> {
            float min = (float) parseDouble(getArg(args, "amount", "a", "food", "f", "saturation", "s"), 0.0);
            float max = (float) parseDouble(getArg(args, "maxSaturation"), 20.0);
            return new org.gsn.pandora.skills.condition.FoodSaturationCondition(min, Math.max(min, max));
        });

        // blocktype{types|block|b|type|t|material|mat|m=...}
        registerCondition("blocktype", args -> {
            String t = getArg(args, "types", "block", "b", "type", "t", "material", "mat", "m");
            if (t == null || t.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.BlockTypeCondition(t);
        });

        // blocktypeinradius{types|t=...;amount|a=1;radius|r=5}
        registerCondition("blocktypeinradius", args -> {
            String t = getArg(args, "types", "type", "t", "material", "mat", "m");
            if (t == null || t.isEmpty()) return null;
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            int radius = (int) parseDouble(getArg(args, "radius", "r"), 5.0);
            return new org.gsn.pandora.skills.condition.BlockTypeInRadiusCondition(t, amount, radius);
        });

        // distancefromlocation{x=...;y=...;z=...;world=...;distance|d=min;maxDistance=max}
        registerCondition("distancefromlocation", args -> {
            double x = parseDouble(getArg(args, "x"), 0.0);
            double y = parseDouble(getArg(args, "y"), 0.0);
            double z = parseDouble(getArg(args, "z"), 0.0);
            String world = getArg(args, "world", "w");
            double min = parseDouble(getArg(args, "distance", "d"), 0.0);
            double max = parseDouble(getArg(args, "maxDistance"), Double.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.DistanceFromLocationCondition(x, y, z, world, min, Math.max(min, max));
        });

        // entityitemtype{material|mat|m|type|types|t=...}
        registerCondition("entityitemtype", args -> {
            String t = getArg(args, "material", "mat", "m", "type", "types", "t");
            if (t == null || t.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.EntityItemTypeCondition(t);
        });

        // mobsinchunk{amount|a=min;maxAmount=max}
        registerCondition("mobsinchunk", args -> {
            int min = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            int max = (int) parseDouble(getArg(args, "maxAmount"), Integer.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.MobsInChunkCondition(min, Math.max(min, max));
        });

        // mobsnearorigin{types|t=...;amount|a=1;radius|r=16}
        registerCondition("mobsnearorigin", args -> {
            String t = getArg(args, "types", "t");
            int min = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            double r = parseDouble(getArg(args, "radius", "r"), 16.0);
            return new org.gsn.pandora.skills.condition.MobsNearOriginCondition(t, min, r);
        });

        // moisturelevel{level|l|m|moistness=min;maxLevel=max}
        registerCondition("moisturelevel", args -> {
            int min = (int) parseDouble(getArg(args, "level", "l", "m", "moistness"), 0.0);
            int max = (int) parseDouble(getArg(args, "maxLevel"), 7.0);
            return new org.gsn.pandora.skills.condition.MoistureLevelCondition(min, Math.max(min, max));
        });

        // plugin{plugin|p|pl=...}
        registerCondition("plugin", args -> {
            String p = getArg(args, "plugin", "p", "pl");
            if (p == null || p.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.PluginCondition(p);
        });

        // serverversion{version|v|sv=1.21}
        registerCondition("serverversion", args -> {
            String v = getArg(args, "version", "v", "sv");
            return new org.gsn.pandora.skills.condition.ServerVersionCondition(v,
                org.gsn.pandora.skills.condition.ServerVersionCondition.Mode.EQUALS);
        });
        registerCondition("serverversionafterorequal", args -> {
            String v = getArg(args, "version", "v", "sv");
            return new org.gsn.pandora.skills.condition.ServerVersionCondition(v,
                org.gsn.pandora.skills.condition.ServerVersionCondition.Mode.AFTER_OR_EQUAL);
        });
        registerCondition("serverversionbefore", args -> {
            String v = getArg(args, "version", "v", "sv");
            return new org.gsn.pandora.skills.condition.ServerVersionCondition(v,
                org.gsn.pandora.skills.condition.ServerVersionCondition.Mode.BEFORE);
        });
        // servernmsversion — 通过 Bukkit 版本字符串近似匹配 NMS 版本
        registerCondition("servernmsversion", args -> {
            String v = getArg(args, "version", "v", "sv");
            // NMS 版本格式如 v1_21_R1，转换为 1.21 进行比较
            String semver = v == null ? "1.21" : v.replaceAll("^v", "").replace("_R\\d+$", "").replace("_", ".");
            return new org.gsn.pandora.skills.condition.ServerVersionCondition(semver,
                org.gsn.pandora.skills.condition.ServerVersionCondition.Mode.EQUALS);
        });

        // size{size=min;maxSize=max}
        registerCondition("size", args -> {
            int min = (int) parseDouble(getArg(args, "size"), 1.0);
            int max = (int) parseDouble(getArg(args, "maxSize"), Integer.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.SizeCondition(min, Math.max(min, max));
        });

        // skilloncooldown{skill|s=...}
        registerCondition("skilloncooldown", args -> {
            String s = getArg(args, "skill", "s");
            if (s == null || s.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.SkillOnCooldownCondition(s, this);
        });

        // xdiff/ydiff/zdiff{difference|diff|d=min;maxDiff=max}
        registerCondition("xdiff", args -> {
            double min = parseDouble(getArg(args, "difference", "diff", "d"), 0.0);
            double max = parseDouble(getArg(args, "maxDiff"), Double.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.CoordDiffCondition(
                org.gsn.pandora.skills.condition.CoordDiffCondition.Axis.X, min, Math.max(min, max));
        });
        registerCondition("ydiff", args -> {
            double min = parseDouble(getArg(args, "difference", "diff", "d"), 0.0);
            double max = parseDouble(getArg(args, "maxDiff"), Double.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.CoordDiffCondition(
                org.gsn.pandora.skills.condition.CoordDiffCondition.Axis.Y, min, Math.max(min, max));
        });
        registerCondition("zdiff", args -> {
            double min = parseDouble(getArg(args, "difference", "diff", "d"), 0.0);
            double max = parseDouble(getArg(args, "maxDiff"), Double.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.CoordDiffCondition(
                org.gsn.pandora.skills.condition.CoordDiffCondition.Axis.Z, min, Math.max(min, max));
        });

        // damageamount{amount|a=min;maxAmount=max}
        registerCondition("damageamount", args -> {
            double min = parseDouble(getArg(args, "amount", "a"), 0.0);
            double max = parseDouble(getArg(args, "maxAmount"), Double.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.DamageAmountCondition(min, Math.max(min, max));
        });

        // itemtype{types|type|t|material|mat|m|item|i=...}
        registerCondition("itemtype", args -> {
            String t = getArg(args, "types", "type", "t", "material", "mat", "m", "item", "i");
            if (t == null || t.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.ItemTypeCondition(t);
        });

        // lightlevelfromblocks{level|l=min;maxLevel=max}
        registerCondition("lightlevelfromblocks", args -> {
            int min = (int) parseDouble(getArg(args, "level", "l"), 0.0);
            int max = (int) parseDouble(getArg(args, "maxLevel"), 15.0);
            return new org.gsn.pandora.skills.condition.LightLevelFromBlocksCondition(min, Math.max(min, max));
        });

        // enchantinglevel{level|l=min;maxLevel=max}
        registerCondition("enchantinglevel", args -> {
            int min = (int) parseDouble(getArg(args, "level", "l"), 0.0);
            int max = (int) parseDouble(getArg(args, "maxLevel"), Integer.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.EnchantingLevelCondition(min, Math.max(min, max));
        });
        registerCondition("enchantingexperience", args -> {
            int min = (int) parseDouble(getArg(args, "amount", "a", "level", "l"), 0.0);
            int max = (int) parseDouble(getArg(args, "maxLevel"), Integer.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.EnchantingLevelCondition(min, Math.max(min, max));
        });

        // biometype{type|t=WARM,COLD,SNOWY,...}
        registerCondition("biometype", args -> {
            String t = getArg(args, "type", "t");
            if (t == null || t.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.BiomeTypeCondition(t);
        });

        // bowtension{force|f|value|v|val=0.5}
        registerCondition("bowtension", args -> {
            float force = (float) parseDouble(getArg(args, "force", "f", "value", "v", "val"), 0.5);
            return new org.gsn.pandora.skills.condition.BowTensionCondition(force);
        });

        // color{color|clr|c=WHITE}
        registerCondition("color", args -> {
            String c = getArg(args, "color", "clr", "c");
            if (c == null || c.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.ColorCondition(c);
        });

        // directionalvelocity{x=min;absx=false;y=min;absy=false;z=min;absz=false}
        registerCondition("directionalvelocity", args -> {
            String xStr = getArg(args, "x");
            String yStr = getArg(args, "y");
            String zStr = getArg(args, "z");
            boolean absX = "true".equalsIgnoreCase(getArg(args, "absx"));
            boolean absY = "true".equalsIgnoreCase(getArg(args, "absy"));
            boolean absZ = "true".equalsIgnoreCase(getArg(args, "absz"));
            Double minX = xStr != null ? parseDouble(xStr, 0.0) : null;
            Double minY = yStr != null ? parseDouble(yStr, 0.0) : null;
            Double minZ = zStr != null ? parseDouble(zStr, 0.0) : null;
            return new org.gsn.pandora.skills.condition.DirectionalVelocityCondition(
                minX, minX, absX, minY, minY, absY, minZ, minZ, absZ);
        });

        // enderdragonphase{type|types|t=CIRCLING,...}
        registerCondition("enderdragonphase", args -> {
            String t = getArg(args, "type", "types", "t");
            if (t == null || t.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.EnderDragonPhaseCondition(t);
        });

        // entitymaterialtype{material|mat|m|type|types|t=...}
        registerCondition("entitymaterialtype", args -> {
            String t = getArg(args, "material", "mat", "m", "type", "types", "t");
            if (t == null || t.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.EntityMaterialTypeCondition(t);
        });

        // globalscore{objective|o=...;entry|e=dummy;value|v=min;maxValue=max}
        registerCondition("globalscore", args -> {
            String obj = getArg(args, "objective", "o");
            if (obj == null || obj.isEmpty()) return null;
            String entry = getArg(args, "entry", "e");
            int min = (int) parseDouble(getArg(args, "value", "v"), Integer.MIN_VALUE / 2.0);
            int max = (int) parseDouble(getArg(args, "maxValue"), Integer.MAX_VALUE / 2.0);
            return new org.gsn.pandora.skills.condition.GlobalScoreCondition(
                obj, entry == null ? "dummy" : entry, min, Math.max(min, max));
        });

        // health{amount|a=min;maxAmount=max}
        registerCondition("health", args -> {
            double min = parseDouble(getArg(args, "amount", "a"), 0.0);
            double max = parseDouble(getArg(args, "maxAmount"), Double.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.HealthCondition(min, Math.max(min, max));
        });

        // itemissimilar{slot|s=HAND;item|i|material|m|mm|mythicitem=DIRT}
        registerCondition("itemissimilar", args -> {
            String slot = getArg(args, "slot", "s");
            String mat = getArg(args, "item", "i", "material", "m", "mm", "mythicitem");
            if (mat == null) mat = "DIRT";
            return new org.gsn.pandora.skills.condition.ItemIsSimilarCondition(slot, mat);
        });

        // level{level|l=min;maxLevel=max}
        registerCondition("level", args -> {
            int min = (int) parseDouble(getArg(args, "level", "l"), 0.0);
            int max = (int) parseDouble(getArg(args, "maxLevel"), Integer.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.LevelCondition(min, Math.max(min, max));
        });

        // localdifficulty{difficulty|diff|d=NORMAL,...}
        registerCondition("localdifficulty", args -> {
            String d = getArg(args, "difficulty", "diff", "d");
            if (d == null || d.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.LocalDifficultyCondition(d);
        });

        // stance{stance|s=SNEAKING;strict|str=false}
        registerCondition("stance", args -> {
            String s = getArg(args, "stance", "s");
            if (s == null || s.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.StanceCondition(s);
        });

        // threatnotwithin{distance|d=16;amount|a=1}
        registerCondition("threatnotwithin", args -> {
            double d = parseDouble(getArg(args, "distance", "d"), 16.0);
            int amount = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            return new org.gsn.pandora.skills.condition.ThreatNotWithinCondition(d, amount);
        });

        // triggerblocktype{types|type|t|material|mat|m=...}
        registerCondition("triggerblocktype", args -> {
            String t = getArg(args, "types", "type", "t", "material", "mat", "m");
            if (t == null || t.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.TriggerBlockTypeCondition(t);
        });

        // triggeritemtype{types|type|t|material|mat|m=...}
        registerCondition("triggeritemtype", args -> {
            String t = getArg(args, "types", "type", "t", "material", "mat", "m");
            if (t == null || t.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.TriggerItemTypeCondition(t);
        });

        // damagetag{tag|t=<tagName>}
        registerCondition("damagetag", args -> {
            String tag = getArg(args, "tag", "t");
            if (tag == null || tag.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.DamageTagCondition(tag);
        });

        // distancefromtrackedlocation{distance|d=min;maxDistance=max}
        registerCondition("distancefromtrackedlocation", args -> {
            double min = parseDouble(getArg(args, "distance", "d"), 0.0);
            double max = parseDouble(getArg(args, "maxDistance"), Double.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.DistanceFromTrackedLocationCondition(min, Math.max(min, max));
        });

        // targets{amount|a=min;maxAmount=max;radius|r=16}
        registerCondition("targets", args -> {
            int min = (int) parseDouble(getArg(args, "amount", "a"), 1.0);
            int max = (int) parseDouble(getArg(args, "maxAmount"), Integer.MAX_VALUE);
            double r = parseDouble(getArg(args, "radius", "r"), 16.0);
            return new org.gsn.pandora.skills.condition.TargetsCondition(min, Math.max(min, max), r);
        });

        // signal{signal|s|sig=<信号名>} — 配合 sendsignal / onSignal
        registerCondition("signal", args -> {
            String sig = getArg(args, "signal", "s", "sig");
            if (sig == null || sig.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.SignalCondition(sig);
        });

        // variable{variable|var|v|name=<名>;value|val=<期望值>;global|g=false}
        registerCondition("variable", args -> {
            String name = getArg(args, "variable", "var", "v", "name");
            String value = getArg(args, "value", "val");
            boolean global = "true".equalsIgnoreCase(getArg(args, "global", "g"));
            if (name == null || name.isEmpty()) return null;
            return new org.gsn.pandora.skills.condition.VariableCondition(name, value, global);
        });

        // mobsinlevel / moblevel{level|l=min;maxLevel=max} — Pandora 等级范围
        registerCondition("moblevel", args -> {
            int min = (int) parseDouble(getArg(args, "level", "l"), 0.0);
            int max = (int) parseDouble(getArg(args, "maxLevel"), Integer.MAX_VALUE);
            return new org.gsn.pandora.skills.condition.MobLevelCondition(min, max);
        });
    }

    /** 按优先顺序从 args 中取第一个非 null 的值，全部缺失时返回 null。 */
    private String getArg(Map<String, String> args, String... keys) {
        for (String key : keys) {
            String v = args.get(key);
            if (v != null) return v;
        }
        return null;
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
                if (line != null) lines.add(line);
            }
            // 解析 Js 字段
            String jsScript = null;
            String jsFunction = "execute";
            ConfigurationSection jsSection = section.getConfigurationSection("Js");
            if (jsSection != null) {
                jsScript = jsSection.getString("Script");
                String func = jsSection.getString("Function");
                if (func != null && !func.isEmpty()) jsFunction = func;
            }
            // 技能名统一存为小写，冷却键直接使用 skill.getName() 无需再转换
            String idLower = id.toLowerCase(Locale.ROOT);
            Skill skill = new Skill(idLower, cooldown, lines, jsScript, jsFunction);
            skills.put(idLower, skill);
        }

        plugin.getLogger().info("已加载 " + skills.size() + " 个技能。");
    }

    public boolean isOnCooldown(String skillName, LivingEntity entity) {
        if (skillName == null || entity == null) return false;
        Skill skill = skills.get(skillName.toLowerCase(Locale.ROOT));
        if (skill == null || skill.getCooldownSeconds() <= 0) return false;
        // 技能名已在 reload() 时存为小写，直接用 skill.getName()
        String key = skill.getName() + ":" + entity.getUniqueId();
        Long last = cooldowns.get(key);
        if (last == null) return false;
        return (System.currentTimeMillis() - last) / 1000.0 < skill.getCooldownSeconds();
    }

    public void castSkill(String name, SkillExecutionContext baseContext) {
        if (name == null || baseContext == null) return;
        Skill skill = skills.get(name.toLowerCase(Locale.ROOT));
        if (skill == null) return;

        LivingEntity caster = baseContext.getCaster();
        if (caster != null && skill.getCooldownSeconds() > 0) {
            // 技能名已在 reload() 时存为小写，直接用 skill.getName()
            String key = skill.getName() + ":" + caster.getUniqueId();
            long now = System.currentTimeMillis();
            Long last = cooldowns.get(key);
            if (last != null && (now - last) / 1000.0 < skill.getCooldownSeconds()) return;
            cooldowns.put(key, now);
        }

        // 优先尝试 Js 整体执行
        if (skill.hasJsScript() && scriptingService != null && scriptingService.isEnabled()) {
            java.util.Map<String, Object> bindings = new java.util.HashMap<>();
            bindings.put("ctx", baseContext);
            bindings.put("skill", skill);
            bindings.put("caster", baseContext.getCaster());
            bindings.put("target", baseContext.getTarget());
            Object result = scriptingService.call(skill.getJsScript(), skill.getJsFunction(), bindings);
            // JS 返回 false 时退回 YAML 行；其他情况（true / null / 异常）视为已处理
            if (!(Boolean.FALSE.equals(result))) return;
        }

        // 退回 YAML 技能行
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
            targets.removeIf(target ->
                conditions.stream().anyMatch(c -> !c.matches(ctx, target)));
            if (targets.isEmpty()) return;
        }

        try {
            mechanic.execute(ctx, targets);
        } catch (Exception ex) {
            plugin.getLogger().log(Level.WARNING,
                "执行技能行时发生异常, mechanic=" + line.getMechanicName(), ex);
        }
    }
}
