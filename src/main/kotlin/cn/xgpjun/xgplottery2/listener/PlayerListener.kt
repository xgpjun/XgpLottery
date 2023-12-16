package cn.xgpjun.xgplottery2.listener

import cn.xgpjun.xgplottery2.manager.DatabaseManager
import cn.xgpjun.xgplottery2.manager.DatabaseManager.save
import cn.xgpjun.xgplottery2.manager.SchedulerManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerListener:Listener {
    @EventHandler
    fun join(e:PlayerJoinEvent){
        SchedulerManager.getScheduler().runTaskAsynchronously{
            e.player.uniqueId.let {
                DatabaseManager.onlinePlayerData[it] = DatabaseManager.getPlayerData(it)
            }
        }
    }

    @EventHandler
    fun quit(e:PlayerQuitEvent){
        val uuid = e.player.uniqueId
        SchedulerManager.getScheduler().runTaskAsynchronously{
            DatabaseManager.onlinePlayerData[uuid]?.save()
            DatabaseManager.onlinePlayerData.remove(uuid)
        }

    }
}

interface Runnable2:Runnable{

    fun cancel(){

    }
}