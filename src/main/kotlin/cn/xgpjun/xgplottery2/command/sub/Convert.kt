package cn.xgpjun.xgplottery2.command.sub

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

object Convert:TabExecutor {
    /**
     * /xl convert toCC
     * /xl convert fromCC
     * /xl convert fromLegacy
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