package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.LotteryTimes;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TimesUtils {

    public static int taskId;
    private static List<LotteryTimes> allTimesTop;
    public static void setAllTimesTop(){
        allTimesTop = new CopyOnWriteArrayList<>(XgpLottery.allTimes);
        allTimesTop.sort(Comparator.comparingInt(LotteryTimes::getTimes));
    }
    public static LotteryTimes getAllTimesTop(int rank){
        if(allTimesTop.size()>=rank)
            return allTimesTop.get(rank-1);
        return null;
    }

    public static int getCurrentTimes(UUID uuid , String lotteryName){
        LotteryTimes result = getCurrentLotteryTimes(uuid,lotteryName);
        if(result==null)
            return 0;
        return result.getTimes();
    }


    public static int getTimes(UUID uuid ,String lotteryName){
        LotteryTimes result = getLotteryTimes(uuid,lotteryName);
        if(result==null)
            return 0;
        return result.getTimes();
    }
    public static LotteryTimes getLotteryTimes(UUID uuid , String lotteryName){
        LotteryTimes result =null;
        if(!XgpLottery.totalTime.isEmpty())
            for(LotteryTimes lotteryTimes: XgpLottery.totalTime){
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
    public static LotteryTimes getAllLotteryTimes(UUID uuid){
        LotteryTimes result =null;
        if(!XgpLottery.allTimes.isEmpty())
            for(LotteryTimes lotteryTimes: XgpLottery.allTimes){
                if(lotteryTimes.getUuid().equals(uuid)){
                    result = lotteryTimes;
                }
            }
        return result;
    }

    public static void addTimes(Player player, String lotteryName) {
        LotteryTimes lotteryTimes = getLotteryTimes(player.getUniqueId(), lotteryName);
        if (lotteryTimes == null){
            lotteryTimes = new LotteryTimes(player.getUniqueId(), lotteryName);
            XgpLottery.totalTime.add(lotteryTimes);
        }
        LotteryTimes currentTimes = getCurrentLotteryTimes(player.getUniqueId(), lotteryName);
        if (currentTimes == null){
            currentTimes = new LotteryTimes(player.getUniqueId(), lotteryName);
            XgpLottery.currentTime.add(currentTimes);
        }
        LotteryTimes allTimes = getAllLotteryTimes(player.getUniqueId());
        if(allTimes == null){
            allTimes = new LotteryTimes(player.getUniqueId());
            XgpLottery.allTimes.add(allTimes);
        }
        allTimes.setTimes(allTimes.getTimes()+1);
        currentTimes.setTimes(currentTimes.getTimes() + 1);
        lotteryTimes.setTimes(lotteryTimes.getTimes() + 1);
    }

    public static int getAllTimes(UUID uuid){
        if(XgpLottery.totalTime==null)
            return 0;
        for(LotteryTimes times:XgpLottery.allTimes){
            if(times.getUuid().equals(uuid)){
                return times.getTimes();
            }
        }
        return 0;
    }

    public static void autoLoadTop(){
        taskId = Bukkit.getScheduler().runTaskTimer(XgpLottery.instance, TimesUtils::setAllTimesTop, 200, ConfigSetting.autoUpdateTopTime).getTaskId();
    }
}
