package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.Utils.GiveUtils;
import cn.xgp.xgplottery.Utils.TimesUtils;
import cn.xgp.xgplottery.XgpLottery;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CumulativeRewards {
    private String name;
    private List<Award> awards;
    private String lotteryName;
    private int neededTimes;

    /**
     * 领取次数,默认为1。0为无限重复领取。
     */
    private int limit;

    public CumulativeRewards(String lotteryName,String name){
        this(name, new ArrayList<>(),lotteryName,0,1);
    }


    public static CumulativeRewards getByName(String name){
        for (CumulativeRewards reward: XgpLottery.rewards){
            if(name.equals(reward.getName()))
                return reward;
        }
        return null;
    }

    //玩家领取奖励！
    public void giveRewards(Player player){
        if(canGetTimes(player)>0){
            TimesUtils.addReceiveTimes(player,name);
            GiveUtils.giveAward(player,awards);
        }
    }

    //返回可领取次数
    public int canGetTimes(Player player){
        if(neededTimes<=0){
            return 0;
        }
        //已经领取的次数
        int rTimes = hasGetTimes(player);
        if(limit>0&&rTimes>=limit){
            return 0;
        }
        //总抽奖次数
        int times=TimesUtils.getLotteryTimes(player.getUniqueId(),lotteryName).getTimes();
        if(times - rTimes*neededTimes < neededTimes ){
            return 0;
        }
        return (times - rTimes*neededTimes)/neededTimes;
    }

    public int hasGetTimes(Player player){
        LotteryTimes times = TimesUtils.getReceiveTimes(player.getUniqueId(),name);
        return times==null?0:times.getTimes();
    }

}
