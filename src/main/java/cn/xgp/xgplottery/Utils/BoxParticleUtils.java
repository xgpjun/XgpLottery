package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.BoxParticle;
import cn.xgp.xgplottery.Lottery.LotteryBox;
import cn.xgp.xgplottery.XgpLottery;

public class BoxParticleUtils {
    public static void addBox(LotteryBox lotteryBox){
        XgpLottery.lotteryBoxList.add(lotteryBox);
        if(ConfigSetting.enableParticle){
            new BoxParticle(lotteryBox.getLocation()).createParticle();
        }
    }
    public static void removeBox(LotteryBox lotteryBox){
        XgpLottery.lotteryBoxList.remove(lotteryBox);
        if(ConfigSetting.enableParticle){
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
