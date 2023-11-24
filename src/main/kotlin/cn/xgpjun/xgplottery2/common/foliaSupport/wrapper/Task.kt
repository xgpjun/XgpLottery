package cn.xgpjun.xgplottery2.common.foliaSupport.wrapper

import org.bukkit.plugin.Plugin

interface Task {
    fun getOwningPlugin(): Plugin?
    fun cancel()
    fun isCancelled(): Boolean
    fun isTimerTask(): Boolean
    fun isAsyncTask(): Boolean
}