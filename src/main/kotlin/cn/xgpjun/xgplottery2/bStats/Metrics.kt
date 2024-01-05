package cn.xgpjun.xgplottery2.bStats

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.bStats.org.bstats.Metrics
import cn.xgpjun.xgplottery2.manager.SchedulerManager

object Metrics{
    fun enable() {
        val pluginId = 18492
        val metrics = Metrics(XgpLottery.instance, pluginId)
        metrics.addCustomChart(
            Metrics.SimplePie(
                "chart_id"
            ) { "My value" })
        metrics.addCustomChart(
            Metrics.SimplePie(
                "isfolia"
            ) { SchedulerManager.serverType.name })
    }
}
