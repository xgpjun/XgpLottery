package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.BoxParticle;
import cn.xgp.xgplottery.Lottery.LotteryBox;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class BoxParticleUtils {
    public static void addBox(LotteryBox lotteryBox){
        if(nmsUtils.versionToInt<9)
            return;
        XgpLottery.lotteryBoxList.add(lotteryBox);
        if(ConfigSetting.enableParticle&& Bukkit.getPluginManager().getPlugin("ParticleLib") != null){
            new BoxParticle(lotteryBox.getLocation()).createParticle();
        }
    }
    public static void removeBox(LotteryBox lotteryBox){
        if(nmsUtils.versionToInt<9)
            return;
        XgpLottery.lotteryBoxList.remove(lotteryBox);
        if(ConfigSetting.enableParticle&&Bukkit.getPluginManager().getPlugin("ParticleLib") != null){
            BoxParticle target=null;
            for(BoxParticle boxParticle:XgpLottery.boxParticleList){
                if(boxParticle.location.equals(lotteryBox.getLocation())){
                    target = boxParticle;
                }
            }
            if(target!=null)
                target.clearParticle();
        }
    }
}
