package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Color;
import org.bukkit.Location;
import top.zoyn.particlelib.pobject.Cube;

public class BoxParticle {
    public Location location;
    Location loc1;
    Location loc2;
    Cube cube;
    public BoxParticle(Location location){
        this.location = location;
        loc1 = location.clone().add(0.1,0.1,0.1);
        loc2 = location.clone().add(0.9, 0.9, 0.9);
        cube = new Cube(loc1, loc2);
    }

    public void createParticle(){
        cube.setPeriod(1L)
                .setColor(Color.WHITE)
                .alwaysShowAsync();
        XgpLottery.boxParticleList.add(this);
    }

    public void clearParticle(){
        cube.turnOffTask();
    }

    public static void playAllParticle(){
        XgpLottery.boxParticleList.clear();
        if(XgpLottery.lotteryBoxList!=null&&!XgpLottery.lotteryBoxList.isEmpty()){
            for (LotteryBox lotteryBox: XgpLottery.lotteryBoxList) {
                new BoxParticle(lotteryBox.getLocation()).createParticle();

            }
        }
    }
    public static void clearAllParticle(){
        if(XgpLottery.lotteryBoxList!=null&&!XgpLottery.boxParticleList.isEmpty()){
            for(BoxParticle boxParticle:XgpLottery.boxParticleList){
                boxParticle.clearParticle();
            }
            XgpLottery.boxParticleList.clear();
        }
    }

}
