package cn.xgpjun.xgplottery2.utils

import cn.xgpjun.xgplottery2.manager.NMSManager
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class VersionAdapterUtils {
    companion object{
        fun ifMainHand(e: PlayerInteractEvent): Boolean {
            return if (NMSManager.versionToInt < 9) true else EquipmentSlot.HAND == e.hand
        }

        fun setItemInMainHand(player: Player, itemStack: ItemStack?) {
            if (NMSManager.versionToInt < 9) {
                player.setItemInHand(itemStack)
            } else {
                player.inventory.setItemInMainHand(itemStack)
            }
        }

        fun getItemInMainHand(player: Player): ItemStack? {
            return if (NMSManager.versionToInt < 9) player.itemInHand else player.inventory.itemInMainHand
        }

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