package cn.xgpjun.xgplottery2.hook

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

object PlaceholderAPIHook {
    val enable:Boolean by lazy {
        Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")
    }
}

fun String.setPlaceholder(player: OfflinePlayer):String{
    return if (PlaceholderAPIHook.enable){
        PlaceholderAPI.setPlaceholders(player,this)
    }else this
}