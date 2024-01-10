package cn.xgpjun.xgplottery2.command.sub

import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.command.filter
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.MessageL
import cn.xgpjun.xgplottery2.manager.ParticleManager
import cn.xgpjun.xgplottery2.send
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

object Particle :TabExecutor{
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): MutableList<String> {
         return when(args.size){
             2-> arrayListOf("show","clear").filter(args)
             else-> arrayListOf()
         }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.isOp){
            return true
        }
        if (!ParticleManager.enable){
            Message.RequireParticleLib.get().send(sender)
            try {
                val message = TextComponent()
                val text = TextComponent("&7&lhttps://github.com/602723113/ParticleLib/releases".color())
                text.clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL,"https://github.com/602723113/ParticleLib/releases")
                message.addExtra(text)
                sender.spigot().sendMessage(message)
            }catch (_:NoSuchMethodError){
            }
            return true
        }

        when(args.getOrNull(1)){
            "show" ->{
                ParticleManager.show()
                Message.Success.get().send(sender)
            }
            "clear" ->{
                ParticleManager.clear()
                Message.Success.get().send(sender)
            }
            else -> help(sender)
        }
        return true
    }

    fun help(sender: CommandSender){
        MessageL.ParticleHelp.get().send(sender)
    }
}