package cn.xgpjun.xgplottery2.command.sub

import cn.xgpjun.xgplottery2.manager.LotteryManager
import cn.xgpjun.xgplottery2.manager.MessageL
import cn.xgpjun.xgplottery2.manager.setTag
import cn.xgpjun.xgplottery2.send
import net.md_5.bungee.api.ChatMessageType
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object Help :TabExecutor{
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ): MutableList<String>? {
        TODO("Not yet implemented")
    }


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        MessageL.HelpMessage.get().send(sender)


        return true
    }
}