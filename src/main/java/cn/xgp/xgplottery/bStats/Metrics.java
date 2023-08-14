package cn.xgp.xgplottery.bStats;

import cn.xgp.xgplottery.XgpLottery;
import cn.xgp.xgplottery.common.FoliaLib.Enum.ServerType;

public class Metrics {
    public static void enable(){
        int pluginId = 18492;
        cn.xgp.xgplottery.bStats.org.bstats.Metrics metrics = new cn.xgp.xgplottery.bStats.org.bstats.Metrics(XgpLottery.instance, pluginId);
        metrics.addCustomChart(new cn.xgp.xgplottery.bStats.org.bstats.Metrics.SimplePie("chart_id", () -> "My value"));
        metrics.addCustomChart(new cn.xgp.xgplottery.bStats.org.bstats.Metrics.SimplePie("isfolia",()-> ServerType.getServerType().name()));
    }
}
