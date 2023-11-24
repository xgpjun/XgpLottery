package cn.xgpjun.xgplottery2.lottery.pojo

import cn.xgpjun.xgplottery2.XgpLottery
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

data class Award(
        var name:String,
        var item:ItemStack,
        var giveItem:Boolean,
        var broadcast:Boolean,
        var weight:Int,
        var rawItem:Boolean,
        var commands:MutableList<String>,
        var customTag:MutableMap<String,Any>){
    // TODO: method

        companion object{
                fun create(name: String):Award{
                        return Award(name, ItemStack(Material.STONE),
                                giveItem = true,
                                broadcast = false,
                                weight = 1,
                                rawItem = false,
                                commands = ArrayList(),
                                customTag = HashMap()
                        )
                }
        }

        fun give(humanEntity: HumanEntity){
                val player = humanEntity as Player
                if (commands.isNotEmpty()){
                        for (command in commands) {
                                val cmd = if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                                        PlaceholderAPI.setPlaceholders(player, command)
                                }else{
                                        command
                                }
                                XgpLottery.instance.server.dispatchCommand(XgpLottery.instance.server.consoleSender,cmd)
                        }
                }
                if (giveItem){
                        val map: Map<Int, ItemStack> = player.inventory.addItem(item.clone())
                        if (map.isEmpty()) return
                        for (item in map.values) {
                                player.world.dropItemNaturally(player.location, item)
                        }
                }
        }

        fun toDisplayItem():ItemStack{
                return item.clone()
        }
}

