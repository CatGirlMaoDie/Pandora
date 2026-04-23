package org.gsn.pandora.skills.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;

import java.util.List;

/**
 * 执行控制台或玩家命令。
 * command|cmd|c=...  asCaster|ac=false  asTarget|at=false  asOp|op=false
 */
public class CommandMechanic implements Mechanic {

    private final String command;
    private final boolean asCaster;
    private final boolean asTarget;
    private final boolean asOp;

    public CommandMechanic(String command, boolean asCaster, boolean asTarget, boolean asOp) {
        this.command = command == null ? "" : command;
        this.asCaster = asCaster;
        this.asTarget = asTarget;
        this.asOp = asOp;
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (command.isEmpty()) return false;

        if (asTarget && targets != null) {
            for (LivingEntity target : targets) {
                if (!(target instanceof Player)) continue;
                Player player = (Player) target;
                Player casterPlayer = context.getCaster() instanceof Player p ? p : null;
                String cmd = replacePlaceholders(command, casterPlayer, player);
                dispatchAs(player, cmd);
            }
            return true;
        }

        if (asCaster && context.getCaster() instanceof Player) {
            Player player = (Player) context.getCaster();
            String cmd = replacePlaceholders(command, player, context.getTarget() instanceof Player ? (Player) context.getTarget() : null);
            dispatchAs(player, cmd);
            return true;
        }

        // 控制台执行
        LivingEntity caster = context.getCaster();
        LivingEntity target = context.getTarget();
        String cmd = replacePlaceholders(command,
            caster instanceof Player ? (Player) caster : null,
            target instanceof Player ? (Player) target : null);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        return true;
    }

    private void dispatchAs(Player player, String cmd) {
        boolean wasOp = player.isOp();
        if (asOp && !wasOp) player.setOp(true);
        try {
            player.performCommand(cmd);
        } finally {
            if (asOp && !wasOp) player.setOp(false);
        }
    }

    private String replacePlaceholders(String cmd, Player caster, Player target) {
        if (caster != null) cmd = cmd.replace("<caster>", caster.getName()).replace("<caster.name>", caster.getName());
        if (target != null) cmd = cmd.replace("<target>", target.getName()).replace("<target.name>", target.getName());
        return cmd;
    }
}
