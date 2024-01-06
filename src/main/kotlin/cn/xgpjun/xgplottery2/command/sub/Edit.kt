package cn.xgpjun.xgplottery2.command.sub

import cn.xgpjun.xgplottery2.command.filter
import cn.xgpjun.xgplottery2.gui.impl.AwardGui
import cn.xgpjun.xgplottery2.gui.impl.EditGui
import cn.xgpjun.xgplottery2.lottery.enums.SellType
import cn.xgpjun.xgplottery2.manager.AnimManager
import cn.xgpjun.xgplottery2.manager.LotteryManager
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.toNBTString
import cn.xgpjun.xgplottery2.send
import cn.xgpjun.xgplottery2.utils.Config
import cn.xgpjun.xgplottery2.utils.getItemInMainHand
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object Edit: TabExecutor {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): MutableList<String> {
        return when(args.size){
            2 ->  ArrayList(LotteryManager.lotteryMap.keys).filter(args)
            3 -> mutableListOf("singleAnim","multipleAnim","sellType")
            else -> ArrayList()
        }

    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp){
            return true
        }
        if (!Config.readWiki(sender)){
            return true
        }
        /**
         * xl edit []
         */
        val lottery = args?.getOrNull(1)?.let {
            LotteryManager.lotteryMap[it]
        } ?: return true
        if (sender is Player){
            if(args.size==2){
                sender.openInventory(EditGui(null,lottery).inventory)
                return true
            }else{
                EditGui.preInventory[sender.uniqueId]?.let {
                    EditGui.preInventory.remove(sender.uniqueId)
                    sender.openInventory(it.inventory)
                }
            }
        }
        val target = args.getOrNull(3)
        when(args.getOrNull(2)){
            "singleAnim" ->{
                target?.let {
                    AnimManager.singleAnim[target]?.let {
                        lottery.animation = target
                    }
                }

            }
            "multipleAnim" ->{
                target?.let {
                    AnimManager.multipleAnim[target]?.let {
                        lottery.multipleAnimation = target
                    }
                }
            }
            "sellType" ->{
                target?.let {
                    SellType.fromString(target).let {
                        if (it != SellType.NULL){
                            lottery.sellType = it
                        }
                    }
                }
            }
            "award" ->{
                lottery.awards[target]?.let {
                    if (sender is Player){
                        val newItem = sender.getItemInMainHand().clone()
                        if (newItem.itemMeta != null){
                            it.item = newItem
                            lottery.yaml.set("awards.${it.name}.item",null)
                            lottery.yaml.set("awards.${it.name}.item.internal",it.item.toNBTString())
                            lottery.yaml.save(lottery.file)
                            Message.Success.get().send(sender)
                            AwardGui.preInventory[sender.uniqueId]?.let { pre->
                                AwardGui.preInventory.remove(sender.uniqueId)
                                sender.openInventory(pre.inventory)
                            }
                        }
                    }
                }
            }
        }
        return true
    }
}