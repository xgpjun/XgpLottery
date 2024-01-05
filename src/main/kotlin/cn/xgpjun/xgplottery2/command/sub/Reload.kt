package cn.xgpjun.xgplottery2.command.sub

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.send
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

object Reload :TabExecutor{
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String> {
        return arrayListOf()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp){
            return true
        }
        XgpLottery.instance.reload()
        Message.Success.get().send(sender)
        return true
    }
}