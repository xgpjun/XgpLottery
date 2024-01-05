package cn.xgpjun.xgplottery2.command.sub

import cn.xgpjun.xgplottery2.command.filter
import cn.xgpjun.xgplottery2.manager.LotteryManager
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.MessageL
import cn.xgpjun.xgplottery2.manager.setTag
import cn.xgpjun.xgplottery2.send
import cn.xgpjun.xgplottery2.utils.give
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object Give :TabExecutor{
    /**
     * usage
     * /xl give [Player] key lotteryName amount
     * */
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): MutableList<String>? {
        return when(args.size){
            2-> null
            3-> mutableListOf("key").filter(args)
            4-> LotteryManager.lotteryMap.keys.toMutableList().filter(args)
            else -> arrayListOf()
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.isOp){
            return true
        }
        if (args.size>3){
            val amount = (args.getOrNull(4)?:"1").toInt()
            args.getOrNull(3)?.let { lotteryName ->
                LotteryManager.getLottery(lotteryName)?.let { lottery ->
                    lottery.key.clone().let { key ->
                        val k = key.setTag("XL2KEY", lottery.virtualKeyName)
                        Bukkit.getPlayer(args[1])?.let {
                            k.give(it,amount)
                        }?: Message.NotFoundPlayer.get().send(sender)
                    }
                }?:Message.NotFoundLottery.get().send(sender)
            }
        }else{
            help(sender)
        }
        return true
    }

    fun help(sender: CommandSender){
        if (!sender.isOp){
            return
        }
        MessageL.GiveHelp.get().send(sender)
    }
}