package cn.xgp.xgplottery.common;

import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;

import java.io.File;

public class Memory {
    public static boolean checkFreeSpace(){
        File file = new File(XgpLottery.instance.getDataFolder().toURI());
        if (file.exists()) {
            long freeSpace = file.getFreeSpace()/(1024 * 1024);
            return freeSpace < 300;
        }
        return true;
    }

    public static void broadcast(){
        Bukkit.broadcastMessage(LangUtils.LotteryPrefix+LangUtils.NoEnoughMemory);
    }
}
