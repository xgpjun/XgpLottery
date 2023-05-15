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
    public static long autoSaveTime;
    public static boolean autoSaveMsg;

    public static void loadConfig(Configuration config){
        version = config.getString("version");
        showProbability = config.getBoolean("ShowProbability",false);
        enableParticle = config.getBoolean("EnableParticle",true);
        giveLottery = config.getBoolean("GiveLottery",true);
        giveKey = config.getBoolean("giveKey",false);
        shop = config.getBoolean("Shop",true);
        autoSaveTime = config.getLong("autoSaveTime",120L)*20;
        autoSaveMsg = config.getBoolean("autoSaveMsg",true);
        LangUtils.loadLangFile(config.getString("lang"));
    }


}
