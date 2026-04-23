package org.gsn.pandora.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

/**
 * 调度器抽象工具，运行时检测 Folia 并自动切换 API。
 * Paper 上委托 BukkitScheduler，Folia 上委托 entity/global region scheduler。
 */
public final class FoliaScheduler {

    private static final boolean IS_FOLIA;

    static {
        boolean folia;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException e) {
            folia = false;
        }
        IS_FOLIA = folia;
    }

    private FoliaScheduler() {}

    public static boolean isFolia() {
        return IS_FOLIA;
    }

    /**
     * 实体绑定延迟任务。实体失效时任务自动取消。
     */
    public static PandoraTask runEntityTaskLater(JavaPlugin plugin, Entity entity,
                                                  Runnable task, long delayTicks) {
        if (IS_FOLIA) {
            var st = entity.getScheduler().runDelayed(plugin,
                scheduledTask -> task.run(), null, Math.max(1L, delayTicks));
            return st == null ? () -> {} : st::cancel;
        } else {
            BukkitTask bt = Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
            return bt::cancel;
        }
    }

    /**
     * 实体绑定重复任务。回调接收 PandoraTask 以便内部取消。
     */
    public static PandoraTask runEntityTaskTimer(JavaPlugin plugin, Entity entity,
                                                  Consumer<PandoraTask> task,
                                                  long delayTicks, long periodTicks) {
        if (IS_FOLIA) {
            var st = entity.getScheduler().runAtFixedRate(plugin, scheduledTask -> {
                PandoraTask wrapper = scheduledTask::cancel;
                task.accept(wrapper);
            }, null, Math.max(1L, delayTicks), Math.max(1L, periodTicks));
            return st == null ? () -> {} : st::cancel;
        } else {
            // Paper: 用 BukkitRunnable 模式让回调能取消自身
            final BukkitTask[] holder = new BukkitTask[1];
            holder[0] = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                PandoraTask wrapper = () -> { if (holder[0] != null) holder[0].cancel(); };
                task.accept(wrapper);
            }, delayTicks, periodTicks);
            return () -> { if (holder[0] != null) holder[0].cancel(); };
        }
    }

    /**
     * 全局重复任务（不绑定实体/区域）。
     */
    public static PandoraTask runGlobalTaskTimer(JavaPlugin plugin, Runnable task,
                                                  long delayTicks, long periodTicks) {
        if (IS_FOLIA) {
            var st = Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin,
                scheduledTask -> task.run(), Math.max(1L, delayTicks), Math.max(1L, periodTicks));
            return st::cancel;
        } else {
            BukkitTask bt = Bukkit.getScheduler().runTaskTimer(plugin, task, delayTicks, periodTicks);
            return bt::cancel;
        }
    }
}
