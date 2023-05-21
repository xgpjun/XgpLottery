package cn.xgp.xgplottery.bStats;

import cn.xgp.xgplottery.XgpLottery;

public class Metrics {
    public static void enable(){
        int pluginId = 18492;
        org.bstats.bukkit.Metrics metrics = new org.bstats.bukkit.Metrics(XgpLottery.instance, pluginId);
        metrics.addCustomChart(new org.bstats.bukkit.Metrics.SimplePie("chart_id", () -> "My value"));
    }
}
