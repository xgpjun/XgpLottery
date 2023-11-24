package cn.xgpjun.xgplottery2.listener

import cn.xgpjun.xgplottery2.gui.LotteryGui
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent


object GuiLimit :Listener{
    @EventHandler
    fun guiClick(e:InventoryClickEvent){
        val holder = e.inventory.holder
        if(holder is LotteryGui){
            e.isCancelled = true
            holder.handleClick(e)
        }
    }
}