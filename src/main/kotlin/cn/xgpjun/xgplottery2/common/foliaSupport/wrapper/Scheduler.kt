package cn.xgpjun.xgplottery2.common.foliaSupport.wrapper

interface Scheduler {
    fun runTask(runnable: Runnable): Task?

    fun runTaskLater(runnable: Runnable, delay: Long): Task?

    fun runTaskTimer(runnable: Runnable, delay: Long, period: Long): Task?

    fun runTaskAsynchronously(runnable: Runnable): Task?

    fun runTaskLaterAsynchronously(runnable: Runnable, delay: Long): Task?

    fun runTaskTimerAsynchronously(runnable: Runnable, delay: Long, period: Long): Task?
}