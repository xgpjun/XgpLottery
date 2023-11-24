package cn.xgpjun.xgplottery2.command.sub

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

object Count:TabExecutor {
    /**
     * /xl count inquire player %target%
     * /xl count add player %target% amount
     * /xl count sub player %target% amount
     * /xl count set player %target% amount
     * /xl count clear player (confirm)
     *
     * %target% : totalDrawCount/lotteryName
     */
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ): MutableList<String>? {
        TODO("Not yet implemented")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        TODO("Not yet implemented")
    }
}