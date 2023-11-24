package cn.xgpjun.xgplottery2.command.sub

import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.command.filter
import cn.xgpjun.xgplottery2.manager.CrateManager
import cn.xgpjun.xgplottery2.manager.LotteryManager
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.MessageL
import cn.xgpjun.xgplottery2.send
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList

object Crate :TabExecutor {
    /**
     *  /xl crate create lotteryName
     *  /xl crate remove
     *  /xl crate list XXX
     */
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): MutableList<String> {
        return when(args.size){
            2->{
                mutableListOf("create", "remove", "list").filter(args)
            }
            3->{
                when(args[1]){
                    "create" -> {
                         ArrayList(LotteryManager.lotteryMap.keys).filter(args)
                    }
                    "list" ->{
                        ArrayList(CrateManager.cratesList.keys)
                            .map { it.toString() }
                            .toMutableList()
                            .filter(args)
                    }
                    else -> arrayListOf()
                }
            }
            else -> arrayListOf()
        }
    }


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player){
            Message.OnlyPlayer.get().send(sender)
            return true
        }
        when(args.getOrNull(2)){
            "create" ->{
                args.getOrNull(3)?.let { lotteryName ->
                    LotteryManager.getLottery(lotteryName)?.let { lottery->
                        CrateManager.createCrate(sender,lottery)
                    }?: Message.NotFoundLottery.get().send(sender)
                }
            }
            "remove" ->{
                CrateManager.removeCrate(sender)
            }
            "list" ->{
                args.getOrNull(3)?.let {
                    val uuid = UUID.fromString(it)
                    CrateManager.cratesList[uuid]?.let { crate ->
                        crate.getLocation()?.let {
                            it.add(0.5,2.0,0.5)
                            sender.teleport(it)
                        }
                    }
                }?:sendCrateInfo(sender)

            }
            else ->{
                help(sender)
            }
        }
        return true
    }

    fun help(sender: CommandSender){
        MessageL.CrateHelp.get().send(sender)
    }

    fun sendCrateInfo(player: Player){
        Message.Prefix.get().send(player)
        try {
            CrateManager.cratesList.forEach{
                val message = TextComponent("${ChatColor.BLUE}${ChatColor.BOLD}${it.key}")

                val temp = ArrayList<BaseComponent>()
                MessageL.CrateListInfo.get(it.value.world,it.value.x.toString(),it.value.y.toString(),it.value.z.toString(),it.value.lotteryName)
                    .forEach{ s->
                        temp.add(TextComponent(s.color()))
                    }

                message.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, temp.toTypedArray())
                message.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/XgpLottery crate list ${it.key}")
                player.spigot().sendMessage(message)
            }
        }catch (e:NoSuchMethodError){
            Message.NotSupport.get().send(player)
        }
    }
}
