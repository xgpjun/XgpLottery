package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Command.SubCmd.GiveCommand;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

public class ConfigSetting {
    public static String version;
    public static boolean showProbability;
    public static boolean enableParticle;
    public static boolean giveLottery;
    public static boolean giveKey;
    public static boolean shop;

    public static void loadConfig(Configuration config){
        version = config.getString("version");
        showProbability = config.getBoolean("ShowProbability");
        enableParticle = config.getBoolean("EnableParticle");
        giveLottery = config.getBoolean("GiveLottery");
        giveKey = config.getBoolean("giveKey");
        shop = config.getBoolean("Shop");
        LangUtils.loadLangFile(config.getString("lang"));
    }


}
