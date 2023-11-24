package cn.xgpjun.xgplottery2.command.sub

import cn.xgpjun.xgplottery2.gui.impl.Manage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object Manage:TabExecutor {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ): MutableList<String> {

        return ArrayList()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val player = sender as Player
        player.openInventory(Manage().getInventory())
        return true
    }
}