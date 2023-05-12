package cn.xgp.xgplottery.Utils;

import org.bukkit.configuration.Configuration;

public class ConfigSetting {
    public static String version;
    public static boolean showProbability;
    public static boolean enableParticle;

    public static void loadConfig(Configuration config){
        version = config.getString("version");
        showProbability = config.getBoolean("ShowProbability");
        enableParticle = config.getBoolean("EnableParticle");
        LangUtils.loadLangFile(config.getString("lang"));
    }
}
