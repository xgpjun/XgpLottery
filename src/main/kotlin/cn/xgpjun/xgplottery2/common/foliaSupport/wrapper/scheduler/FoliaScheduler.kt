package cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.scheduler

import cn.xgpjun.xgplottery2.common.foliaSupport.enum.SchedulerType
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Scheduler
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.task.FoliaTask
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin
import java.util.concurrent.TimeUnit

@SuppressWarnings("unused")
class FoliaScheduler:Scheduler {
    private var schedulerType: SchedulerType? = null
    private lateinit var location: Location
    private lateinit var entity: Entity
    private lateinit var retired: Runnable
    private var plugin: Plugin
    constructor(plugin: Plugin, isGlobal: Boolean) {
        this.plugin = plugin
        schedulerType = if (isGlobal) SchedulerType.GLOBAL else SchedulerType.ASYNC
    }

    constructor(plugin: Plugin, location: Location) {
        this.plugin = plugin
        this.location = location
        schedulerType = SchedulerType.REGION
    }

    constructor(plugin: Plugin, entity: Entity, retired: Runnable) {
        this.plugin = plugin
        this.entity = entity
        this.retired = retired
        schedulerType = SchedulerType.ENTITY
    }

    override fun runTask(runnable: Runnable): Task {
        val task1 = when (schedulerType) {
            SchedulerType.ENTITY -> entity.scheduler.run(plugin, { runnable.run() }, retired)
            SchedulerType.GLOBAL -> Bukkit.getGlobalRegionScheduler().run(plugin) { runnable.run() }
            SchedulerType.REGION -> Bukkit.getRegionScheduler().run(plugin, location) { runnable.run() }
            SchedulerType.ASYNC -> Bukkit.getAsyncScheduler().runNow(plugin) { runnable.run() }
            else -> Bukkit.getAsyncScheduler().runNow(plugin) { runnable.run() }
        }
        return FoliaTask(task1)
    }

    override fun runTaskLater(delay: Long,runnable: Runnable): Task {
        val task1= when (schedulerType) {
            SchedulerType.ENTITY -> entity.scheduler.runDelayed(plugin, { runnable.run() }, retired, delay)
            SchedulerType.GLOBAL -> Bukkit.getGlobalRegionScheduler().runDelayed(plugin, { runnable.run() }, delay)
            SchedulerType.REGION -> Bukkit.getRegionScheduler().runDelayed(plugin, location, {  runnable.run() }, delay)
            SchedulerType.ASYNC -> Bukkit.getAsyncScheduler()
                .runDelayed(plugin, { runnable.run() }, delay * 50, TimeUnit.MILLISECONDS)

            else -> Bukkit.getAsyncScheduler()
                .runDelayed(plugin, {  runnable.run() }, delay * 50, TimeUnit.MILLISECONDS)
        }
        return FoliaTask(task1)
    }

    override fun runTaskTimer(delay: Long, period: Long, runnable: Runnable): Task {
        val task1 = when (schedulerType) {
            SchedulerType.ENTITY -> entity.scheduler.runAtFixedRate(plugin, { runnable.run() }, retired, delay, period)
            SchedulerType.GLOBAL -> Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, { runnable.run() }, delay, period)
            SchedulerType.REGION -> Bukkit.getRegionScheduler()
                .runAtFixedRate(plugin, location, { runnable.run() }, delay, period)

            SchedulerType.ASYNC -> Bukkit.getAsyncScheduler()
                .runAtFixedRate(plugin, { runnable.run() }, delay * 50, period * 50, TimeUnit.MILLISECONDS)

            else -> Bukkit.getAsyncScheduler()
                .runAtFixedRate(plugin, { runnable.run() }, delay * 50, period * 50, TimeUnit.MILLISECONDS)
        }
        return FoliaTask(task1)
    }

    /**
     * 在Folia中没有Bukkit中"异步"的概念
     * @param runnable 需要执行的程序
     * @return 调度任务实例
     */
    override fun runTaskAsynchronously(runnable: Runnable): Task {
        return runTask(runnable)
    }

    override fun runTaskLaterAsynchronously(delay: Long, runnable: Runnable): Task {
        return runTaskLater(delay,runnable)
    }

    override fun runTaskTimerAsynchronously(delay: Long, period: Long, runnable: Runnable): Task {
        return runTaskTimer(delay, period,runnable)
    }
}