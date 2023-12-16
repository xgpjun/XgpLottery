package cn.xgpjun.xgplottery2.manager

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.listener.AnimListener
import cn.xgpjun.xgplottery2.listener.GuiLimit
import cn.xgpjun.xgplottery2.listener.PlayerListener
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener


object ListenerManager {

    private val listeners = arrayListOf(GuiLimit,AnimListener,PlayerListener)

    fun registerAll(){
        listeners.forEach{
            it.register()
        }
    }

}

fun Listener.register(){
    XgpLottery.instance.let { Bukkit.getPluginManager().registerEvents(this, it) }
}

fun Listener.unRegister(){
    HandlerList.unregisterAll(this)
}

