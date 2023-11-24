package cn.xgpjun.xgplottery2.listener

import cn.xgpjun.xgplottery2.lottery.anim.multiple.MultipleAnim
import cn.xgpjun.xgplottery2.lottery.anim.single.SingleAnim
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerDropItemEvent

object AnimListener:Listener {

    @EventHandler
    fun giveAward(e:InventoryCloseEvent){
        val holder = e.inventory.holder
        if (holder is SingleAnim){
            holder.finish()
            holder.award?.give(e.player)
        }
    }


    @EventHandler
    fun giveAwardM(e:InventoryCloseEvent){
        val holder = e.inventory.holder
        if (holder is MultipleAnim){
            holder.awards.forEach{
                it.give(e.player)
            }
        }
    }

    @EventHandler
    fun guiClick(e:InventoryClickEvent){
        val holder = e.inventory.holder
        if (holder is SingleAnim||holder is MultipleAnim){
            e.isCancelled = true
        }
    }

    @EventHandler
    fun dropItemEvent(e: PlayerDropItemEvent) {
        val holder = e.player.openInventory.topInventory.holder
        if (holder is SingleAnim||holder is MultipleAnim){
            e.isCancelled = true
        }
    }

}