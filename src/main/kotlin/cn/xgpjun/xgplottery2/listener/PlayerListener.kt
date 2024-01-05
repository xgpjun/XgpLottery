package cn.xgpjun.xgplottery2.listener

import cn.xgpjun.xgplottery2.enums.Sounds
import cn.xgpjun.xgplottery2.manager.*
import cn.xgpjun.xgplottery2.manager.DatabaseManager.save
import cn.xgpjun.xgplottery2.send
import cn.xgpjun.xgplottery2.utils.getItemInMainHand
import cn.xgpjun.xgplottery2.utils.isMainHand
import cn.xgpjun.xgplottery2.utils.setItemInMainHand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

object PlayerListener:Listener {

    var pluginOutDate :String? =null
    @EventHandler
    fun join(e:PlayerJoinEvent){
        if (e.player.isOp){
            pluginOutDate?.let {
                Message.VersionOutDate.get(pluginOutDate).send(e.player)
            }
        }
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


    private val coolDown = HashMap<UUID,Long>()
    @EventHandler
    fun keyUse(e:PlayerInteractEvent){
        val player = e.player
        val items = player.getItemInMainHand()
        if (!items.hasItemMeta()){
            return
        }

        val nowTime = System.currentTimeMillis()
        val lastTime = coolDown.getOrDefault(e.player.uniqueId,nowTime - 114514)
        if (nowTime - lastTime < 50 * 4||!e.isMainHand()|| (e.action != Action.RIGHT_CLICK_AIR&&e.action != Action.RIGHT_CLICK_BLOCK)){
            return
        }
        coolDown[player.uniqueId] = nowTime
        items.getTag("XL2KEY")?.let {
            if (it == "")
                return
            e.isCancelled = true
            if (items.amount==1){
                player.setItemInMainHand(null)
            }else{
                items.amount = items.amount-1
                player.setItemInMainHand(items)
            }
            player.playSound(player.location, Sounds.PLING.get(), 1.0f, 1f)
            SchedulerManager.getScheduler().runTaskAsynchronously{
                val data = DatabaseManager.getPlayerData(player.uniqueId)
                data.keyCount[it] = data.keyCount.getOrDefault(it,0) + 1
                if (player.isOnline){
                    DatabaseManager.onlinePlayerData[player.uniqueId] = data
                }else{
                    data.save()
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun draw(e:PlayerInteractEvent){
        if (!e.isMainHand()){
            return
        }
        e.clickedBlock?.let {block->
            CrateManager.getLottery(block.location)?.let {
                e.isCancelled = true
                if (e.action == Action.RIGHT_CLICK_BLOCK){
                    if (e.player.isSneaking){
                        DrawManager.draw(e.player, it, isConsumeKeys = true, isMultiple =  true, crateLocation = block.location)
                    }else{
                        DrawManager.draw(e.player, it, isConsumeKeys = true, crateLocation = block.location)
                    }
                }else{
                    MessageL.DrawTips.get(it.name, DatabaseManager.getPlayerData(e.player.uniqueId).keyCount.getOrDefault(it.virtualKeyName,0).toString()).send(e.player)
                }
            }
        }
    }
}

