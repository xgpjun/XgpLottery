package cn.xgpjun.xgplottery2.gui.impl

import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.gui.LotteryGui
import cn.xgpjun.xgplottery2.enums.PresetItem
import cn.xgpjun.xgplottery2.manager.*
import cn.xgpjun.xgplottery2.utils.MyItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

class Manage :LotteryGui(null){
    override val inv: Inventory = Bukkit.createInventory(this,54,Message.ManageTitle.get().color())

    override fun handleClick(e: InventoryClickEvent) {
        if (e.isLeftClick&&!e.isShiftClick){
            val item =inv.getItem(e.rawSlot);
            if (item!=null&&item.hasItemMeta()){
                val lotteryName = item.getTag("LotteryNameGui")
                LotteryManager.lotteryMap[lotteryName]?.let {
                    e.whoClicked.openInventory(EditGui(this,it).inventory)
                }
            }
        }
    }

    override fun reloadGui() {
        size = 1.coerceAtLeast(LotteryManager.lotteryMap.size / 28)
        page = 1.coerceAtLeast(page.coerceAtMost(size))

        pageItem()

        border.forEach { inv.setItem(it, PresetItem.BORDER_GLASS.getItem()) }
        var index = 0
        LotteryManager.lotteryMap.forEach{ (name, _) ->
            inv.setItem(slot[index+((page-1)*28)],
                MyItemBuilder(Material.CHEST)
                    .setDisplayName("&a奖池名称:&9$name".color())
                    .getItem().setTag("LotteryNameGui",name))

            if (index++==28) return@forEach
        }
    }

    override fun getInventory(): Inventory {
        reloadGui()
        return inv
    }

}

