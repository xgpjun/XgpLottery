package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.XgpLottery;
import lombok.Getter;
import org.bukkit.Location;

public class LotteryBox{
    @Getter
    private final String lotteryName;
    @Getter
    private final Location location;

    public LotteryBox(String lotteryName, Location location) {
        this.lotteryName = lotteryName;
        this.location = location;
    }

    public static Lottery getLotteryByLocation(Location location){
        LotteryBox result =null;
        for(LotteryBox lotteryBox: XgpLottery.lotteryBoxList){
            if(lotteryBox.getLocation().equals(location))
                result = lotteryBox;
        }
        if(result!=null){
            return XgpLottery.lotteryList.get(result.getLotteryName());
        }
        return null;
    }

}

