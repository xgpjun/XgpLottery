package cn.xgpjun.xgplottery2.command.sub

import cn.xgpjun.xgplottery2.command.filter
import cn.xgpjun.xgplottery2.manager.DatabaseManager
import cn.xgpjun.xgplottery2.manager.DatabaseManager.save
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.MessageL
import cn.xgpjun.xgplottery2.send
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object Count:TabExecutor {
    /**
     * /xl count inquire player
     * /xl count clear player (confirm)
     *
     */
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): MutableList<String>? {
        return when(args.size){
            2 -> if (sender.isOp) mutableListOf("inquire","clear").filter(args) else mutableListOf("inquire").filter(args)
            3 -> null
            4 -> if(args[2] == "clear"&&sender.isOp) mutableListOf("confirm").filter(args) else arrayListOf()
            else -> arrayListOf()
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(args.size<3){
            help(sender)
            return true
        }
        val player = Bukkit.getOfflinePlayer(args[2])

        if (!sender.isOp&&(sender is Player && player.uniqueId!=sender.uniqueId)){
            return true
        }

        val data = DatabaseManager.getPlayerData(player.uniqueId)

        when(args.getOrNull(1)){
            "inquire" -> {
                data.individualPoolDrawCount.forEach{
                    Message.CountInquire.get(it.key,it.value.toString()).send(sender)
                }
                data.totalDrawCount.let {
                    Message.CountInquire.get("total",it.toString()).send(sender)
                }
            }
            "clear" -> {
                if (!sender.isOp){
                    return true
                }
                if (args.getOrNull(3).equals("confirm",true)){
                    Message.CountClearConfirm.get(args[2]).send(sender)
                }else{
                    data.individualPoolDrawCount.clear()
                    data.totalDrawCount = 0
                    if (player.isOnline){
                        DatabaseManager.onlinePlayerData[player.uniqueId] = data
                    }else{
                        data.save()
                    }
                    Message.Success.get().send(sender)
                }
            }
        }
        return true
    }
    fun help(sender: CommandSender){
        if (!sender.isOp){
            return
        }
        MessageL.CountHelp.get().send(sender)
    }
}