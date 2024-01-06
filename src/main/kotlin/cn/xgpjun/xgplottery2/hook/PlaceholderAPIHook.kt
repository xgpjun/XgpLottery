package cn.xgpjun.xgplottery2.hook

import cn.xgpjun.xgplottery2.manager.DatabaseManager
import cn.xgpjun.xgplottery2.manager.LotteryManager
import cn.xgpjun.xgplottery2.utils.Config
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.*
import kotlin.collections.HashMap

object PlaceholderAPIHook {
    val enable:Boolean by lazy {
        Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null
    }
    fun register(){
        if (enable){
            XLPlaceholder.register()
            XLPlaceholder.subRequest["total"] = Total
            XLPlaceholder.subRequest["individual"] = Individual
            XLPlaceholder.subRequest["ngt"] = NGT
            XLPlaceholder.subRequest["lottery"] = LotteryInfo

        }
    }
}

object XLPlaceholder:PlaceholderExpansion(){

    val subRequest = HashMap<String,SubRequest>()
    override fun getIdentifier() = "XL2"

    override fun getAuthor() = "xgpjun"

    override fun getVersion() = Config.version

    override fun canRegister() = true

    override fun onRequest(p: OfflinePlayer?, params: String): String? {
        val args = params.split("_")
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        return subRequest[args[0].lowercase(locale = Locale.getDefault())]?.onRequest(p,args)
    }
}

fun String.setPlaceholder(player: OfflinePlayer):String{
    return if (PlaceholderAPIHook.enable){
        PlaceholderAPI.setPlaceholders(player,this)
    }else this
}

interface SubRequest{
    fun onRequest(p: OfflinePlayer?, args:Array<String>?): String?
}

object Total:SubRequest{
    override fun onRequest(p: OfflinePlayer?, args: Array<String>?): String? {
        return p?.uniqueId?.let { DatabaseManager.getPlayerData(it).totalDrawCount.toString() }
    }
}

object Individual:SubRequest{
    override fun onRequest(p: OfflinePlayer?, args: Array<String>?): String? {
        return args?.getOrNull(1)?.let {
            p?.let { it1 ->
                DatabaseManager.getPlayerData(it1.uniqueId).individualPoolDrawCount.getOrDefault(it,0).toString()
            }
        }
    }
}

object NGT:SubRequest{
    override fun onRequest(p: OfflinePlayer?, args: Array<String>?): String? {
        return args?.getOrNull(1)?.let {
            p?.let { it1 ->
                DatabaseManager.getPlayerData(it1.uniqueId).customData.getOrDefault("nonGuaranteed$it",0).toString()
            }
        }
    }
}

object LotteryInfo:SubRequest{
    override fun onRequest(p: OfflinePlayer?, args: Array<String>?): String? {
        return args?.getOrNull(1)?.let {
            LotteryManager.getLottery(it)?.customTags?.getOrDefault("guaranteedCount",0)?.toString()
        }
    }
}


