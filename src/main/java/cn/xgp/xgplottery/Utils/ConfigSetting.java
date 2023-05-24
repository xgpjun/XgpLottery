package cn.xgp.xgplottery.Utils;

import org.bukkit.configuration.Configuration;

public class ConfigSetting {
    public static String version;
    public static boolean showProbability;
    public static boolean enableParticle;
    public static boolean giveLottery;
    public static boolean giveKey;
    public static boolean shop;
    public static long autoSaveTime;
    public static boolean autoSaveMsg;
    public static long autoUpdateTopTime;
    public static boolean broadcast;

    public static void loadConfig(Configuration config){
        version = config.getString("version");
        showProbability = config.getBoolean("showProbability",false);
        enableParticle = config.getBoolean("enableParticle",true);
        giveLottery = config.getBoolean("giveLottery",true);
        giveKey = config.getBoolean("giveKey",false);
        shop = config.getBoolean("shop",true);
        autoSaveTime = config.getLong("autoSaveTime",120L)*20;
        autoUpdateTopTime = config.getLong("autoUpdateTopTime",120L)*20;
        autoSaveMsg = config.getBoolean("autoSaveMsg",true);
        broadcast = config.getBoolean("broadcast",true);
        LangUtils.loadLangFile(config.getString("lang","zh_CN.yml"));
    }


}
