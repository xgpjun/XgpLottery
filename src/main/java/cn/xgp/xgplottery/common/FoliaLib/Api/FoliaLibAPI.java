package cn.xgp.xgplottery.common.FoliaLib.Api;


import cn.xgp.xgplottery.common.FoliaLib.Enum.ServerType;
import cn.xgp.xgplottery.common.FoliaLib.Wrapper.Scheduler;
import cn.xgp.xgplottery.common.FoliaLib.Wrapper.SchedulerWrapper.BukkitScheduler;
import cn.xgp.xgplottery.common.FoliaLib.Wrapper.SchedulerWrapper.FoliaScheduler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import java.util.Objects;


public class FoliaLibAPI {
    private final Plugin plugin;
    private final ServerType serverType;

    /**
     * 获得API实例
     * @param plugin 调用调度程序的插件实例。
     */
    public FoliaLibAPI(Plugin plugin){
        serverType = ServerType.getServerType();
        this.plugin = plugin;
    }

    /**
     * 传送实体
     * @param entity 需要传送的实体
     * @param target 目的地
     * @return 传送结果
     */
    public boolean teleport(Entity entity, Location target){
        return teleport(entity,target, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    /**
     * 传送实体
     * @param entity 需要传送的实体
     * @param target 传送目的地
     * @param cause 传送原因
     * @return 传送结果
     */
    public boolean teleport(Entity entity, Location target, PlayerTeleportEvent.TeleportCause cause){
        if (Objects.requireNonNull(serverType) == ServerType.FOLIA) {
            return entity.teleportAsync(target,cause).isDone();
        }
        return entity.teleport(target,cause);
    }

    /**
     * 获得调度程序管理器, 参数仅影在Folia中的实现, 无参代表getAsyncScheduler()。
     * @return 调度管理器
     */
    public Scheduler getScheduler(){
        if (Objects.requireNonNull(serverType) == ServerType.FOLIA) {
            return new FoliaScheduler(plugin,false);
        }
        return new BukkitScheduler(plugin);
    }

    /**
     * 获得调度程序管理器, 参数仅影在Folia中的实现, 此处获得实体调度器.
     * @param entity 操作的实体
     * @param retired 回调函数. 当执行时实体变为null时执行的方法。
     * @return 调度管理器
     */
    public Scheduler getScheduler(Entity entity,Runnable retired){
        if (Objects.requireNonNull(serverType) == ServerType.FOLIA) {
            return new FoliaScheduler(plugin,entity,retired);
        }
        return new BukkitScheduler(plugin);
    }

    /**
     *获得调度程序管理器, 参数仅影在Folia中的实现, 此处获得区块调度器.
     * @param location 区域的位置
     * @return 调度管理器
     */
    public Scheduler getScheduler(Location location){
        if (Objects.requireNonNull(serverType) == ServerType.FOLIA) {
            return new FoliaScheduler(plugin,location);
        }
        return new BukkitScheduler(plugin);
    }

    /**
     * 获得调度程序管理器, 参数仅影在Folia中的实现, 此处获得全局调度器.
     * @param isGlobal 是否为全局， 如为false则等同无参。
     * @return 调度管理器
     */
    public Scheduler getScheduler(boolean isGlobal){
        if (Objects.requireNonNull(serverType) == ServerType.FOLIA) {
            return new FoliaScheduler(plugin,isGlobal);
        }
        return new BukkitScheduler(plugin);
    }
}
