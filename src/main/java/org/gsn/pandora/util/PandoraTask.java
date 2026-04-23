package org.gsn.pandora.util;

/**
 * 可取消的调度任务抽象，兼容 Paper BukkitTask 和 Folia ScheduledTask。
 */
@FunctionalInterface
public interface PandoraTask {
    void cancel();
}
