package cn.xgpjun.xgplottery2.lottery.pojo

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.hook.PlaceholderAPIHook
import cn.xgpjun.xgplottery2.hook.setPlaceholder
import cn.xgpjun.xgplottery2.utils.give
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

data class Award(
        var name:String,
        var item:ItemStack,
        var giveItem:Boolean,
        var weight:Int,
        var rawItem:Boolean,
        var commands:MutableList<String>,
        var customTag:MutableMap<String,Any>){

        companion object{
                fun create(name: String):Award{
                        return Award(name, ItemStack(Material.STONE),
                                giveItem = true,
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
                                val cmd = if (PlaceholderAPIHook.enable) {
                                        command.setPlaceholder(player)
                                }else{
                                        command
                                }
                                XgpLottery.instance.server.dispatchCommand(XgpLottery.instance.server.consoleSender,cmd)
                        }
                }
                if (giveItem){
                        item.give(player)
                }
        }

        fun toDisplayItem():ItemStack{
                return item.clone()
        }
}

