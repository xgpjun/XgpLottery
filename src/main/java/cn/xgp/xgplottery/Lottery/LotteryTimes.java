package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.Utils.SerializeUtils;
import cn.xgp.xgplottery.XgpLottery;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LotteryTimes {

    private UUID uuid;
    private int times;
    private String lotteryName;

    public LotteryTimes(UUID uuid,String lotteryName){
        this(uuid,0,lotteryName);
    }


    public void clearTimes(int times,Player player) {
        this.times = times;
        SerializeUtils.savePlayerCurrentLotteryTimes(player,lotteryName);

    }

    public static LotteryTimes getLotteryTimes(UUID uuid , String lotteryName){
        LotteryTimes result =null;
        if(!XgpLottery.lotteryTimesList.isEmpty())
            for(LotteryTimes lotteryTimes: XgpLottery.lotteryTimesList){
                if(lotteryTimes.getUuid().equals(uuid)&&lotteryTimes.getLotteryName().equals(lotteryName)){
                    result = lotteryTimes;
                }
            }
        return result;
    }

    public static LotteryTimes getCurrentLotteryTimes(UUID uuid ,String lotteryName){
        LotteryTimes result =null;
        if(!XgpLottery.currentTime.isEmpty())
            for(LotteryTimes lotteryTimes: XgpLottery.currentTime){
                if(lotteryTimes.getUuid().equals(uuid)&&lotteryTimes.getLotteryName().equals(lotteryName)){
                    result = lotteryTimes;
                }
            }
        return result;
    }

    public static int getCurrentTimes(UUID uuid ,String lotteryName){
        LotteryTimes result = getCurrentLotteryTimes(uuid,lotteryName);
        if(result==null)
            return -1;
        return result.getTimes();
    }


    public static int getTimes(UUID uuid ,String lotteryName){
        LotteryTimes result = getLotteryTimes(uuid,lotteryName);
        if(result==null)
            return -1;
        return result.getTimes();
    }


    public static void addTimes(Player player, String lotteryName) {
        LotteryTimes lotteryTimes = getLotteryTimes(player.getUniqueId(), lotteryName);
        if (lotteryTimes == null){
            lotteryTimes = new LotteryTimes(player.getUniqueId(), lotteryName);
            XgpLottery.lotteryTimesList.add(lotteryTimes);
        }
        LotteryTimes currentTimes = getCurrentLotteryTimes(player.getUniqueId(), lotteryName);
        if (currentTimes == null){
            currentTimes = new LotteryTimes(player.getUniqueId(), lotteryName);
            XgpLottery.currentTime.add(currentTimes);
        }
        currentTimes.setTimes(currentTimes.getTimes() + 1);
        lotteryTimes.setTimes(lotteryTimes.getTimes() + 1);
        SerializeUtils.savePlayerTotalLotteryTimes(player,lotteryName);
        SerializeUtils.savePlayerCurrentLotteryTimes(player,lotteryName);
    }

}
