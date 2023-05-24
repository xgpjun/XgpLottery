package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LangUtils {
    public static LangUtils instance;
    public static String EnableMessage;
    public static String DisableMessage;
    public LangUtils(){
        instance = this;
    }

    public static void loadLangFile(String fileName)
    {
        File LangConfigFile = new File(XgpLottery.instance.getDataFolder(),"lang\\"+fileName);
        FileConfiguration LangConfig = YamlConfiguration.loadConfiguration(LangConfigFile);
        EnableMessage = LangConfig.getString("EnableMessage")+"小钢炮君制作~";
        DisableMessage = LangConfig.getString("DisableMessage");
    }





}
