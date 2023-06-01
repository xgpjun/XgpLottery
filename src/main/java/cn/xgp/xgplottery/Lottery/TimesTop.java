package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.TimesUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TimesTop {
    public List<String> topString = new ArrayList<>();
    private final List<LotteryTimes> times;
    public TimesTop(boolean isTotal,String lotteryName){
        this.times = isTotal? TimesUtils.getAllTotalTime(lotteryName):TimesUtils.getAllCurrentTime(lotteryName);
        createTopString();
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
            topString.add(ChatColor.GOLD+ LangUtils.Player+" "+Bukkit.getOfflinePlayer(lotteryTimes.getUuid()).getName()+" : "+ChatColor.AQUA+lotteryTimes.getTimes());
        }
    }


}
