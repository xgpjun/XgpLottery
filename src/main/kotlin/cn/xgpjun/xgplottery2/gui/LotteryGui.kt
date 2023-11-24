package cn.xgpjun.xgplottery2.gui

import cn.xgpjun.xgplottery2.enums.PresetItem
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

abstract class LotteryGui(val previousInventory:InventoryHolder?) :InventoryHolder{
    abstract val inv:Inventory

    abstract fun handleClick(e:InventoryClickEvent)
    abstract fun reloadGui()
    protected var slot =
        intArrayOf(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43)
    protected var border =
        intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53)
    var page = 1
    var size = 1


    fun getPreviousInventory(player:Player){
        previousInventory?.let { player.openInventory(it.inventory) }?:{
            player.closeInventory()
        }
    }



    fun pageItem(){
        if(page!=size){
            inv.setItem(53, PresetItem.NEXT_PAGE.getItem())
        }
        if (page!=1){
            inv.setItem(45, PresetItem.PREVIOUS_PAGE.getItem())
        }
    }
}