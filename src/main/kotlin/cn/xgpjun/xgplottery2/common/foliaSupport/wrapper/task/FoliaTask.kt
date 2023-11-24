package cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.task

import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.plugin.Plugin

class FoliaTask(private val task:ScheduledTask?):Task {
    override fun getOwningPlugin(): Plugin? {
        return task?.owningPlugin
    }

    override fun cancel() {
        task?.cancel()
    }

    override fun isCancelled(): Boolean {
        return task?.isCancelled?:false
    }


    override fun isTimerTask(): Boolean {
        return task?.isRepeatingTask?:false
    }

    override fun isAsyncTask(): Boolean {
        return true
    }
}