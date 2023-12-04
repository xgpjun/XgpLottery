package cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.scheduler

import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Scheduler
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.task.BukkitTask
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class BukkitScheduler(private val plugin: Plugin): Scheduler {

    override fun runTask(runnable: Runnable): Task {
        return BukkitTask(Bukkit.getScheduler().runTask(plugin, runnable), false)
    }

    override fun runTaskLater(delay: Long,runnable: Runnable): Task {
        return BukkitTask(Bukkit.getScheduler().runTaskLater(plugin, runnable, delay), false)
    }

    override fun runTaskTimer(delay: Long, period: Long,runnable: Runnable): Task {
        return BukkitTask(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period), true)
    }

    override fun runTaskAsynchronously(runnable: Runnable): Task {
        return BukkitTask(Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable), false)
    }

    override fun runTaskLaterAsynchronously(delay: Long,runnable: Runnable): Task {
        return BukkitTask(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay), false)
    }

    override fun runTaskTimerAsynchronously(delay: Long, period: Long,runnable: Runnable, ): Task {
        return BukkitTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period), true)
    }
}