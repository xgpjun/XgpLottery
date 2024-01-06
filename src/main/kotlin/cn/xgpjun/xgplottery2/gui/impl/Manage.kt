package cn.xgpjun.xgplottery2.gui.impl

import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.gui.LotteryGui
import cn.xgpjun.xgplottery2.manager.*
import cn.xgpjun.xgplottery2.utils.MyItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import kotlin.math.ceil
import kotlin.math.min

class Manage :LotteryGui(null){
    override val inv: Inventory = Bukkit.createInventory(this,54,Message.ManageTitle.get().color())

    override fun handleClick(e: InventoryClickEvent) {
        if (e.isLeftClick&&!e.isShiftClick){
            val item =inv.getItem(e.rawSlot)
            if (item!=null&&item.hasItemMeta()){
                val lotteryName = item.getTag("LotteryNameGui")
                LotteryManager.lotteryMap[lotteryName]?.let {
                    e.whoClicked.openInventory(EditGui(this,it).inventory)
                }
            }
        }

        if (e.rawSlot == 8)
            getPreviousInventory(e.whoClicked as Player)
    }

    override fun reloadGui() {
        inv.clear()
        size = ceil(LotteryManager.lotteryMap.size.toDouble() / 28).toInt().coerceAtLeast(1)
        page = 1.coerceAtLeast(page.coerceAtMost(size))

        pageItem()
        LotteryManager.lotteryMap
            .toList()
            .slice((page - 1) * 28 until min(page*28,LotteryManager.lotteryMap.size))
            .forEachIndexed{ index,(name, _) ->
            inv.setItem(slot[index],
                MyItemBuilder(Material.CHEST)
                    .setDisplayName("&aLotteryName:&9$name".color())
                    .getItem().setTag("LotteryNameGui",name))
            }
    }

    override fun getInventory(): Inventory {
        reloadGui()
        return inv
    }

}

