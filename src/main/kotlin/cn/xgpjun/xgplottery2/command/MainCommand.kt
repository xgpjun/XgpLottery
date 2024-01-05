package cn.xgpjun.xgplottery2.command

import cn.xgpjun.xgplottery2.command.sub.*
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import java.util.*

object MainCommand :TabExecutor {
    val subCommands: MutableMap<String, TabExecutor> = HashMap()
    init {
        registerSubCommand("convert",Convert)
        registerSubCommand("count",Count)
        registerSubCommand("crate",Crate)
        registerSubCommand("cumulativeReward",CumulativeReward)
        registerSubCommand("draw",Draw)
        registerSubCommand("edit",Edit)
        registerSubCommand("give",Give)
        registerSubCommand("help",Help)
        registerSubCommand("key",Key)
        registerSubCommand("particle",Particle)
        registerSubCommand("preview",Preview)
        registerSubCommand("manage",Manage)
        registerSubCommand("reload",Reload)
    }
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): MutableList<String>? {
        val list: MutableList<String> = ArrayList(subCommands.keys)
        if (!sender.isOp){
            return arrayListOf()
        }
        if (args.size == 1) {
            return list.filter(args)
        } else {
            val subCommand = args[0].lowercase(Locale.getDefault())
            val executor = subCommands[subCommand]
            if (executor != null) {
                return executor.onTabComplete(sender, command, alias, args)
            }
        }
        return null
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        return if (args.isNotEmpty()) {
            val subCommand = args[0].lowercase(Locale.getDefault())
            subCommands[subCommand]?.onCommand(sender, command, label, args)
            true
        } else subCommands["help"]!!.onCommand(sender, command, label, args)

    }

    fun registerSubCommand(subCommand: String, executor: TabExecutor) {
        subCommands[subCommand.lowercase(Locale.getDefault())] = executor
    }

    fun register(){
        Bukkit.getPluginCommand("xgplottery2")?.setExecutor(this)
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
