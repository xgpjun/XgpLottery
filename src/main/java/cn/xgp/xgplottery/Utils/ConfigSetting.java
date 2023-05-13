package cn.xgp.xgplottery.Utils;

import org.bukkit.configuration.Configuration;

public class ConfigSetting {
    public static String version;
    public static boolean showProbability;
    public static boolean enableParticle;
    public static boolean dontGiveLottery;
    public static boolean giveKey;

    public static void loadConfig(Configuration config){
        version = config.getString("version");
        showProbability = config.getBoolean("ShowProbability");
        enableParticle = config.getBoolean("EnableParticle");
        dontGiveLottery = config.getBoolean("dontGiveLottery");
        giveKey = config.getBoolean("giveKey");
        LangUtils.loadLangFile(config.getString("lang"));
    }
}
