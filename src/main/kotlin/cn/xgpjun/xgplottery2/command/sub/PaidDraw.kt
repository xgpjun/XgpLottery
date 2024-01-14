package cn.xgpjun.xgplottery2.command.sub

import cn.xgpjun.xgplottery2.command.filter
import cn.xgpjun.xgplottery2.manager.DrawManager
import cn.xgpjun.xgplottery2.manager.LotteryManager
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.MessageL
import cn.xgpjun.xgplottery2.send
import cn.xgpjun.xgplottery2.utils.Config
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

object PaidDraw:TabExecutor {
    /**
     * /xl paiddraw player lottery (multiple)
     */
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): MutableList<String>? {
        return when(args.size){
            2 -> null
            3 -> ArrayList(LotteryManager.lotteryMap.keys).filter(args)
            4 -> arrayListOf("multiple").filter(args)
            else -> arrayListOf()
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.isOp){
            return true
        }
        if (!Config.readWiki(sender)){
            return true
        }
        args.getOrNull(2)?.let { lotteryName->
            LotteryManager.getLottery(lotteryName)?.let {lottery ->
                Bukkit.getPlayer(args[1])?.let { player ->
                    DrawManager.draw(player,lottery, isConsumeKeys = true ,isMultiple = args.getOrNull(3).equals("multiple"))
                }?:Message.NotFoundPlayer.get().send(sender)
            }?:Message.NotFoundLottery.get().send(sender)
        }?: help(sender)

        return true
    }
    fun help(sender: CommandSender){
        if (!sender.isOp){
            return
        }
        MessageL.PaidDrawHelp.get().send(sender)
    }
}