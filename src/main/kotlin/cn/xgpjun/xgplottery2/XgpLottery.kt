package cn.xgpjun.xgplottery2

import cn.xgpjun.xgplottery2.command.MainCommand
import cn.xgpjun.xgplottery2.manager.DatabaseManager
import cn.xgpjun.xgplottery2.manager.DatabaseManager.save
import cn.xgpjun.xgplottery2.lottery.calculator.impl.GuaranteedCalculator
import cn.xgpjun.xgplottery2.lottery.calculator.impl.NormalCalculator
import cn.xgpjun.xgplottery2.manager.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class XgpLottery : JavaPlugin() {
    override fun onEnable() {
        instance = this
        NMSManager.register()
        saveDefaultConfig()
        MessageManager.load()
        logo.send(Bukkit.getConsoleSender())
        if (!File(dataFolder, "lottery").exists()){
            saveResource("lottery/default.yml",false)
        }
        //计算器注册
        NormalCalculator().register("Normal")
        GuaranteedCalculator().register("Guaranteed")

        SchedulerManager.register(this)

        DatabaseManager.register()

        //注册动画处理器
        AnimManager.register()

        //读取lottery文件
        LotteryManager.loadFileData()
        //注册监听器
        ListenerManager.registerAll()
        //注册命令
        MainCommand.register()

    }

    override fun onDisable() {
        DatabaseManager.onlinePlayerData.forEach{
            it.value.save()
        }
        DatabaseManager.onlinePlayerData.clear()
    }

    companion object {
        lateinit var instance : JavaPlugin
        val logo = listOf("&f",
            "&9__   __               _             _    _                      _____ ",
            "&9\\ \\ / /              | |           | |  | |                    / __  \\",
            "&9 \\ V /   __ _  _ __  | |      ___  | |_ | |_  ___  _ __  _   _ `' / /'",
            "&9 /   \\  / _` || '_ \\ | |     / _ \\ | __|| __|/ _ \\| '__|| | | |  / /  ",
            "&9/ /^\\ \\| (_| || |_) || |____| (_) || |_ | |_ | __/| |   | |_| |./ /___",
            "&9\\/   \\/ \\__, || .__/ \\_____/ \\___/  \\__| \\__|\\___||_|    \\__, |\\_____/",
            "&9         __/ || |                                         __/ |       ",
            "&9        |___/ |_|                                        |___/        ",
            "&f"
        )
    }

}

fun stop(message: String){
    message.log()
    Bukkit.getPluginManager().disablePlugin(XgpLottery.instance)
}

fun String.log(){
    val message = "&l[&9Xgp&3Lottery&a2&f] &e=> &f$this"
    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message))
}

fun String.color():String{
    return ChatColor.translateAlternateColorCodes('&',this)
}
fun List<String>.color():MutableList<String>{
    val strings = ArrayList<String>()
    this.forEach { strings.add(it.color()) }
    return strings
}
fun String.send(commandSender: CommandSender){
    commandSender.sendMessage("${Message.Prefix.get()}$this".color())
}

fun List<String>.send(commandSender: CommandSender){
    commandSender.sendMessage(Message.Prefix.get().color())
    this.forEach { commandSender.sendMessage(it.color()) }
}