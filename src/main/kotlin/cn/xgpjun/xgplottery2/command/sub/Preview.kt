package cn.xgpjun.xgplottery2.command.sub

import cn.xgpjun.xgplottery2.command.filter
import cn.xgpjun.xgplottery2.gui.impl.PreviewGui
import cn.xgpjun.xgplottery2.manager.LotteryManager
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.MessageL
import cn.xgpjun.xgplottery2.send
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object Preview :TabExecutor{
    /**
     * xl preview xxx
     */
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): MutableList<String> {
        return when(args.size){
            2-> ArrayList(LotteryManager.lotteryMap.keys).filter(args)
            else -> arrayListOf()
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player){
            Message.OnlyPlayer.get().send(sender)
            return true
        }

        args.getOrNull(1)?.let {
            LotteryManager.getLottery(it)?.let { lottery ->
                PreviewGui(lottery,sender).openGui()
            }?:Message.NotFoundLottery.get().send(sender)
        }?: help(sender)
        return true
    }

    fun help(sender: CommandSender){
        MessageL.PreviewHelp.get().send(sender)
    }
}