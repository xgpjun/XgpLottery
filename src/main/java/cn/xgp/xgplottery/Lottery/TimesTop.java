package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TimesTop {
    public List<String> topString = new ArrayList<>();
    private final List<LotteryTimes> times = new CopyOnWriteArrayList<>();
    public TimesTop(boolean isTotal,String lotteryName){
        List<LotteryTimes> allTimes = isTotal? XgpLottery.totalTime:XgpLottery.currentTime;
        if(XgpLottery.lotteryList.containsKey(lotteryName)){
            for(LotteryTimes lotteryTimes :allTimes){
                if(lotteryTimes.getLotteryName().equals(lotteryName)){
                    this.times.add(lotteryTimes);
                    createTopString();
                }
            }
        }
    }

    public LotteryTimes getTimesByRank(int rank){
        if(rank<=times.size())
            return times.get(rank-1);
        else
            return null;
    }


    private void createTopString(){
        if(times.isEmpty())
            return;
        times.sort(Comparator.comparingInt(LotteryTimes::getTimes).reversed());
        for(LotteryTimes lotteryTimes:times){
            topString.add(ChatColor.GOLD+"玩家 "+Bukkit.getOfflinePlayer(lotteryTimes.getUuid()).getName()+" : "+ChatColor.AQUA+lotteryTimes.getTimes());
        }
    }


}
