package cn.xgpjun.xgplottery2.utils

import cn.xgpjun.xgplottery2.manager.NMSManager
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class VersionAdapterUtils {
    companion object{
        fun getPlayerEmptySlot(player: Player): Int {
            val items = player.inventory.contents
            var emptySlots = 0
            for (i in items) {
                if (i == null) {
                    emptySlots++
                }
            }
            val equipment = player.equipment!!.armorContents
            for (i in equipment) {
                if (i == null) {
                    emptySlots--
                }
            }
            return if (NMSManager.versionToInt < 9) {
                emptySlots
            } else {
                if (player.inventory.itemInOffHand.itemMeta == null) emptySlots--
                emptySlots
            }
        }
    }
}

fun Player.setItemInMainHand(itemStack: ItemStack?) {
    if (NMSManager.versionToInt < 9) {
        this.setItemInHand(itemStack)
    } else {
        this.inventory.setItemInMainHand(itemStack)
    }
}

fun Player.getItemInMainHand(): ItemStack {
    return if (NMSManager.versionToInt < 9) this.itemInHand else this.inventory.itemInMainHand
}

fun PlayerInteractEvent.isMainHand():Boolean{
    return if (NMSManager.versionToInt < 9) true else EquipmentSlot.HAND == this.hand
}