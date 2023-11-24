package cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.task

import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask


class BukkitTask(private val task: BukkitTask, private val isTimerTask: Boolean) : Task {
    override fun getOwningPlugin(): Plugin {
        return task.owner
    }

    override fun cancel() {
        task.cancel()
    }

    override fun isCancelled(): Boolean {
        return task.isCancelled
    }


    override fun isTimerTask(): Boolean {
        return isTimerTask
    }

    override fun isAsyncTask(): Boolean {
        return task.isSync
    }
}
