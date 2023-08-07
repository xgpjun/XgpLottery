package cn.xgp.xgplottery.bStats;

import cn.xgp.xgplottery.XgpLottery;

public class Metrics {
    public static void enable(){
        int pluginId = 18492;
        cn.xgp.xgplottery.bStats.org.bstats.bukkit.Metrics metrics = new cn.xgp.xgplottery.bStats.org.bstats.bukkit.Metrics(XgpLottery.instance, pluginId);
        metrics.addCustomChart(new cn.xgp.xgplottery.bStats.org.bstats.bukkit.Metrics.SimplePie("chart_id", () -> "My value"));
        metrics.addCustomChart(new cn.xgp.xgplottery.bStats.org.bstats.bukkit.Metrics.SimplePie("isfolia",()->"true"));
    }
}
