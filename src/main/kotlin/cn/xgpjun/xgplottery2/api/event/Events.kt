package cn.xgpjun.xgplottery2.api.event

import cn.xgpjun.xgplottery2.lottery.pojo.Award
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class Events {
    class XLReloadEvent: Event() {
        companion object {
            val handlerList = HandlerList()
        }
        override fun getHandlers() = handlerList
    }

    class DrawEvent(
        val player: Player,
        val award: Award
    ):Event(){
        companion object {
            val handlerList = HandlerList()
        }

        override fun getHandlers() = handlerList
    }


}

fun Event.call(){
    org.bukkit.Bukkit.getPluginManager().callEvent(this)
}