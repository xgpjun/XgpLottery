package cn.xgpjun.xgplottery2

import cn.xgpjun.xgplottery2.additions.AdditionLoader
import cn.xgpjun.xgplottery2.api.event.Events
import cn.xgpjun.xgplottery2.api.event.call
import cn.xgpjun.xgplottery2.bStats.Metrics
import cn.xgpjun.xgplottery2.command.MainCommand
import cn.xgpjun.xgplottery2.hook.PlaceholderAPIHook
import cn.xgpjun.xgplottery2.listener.PlayerListener
import cn.xgpjun.xgplottery2.lottery.calculator.impl.GuaranteedCalculator
import cn.xgpjun.xgplottery2.lottery.calculator.impl.NormalCalculator
import cn.xgpjun.xgplottery2.lottery.pojo.CumulativeRewardManager
import cn.xgpjun.xgplottery2.manager.*
import cn.xgpjun.xgplottery2.manager.DatabaseManager.save
import cn.xgpjun.xgplottery2.utils.Config
import com.google.gson.JsonParser
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class XgpLottery : JavaPlugin() {
    override fun onEnable() {
        instance = this
        Config.loadConfig()
        NMSManager.register()
        saveDefaultConfig()
        MessageManager.load()
        logo.send(Bukkit.getConsoleSender())
        if (!File(dataFolder, "lottery").exists()){
            saveResource("lottery/default.yml",false)
        }
        if (!File(dataFolder, "cumulativeReward").exists()){
            saveResource("cumulativeReward/example.yml",false)
        }
        //计算器注册
        NormalCalculator().register("Normal")
        GuaranteedCalculator().register("Guaranteed")
        PlaceholderAPIHook.register()
        SchedulerManager.register(this)

        DatabaseManager.register()

        //注册动画处理器
        AnimManager.register()

        //读取lottery文件
        LotteryManager.loadFileData()
        CumulativeRewardManager.register()
        //注册监听器
        ListenerManager.registerAll()
        //注册命令
        MainCommand.register()
        //读取抽奖箱
        CrateManager.register()
        //粒子特效
        ParticleManager.register()

        "&7loading additions...".log()
        AdditionLoader.loadAddition()
        Metrics.enable()
        checkUpdate()
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)
        Bukkit.getScheduler().cancelTasks(this)
        DatabaseManager.onlinePlayerData.forEach{
            it.value.save()
        }
        DatabaseManager.onlinePlayerData.clear()
    }
    fun reload(){
        Events.XLReloadEvent().call()
        saveDefaultConfig()
        Config.loadConfig()
        MessageManager.load()
        logo.send(Bukkit.getConsoleSender())
        if (!File(dataFolder, "lottery").exists()){
            saveResource("lottery/default.yml",false)
        }
        LotteryManager.loadFileData()
        CumulativeRewardManager.register()
        CrateManager.register()
        ParticleManager.register()
        PlaceholderAPIHook.register()
        "&7loading additions...".log()
        AdditionLoader.loadAddition()
    }
    companion object {
        lateinit var instance : XgpLottery
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

fun checkUpdate(){
    SchedulerManager.getScheduler().runTaskLaterAsynchronously(100L){
        try {
            val githubAPIURL = "https://api.github.com/repos/xgpjun/XgpLottery/releases/latest"
            val url = URL(githubAPIURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.addRequestProperty("Accept", "application/vnd.github.v3+json")
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream, StandardCharsets.UTF_8))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                val jsonParser = JsonParser()
                val json = jsonParser.parse(response.toString()).asJsonObject
                val version = json["tag_name"].toString().replace("\"", "")
                if (versionToInt(Config.version)< versionToInt(version)){
                    Message.VersionOutDate.get(version).log()
                    PlayerListener.pluginOutDate = version
                }
            }
        }catch (_:Exception){
        }
    }
}

fun versionToInt(version: String): Int {
    val v = version.split(".")
    return v[0].toInt() * 100 + v[1].toInt() * 10 + v[2].toInt()
}