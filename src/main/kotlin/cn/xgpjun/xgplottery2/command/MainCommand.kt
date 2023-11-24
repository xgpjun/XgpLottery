package cn.xgpjun.xgplottery2.command

import cn.xgpjun.xgplottery2.command.sub.*
import cn.xgpjun.xgplottery2.manager.MessageL
import cn.xgpjun.xgplottery2.send
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import java.util.*

object MainCommand :TabExecutor {
    private val subCommands: MutableMap<String, TabExecutor> = HashMap()
    init {
        registerSubCommand("crate",Crate)
        registerSubCommand("help",Help)
        registerSubCommand("draw",Draw)
        registerSubCommand("manage",Manage)
        registerSubCommand("edit",Edit)
    }
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>?
    ): MutableList<String>? {
        val list: MutableList<String> = ArrayList(subCommands.keys)
        if (!sender.hasPermission("xgplottery.manager")) {
            return if (args?.size == 1) {
                mutableListOf("shop")
            } else {
                ArrayList()
            }
        }

        if (sender.hasPermission("xgplottery.manager")) {
            if (args?.size == 1) {
                return list.filter(args)
            } else {
                val subCommand = args?.get(0)?.lowercase(Locale.getDefault())
                val executor = subCommands[subCommand]
                if (executor != null) {
                    return executor.onTabComplete(sender, command, alias, args)
                }
            }
        }
        return null
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args?.isNotEmpty() == true) {
            val subCommand = args[0].lowercase(Locale.getDefault())
            subCommands[subCommand]?.onCommand(sender, command, label, args)
            return true
        } else return subCommands["help"]!!.onCommand(sender, command, label, args)

        MessageL.HelpMessage.get().send(sender)
        return true
    }

    private fun registerSubCommand(subCommand: String, executor: TabExecutor) {
        subCommands[subCommand.lowercase(Locale.getDefault())] = executor
    }

    fun register(){
        Bukkit.getPluginCommand("XgpLottery")?.setExecutor(this)
    }
}

fun MutableList<String>.filter(args: Array<String>): MutableList<String> {
    var latest: String? = null
    if (args.isNotEmpty()) {
        latest = args[args.size - 1]
    }
    if (this.isEmpty() || latest == null) return this
    val ll = latest.lowercase(Locale.getDefault())
    this.removeIf { k: String ->
        !k.lowercase(Locale.getDefault()).startsWith(ll)
    }
    return this
}