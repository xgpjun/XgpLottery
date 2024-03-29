package cn.xgpjun.xgplottery2.command.sub

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.command.MainCommand
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.MessageL
import cn.xgpjun.xgplottery2.send
import cn.xgpjun.xgplottery2.utils.Config
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object Help :TabExecutor{
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): MutableList<String> {
        return arrayListOf()
    }


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp){
             return true
        }
        if (args.getOrNull(1).equals("62a79341-7adb-4791-a1c4-d829f237f033")){
            XgpLottery.instance.config.set("uuid","62a79341-7adb-4791-a1c4-d829f237f033")
            XgpLottery.instance.saveConfig()
            Config.loadConfig()
            Message.Success.get().send(sender)
            return true
        }
        if (!Config.readWiki(sender)){
            return true
        }
        MessageL.HelpMessage.get().send(sender)
        val message = TextComponent()
        MainCommand.subCommands.keys.forEachIndexed{ index,it->
            val text = TextComponent("&7&l&n$it".color())
            text.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xgplottery2 $it")
            text.addExtra(" ")
            message.addExtra(text)
            if (index%5==4){
                message.addExtra("\n")
            }
        }
        try {
            if (sender is Player){
                sender.send(message)
            }else{
                sender.spigot().sendMessage(message)
            }
        }catch (e:Throwable){
            val string = StringBuilder("\n")
            MainCommand.subCommands.keys.forEachIndexed{ index,it->
                string.append("&7$it ")
                if (index%5==4){
                    string.appendLine()
                }
            }
            string.toString().send(sender)
        }

        return true
    }
}