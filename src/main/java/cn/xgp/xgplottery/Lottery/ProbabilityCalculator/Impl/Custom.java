package cn.xgp.xgplottery.Lottery.ProbabilityCalculator.Impl;

import cn.xgp.xgplottery.Lottery.*;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.ProbabilityCalculator;
import cn.xgp.xgplottery.Utils.ChatUtils;
import cn.xgp.xgplottery.Utils.ConfigSetting;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.TimesUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Custom extends ProbabilityCalculator{

    private final List<String> msg = new ArrayList<>();
    private final List<Award> award = new ArrayList<>();
    private Player player;

    @Override
    public Award getAward(Lottery lottery,Player player) {
        //添加次数
        TimesUtils.addTimes(player, lottery.getName());
        int maxTime = lottery.getMaxTime();
        int spWeight = lottery.getSpWeightSum();
        int commWeight = lottery.getCommonWeightSum();
        //拥有保底机制
        if(maxTime>0&&spWeight>0){
            LotteryTimes lotteryTimes = TimesUtils.getCurrentLotteryTimes(player.getUniqueId(),lottery.getName());
            int currentTime = lotteryTimes.getTimes();
            //抽奖次数大于等于保底次数
            if(currentTime>=maxTime){
                int randomWeight = new Random().nextInt(spWeight)+1;
                return getSpItem(lottery, player, lottery.getSpAwards(), lotteryTimes, randomWeight);
                //这里是执行不到的~
            }
            //抽奖次数未到达保底次数
            else {
                int randomWeight = new Random().nextInt(lottery.getWeightSum())+1;
                //抽到保底物品
                if(randomWeight<=spWeight){
                    return getSpItem(lottery, player, lottery.getSpAwards(), lotteryTimes, randomWeight);
                }
                //没抽到保底物品
                else {
                    randomWeight = randomWeight-spWeight;
                    return getItem(lottery, player, randomWeight);
                }
            }
        }
        else{
            int randomWeight = new Random().nextInt(commWeight)+1;
            return getItem(lottery, player, randomWeight);
        }
    }

    @NotNull
    private Award getItem(Lottery lottery, Player player, int randomWeight) {
        for(Award value : lottery.getAwards()){
            randomWeight -=value.getWeight();
            if(randomWeight<=0){
                LotteryRecord.addRecord(value.getRecordDisplayItem(), player.getUniqueId(), false, lottery.getName());
                msg.add(ChatColor.GOLD + LangUtils.LotteryPrefix + ChatColor.AQUA + LangUtils.BroadcastMsg.replace("%player%", player.getName()).replace("%lotteryName%", lottery.getName()).replace("%time%", Integer.toString(TimesUtils.getCurrentTimes(player.getUniqueId(), lottery.getName()) + 1)));
                award.add(value);
                return value;
            }
        }
        return new Award(MyItem.getMissingItem());
    }


    private @NotNull Award getSpItem(Lottery lottery, Player player, List<Award> awards, LotteryTimes lotteryTimes, int randomWeight) {
        for (Award value : awards) {
            randomWeight -= value.getWeight();
            if (randomWeight <= 0) {
                award.add(value);
                msg.add(ChatColor.GOLD + LangUtils.LotteryPrefix + ChatColor.AQUA + LangUtils.BroadcastMsg.replace("%player%", player.getName()).replace("%lotteryName%", lottery.getName()).replace("%time%", Integer.toString(TimesUtils.getCurrentTimes(player.getUniqueId(), lottery.getName()) + 1)));
                this.player = player;
                TimesUtils.clearCurrentTime(lotteryTimes);
                LotteryRecord.addRecord(value.getRecordDisplayItem(),player.getUniqueId(),true,lottery.getName());
                isSpecial = true;
                return value;
            }
        }
        return new Award(MyItem.getMissingItem());
    }

    @Override
    public void sendMessage(){
        if(ConfigSetting.broadcast){
            for(int i=0;i<msg.size();i++){
                if(award.get(i).isBroadCast())
                    ChatUtils.sendMessage(msg.get(i),award.get(i).getRecordDisplayItem());
            }
        }else {
            player.sendMessage(ChatColor.GOLD+LangUtils.BroadcastNotEnableMsg);
        }
    }

}
