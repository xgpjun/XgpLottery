package cn.xgpjun.xgplottery2.common.foliaSupport.wrapper

interface Scheduler {
    fun runTask(runnable: Runnable): Task?

    fun runTaskLater(delay: Long,runnable: Runnable): Task?

    fun runTaskTimer( delay: Long, period: Long,runnable: Runnable, ): Task?

    fun runTaskAsynchronously(runnable: Runnable): Task?

    fun runTaskLaterAsynchronously(delay: Long,runnable: Runnable): Task?

    fun runTaskTimerAsynchronously(delay: Long, period: Long,runnable: Runnable, ): Task?
}