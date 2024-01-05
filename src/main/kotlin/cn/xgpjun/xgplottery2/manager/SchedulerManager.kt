package cn.xgpjun.xgplottery2.manager

import cn.xgpjun.xgplottery2.common.foliaSupport.enum.ServerType
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Scheduler
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.scheduler.BukkitScheduler
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.scheduler.FoliaScheduler
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.java.JavaPlugin

object SchedulerManager {
    private lateinit var plugin:JavaPlugin
    lateinit var serverType: ServerType
    fun register(plugin:JavaPlugin){
        this.plugin = plugin
        serverType = ServerType.getServerType()
    }

    /**
     * 传送实体
     * @param entity 需要传送的实体
     * @param target 目的地
     * @return 传送结果
     */
    fun teleport(entity: Entity, target: Location): Boolean {
        return teleport(entity, target, PlayerTeleportEvent.TeleportCause.PLUGIN)
    }

    /**
     * 传送实体
     * @param entity 需要传送的实体
     * @param target 传送目的地
     * @param cause 传送原因
     * @return 传送结果
     */
    fun teleport(entity: Entity, target: Location, cause: PlayerTeleportEvent.TeleportCause): Boolean {
        return if (serverType == ServerType.FOLIA) {
            entity.teleportAsync(target, cause).isDone
        } else entity.teleport(target, cause)
    }

    /**
     * 获得调度程序管理器, 参数仅影在Folia中的实现, 无参代表getAsyncScheduler()。
     * @return 调度管理器
     */
    fun getScheduler(): Scheduler {
        return if (serverType == ServerType.FOLIA) {
            FoliaScheduler(plugin, false)
        } else BukkitScheduler(plugin)
    }

    /**
     * 获得调度程序管理器, 参数仅影在Folia中的实现, 此处获得实体调度器.
     * @param entity 操作的实体
     * @param retired 回调函数. 当执行时实体变为null时执行的方法。
     * @return 调度管理器
     */
    fun getScheduler(entity: Entity, retired: Runnable): Scheduler {
        return if (serverType == ServerType.FOLIA) {
            FoliaScheduler(plugin, entity, retired)
        } else BukkitScheduler(plugin)
    }

    /**
     * 获得调度程序管理器, 参数仅影在Folia中的实现, 此处获得区块调度器.
     * @param location 区域的位置
     * @return 调度管理器
     */
    fun getScheduler(location: Location): Scheduler {
        return if (serverType == ServerType.FOLIA) {
            FoliaScheduler(plugin, location)
        } else BukkitScheduler(plugin)
    }

    /**
     * 获得调度程序管理器, 参数仅影在Folia中的实现, 此处获得全局调度器.
     * @param isGlobal 是否为全局， 如为false则等同无参。
     * @return 调度管理器
     */
    fun getScheduler(isGlobal: Boolean): Scheduler {
        return if (serverType == ServerType.FOLIA) {
            FoliaScheduler(plugin, isGlobal)
        } else BukkitScheduler(plugin)
    }
}