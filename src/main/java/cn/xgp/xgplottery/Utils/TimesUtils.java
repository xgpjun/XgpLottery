package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.LotteryTimes;
import cn.xgp.xgplottery.XgpLottery;
import cn.xgp.xgplottery.common.FoliaLib.Wrapper.Task;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class TimesUtils {

    public static Task task;
    private static List<LotteryTimes> allTimesTop;

    public static void setAllTimesTop(){
        if(!SqlUtils.enable){
            allTimesTop = new CopyOnWriteArrayList<>(XgpLottery.allTimes);
        }else{
            allTimesTop = SqlUtils.getAllTimes("all");
        }
        allTimesTop.sort(Comparator.comparingInt(LotteryTimes::getTimes).reversed());
    }

    public static LotteryTimes getAllTimesTop(int rank){
        if(allTimesTop==null)
            return null;
        allTimesTop.sort(Comparator.comparingInt(LotteryTimes::getTimes).reversed());
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
        return result.getTimes();
    }

    public static LotteryTimes getLotteryTimes(UUID uuid , String lotteryName){
        LotteryTimes result =null;
        if(!SqlUtils.enable){
            if(!XgpLottery.totalTime.isEmpty())
                for(LotteryTimes lotteryTimes: XgpLottery.totalTime){
                    if(lotteryTimes==null||lotteryTimes.getUuid()==null||lotteryTimes.getLotteryName()==null){
                        continue;
                    }
                    if(lotteryTimes.getUuid().equals(uuid)&&lotteryTimes.getLotteryName().equals(lotteryName)){
                        result = lotteryTimes;
                    }
                }
        }else {
            int times = SqlUtils.getOneTimes("total",uuid.toString(),lotteryName);
            result = new LotteryTimes(lotteryName,uuid,times);
        }
        if(result==null)
            result = new LotteryTimes(lotteryName,uuid,0);

        return result;
    }

    public static LotteryTimes getCurrentLotteryTimes(UUID uuid ,String lotteryName){
        LotteryTimes result =null;
        if(!SqlUtils.enable){
            if(!XgpLottery.currentTime.isEmpty())
                for(LotteryTimes lotteryTimes: XgpLottery.currentTime){
                    if(lotteryTimes==null||lotteryTimes.getUuid()==null||lotteryTimes.getLotteryName()==null){
                        continue;
                    }
                    if(lotteryTimes.getUuid().equals(uuid)&&lotteryTimes.getLotteryName().equals(lotteryName)){
                        result = lotteryTimes;
                    }
                }
        }else {
            int times = SqlUtils.getOneTimes("current",uuid.toString(),lotteryName);
            result = new LotteryTimes(lotteryName,uuid,times);
        }

        return result;
    }

    public static LotteryTimes getAllLotteryTimes(UUID uuid){
        LotteryTimes result =null;
        if(!SqlUtils.enable){
            if(!XgpLottery.allTimes.isEmpty())
                for(LotteryTimes lotteryTimes: XgpLottery.allTimes){
                    if(lotteryTimes==null||lotteryTimes.getUuid()==null){
                        continue;
                    }
                    if(lotteryTimes.getUuid().equals(uuid)){
                        result = lotteryTimes;
                    }
                }
        }else {
            int times = SqlUtils.getOneTimes("all",uuid.toString(),"all");
            result = new LotteryTimes(null,uuid,times);
        }

        return result;
    }

    public static void addTimes(Player player, String lotteryName) {
        if(!SqlUtils.enable){
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
                allTimes = new LotteryTimes(player.getUniqueId(),"all");
                XgpLottery.allTimes.add(allTimes);
            }
            allTimes.setTimes(allTimes.getTimes()+1);
            currentTimes.setTimes(currentTimes.getTimes() + 1);
            lotteryTimes.setTimes(lotteryTimes.getTimes() + 1);
            SerializeUtils.saveData();
        }
        else {
            SqlUtils.addTimes(String.valueOf(player.getUniqueId()),"all","all");
            SqlUtils.addTimes(String.valueOf(player.getUniqueId()),lotteryName,"current");
            SqlUtils.addTimes(String.valueOf(player.getUniqueId()),lotteryName,"total");
        }
    }

    public static int getAllTimes(UUID uuid){
        if(!SqlUtils.enable) {
            if (XgpLottery.totalTime == null)
                return 0;
            for (LotteryTimes times : XgpLottery.allTimes) {
                if (times.getUuid().equals(uuid)) {
                    return times.getTimes();
                }
            }
        }else {
            return SqlUtils.getOneTimes("all",uuid.toString(),"all");
        }
        return 0;
    }

    public static void autoLoadTop(){
        task = XgpLottery.foliaLibAPI.getScheduler().runTaskTimer(TimesUtils::setAllTimesTop,200,ConfigSetting.autoUpdateTopTime);

    }

    public static void deleteTimes(String lotteryName){
        if(!SqlUtils.enable) {
            //删除lotteryTime
            List<LotteryTimes> times = new ArrayList<>();
            for (LotteryTimes lotteryTimes : XgpLottery.totalTime) {
                if (lotteryTimes.getLotteryName().equals(lotteryName)) {
                    times.add(lotteryTimes);
                }
            }
            for (LotteryTimes lotteryTimes : times) {
                XgpLottery.totalTime.remove(lotteryTimes);
            }

            List<LotteryTimes> cTimes = new ArrayList<>();
            for (LotteryTimes lotteryTimes : XgpLottery.currentTime) {
                if (lotteryTimes.getLotteryName().equals(lotteryName)) {
                    cTimes.add(lotteryTimes);
                }
            }
            for (LotteryTimes lotteryTimes : cTimes) {
                XgpLottery.currentTime.remove(lotteryTimes);
            }
            SerializeUtils.saveData();
        }else {
            SqlUtils.deleteTimes(lotteryName,"current");
            SqlUtils.deleteTimes(lotteryName,"total");
        }
    }

    public static List<LotteryTimes> getAllCurrentTime(String lotteryName){
        List<LotteryTimes> result;
        if(!SqlUtils.enable){
            result = new ArrayList<>();
            for(LotteryTimes lotteryTimes :XgpLottery.currentTime){
                if(lotteryTimes.getLotteryName().equals(lotteryName)){
                    result.add(lotteryTimes);
                }
            }
        }else {
            result  = SqlUtils.getAllTimes("current");
        }

        return result;

    }

    public static List<LotteryTimes> getAllTotalTime(String lotteryName){

        List<LotteryTimes> result;
        if(!SqlUtils.enable){
            result = new ArrayList<>();
            for(LotteryTimes lotteryTimes :XgpLottery.totalTime){
                if(lotteryTimes.getLotteryName().equals(lotteryName)){
                    result.add(lotteryTimes);
                }
            }
        }else {
            result = SqlUtils.getAllTimes("total");
        }
        return result;
    }

    public static void clearCurrentTime(LotteryTimes lotteryTimes){
        if(!SqlUtils.enable){
            lotteryTimes.setTimes(0);
            SerializeUtils.saveData();
        }else {
            SqlUtils.clearCurrentTimes(lotteryTimes);
        }
    }

    public static void addReceiveTimes(Player player,String name){
        if(!SqlUtils.enable){
            LotteryTimes lotteryTimes = getReceiveTimes(player.getUniqueId(), name);
            if(lotteryTimes==null){
                lotteryTimes = new LotteryTimes(name,player.getUniqueId(),0);
                XgpLottery.rewardsTimes.add(lotteryTimes);
            }
            lotteryTimes.setTimes(lotteryTimes.getTimes() + 1);
            SerializeUtils.saveData();
        }
        else {
            SqlUtils.addTimes(String.valueOf(player.getUniqueId()),name,"reward");
        }
    }
    public static LotteryTimes getReceiveTimes(UUID uuid,String name){
        LotteryTimes result =null;
        if(!SqlUtils.enable){
            if(!XgpLottery.rewardsTimes.isEmpty())
                for(LotteryTimes lotteryTimes: XgpLottery.rewardsTimes){
                    if(lotteryTimes.getUuid().equals(uuid)&&lotteryTimes.getLotteryName().equals(name)){
                        result = lotteryTimes;
                    }
                }
        }else {
            int times = SqlUtils.getOneTimes("reward",uuid.toString(),name);
            result = new LotteryTimes(name,uuid,times);
        }
        return result;
    }
}
