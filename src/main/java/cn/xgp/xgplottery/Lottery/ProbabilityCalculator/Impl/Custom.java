package cn.xgp.xgplottery.Lottery.ProbabilityCalculator.Impl;

import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryRecord;
import cn.xgp.xgplottery.Lottery.LotteryTimes;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.ProbabilityCalculator;
import cn.xgp.xgplottery.Utils.ChatUtils;
import cn.xgp.xgplottery.Utils.ConfigSetting;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.TimesUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;
public class Custom extends ProbabilityCalculator{

    String msg;
    ItemStack award;
    Player player;

    @Override
    public ItemStack getAward(Lottery lottery,Player player) {
        int maxTime = lottery.getMaxTime();
        int spWeight = lottery.getSpWeightSum();
        int commWeight = lottery.getCommonWeightSum();

        List<ItemStack> items = new ArrayList<>(lottery.getItems()) ;
        List<ItemStack> spItems = new ArrayList<>(lottery.getSpItems()) ;
        List<Integer> weights = new ArrayList<>(lottery.getWeights()) ;
        List<Integer> spWeights = new ArrayList<>(lottery.getSpWeights()) ;

        //拥有保底机制
        if(maxTime>0&&lottery.getSpItems().size()>0){
            LotteryTimes lotteryTimes = TimesUtils.getCurrentLotteryTimes(player.getUniqueId(),lottery.getName());
            int currentTime = lotteryTimes.getTimes();
            //抽奖次数大于等于保底次数
            if(currentTime>=maxTime){
                int randomWeight = new Random().nextInt(spWeight)+1;
                player.sendMessage(ChatColor.GOLD+ LangUtils.LotteryPrefix+ChatColor.GREEN+ LangUtils.LotteryInformation.replace("%time%",Integer.toString(TimesUtils.getCurrentTimes(player.getUniqueId(),lottery.getName()))));
                ItemStack award = getSpItem(lottery, player, spItems, spWeights, lotteryTimes, randomWeight);
                LotteryRecord.addRecord(award,player.getUniqueId(),true,lottery.getName());
                if (award != null) return award;
                //这里是执行不到的~
                return new ItemStack(Material.DIAMOND);
            }
            //抽奖次数未到达保底次数
            else {
                int randomWeight = new Random().nextInt(lottery.getWeightSum())+1;
                //抽到保底物品
                if(randomWeight<=spWeight){
                    player.sendMessage(ChatColor.GOLD+LangUtils.LotteryPrefix+ChatColor.GREEN+ LangUtils.LotteryInformation.replace("%time%",Integer.toString(TimesUtils.getCurrentTimes(player.getUniqueId(),lottery.getName()))));
                    ItemStack award = getSpItem(lottery, player, spItems, spWeights, lotteryTimes, randomWeight);
                    LotteryRecord.addRecord(award,player.getUniqueId(),true,lottery.getName());
                    return award;
                }
                //没抽到保底物品
                else {
                    randomWeight = randomWeight-spWeight;
                    for(int i =0;i<items.size();i++){
                        randomWeight -=weights.get(i);
                        if(randomWeight<=0){
                            player.sendMessage(ChatColor.GOLD+LangUtils.LotteryPrefix+ChatColor.GREEN+ LangUtils.LotteryInformation.replace("%time%",Integer.toString(TimesUtils.getCurrentTimes(player.getUniqueId(),lottery.getName()))));
                            ItemStack item = items.get(i).clone();
                            LotteryRecord.addRecord(item,player.getUniqueId(),false,lottery.getName());
                            return item;
                        }
                    }
                }
            }
        }
        else{
            player.sendMessage(ChatColor.GOLD+LangUtils.LotteryPrefix+ChatColor.GREEN+ LangUtils.LotteryInformation.replace("%time%",Integer.toString(TimesUtils.getCurrentTimes(player.getUniqueId(),lottery.getName()))));
            int randomWeight = new Random().nextInt(commWeight)+1;
            for(int i =0;i<items.size();i++){
                ItemStack item = items.get(i);
                randomWeight -=weights.get(i);
                if(randomWeight<=0){
                    LotteryRecord.addRecord(item,player.getUniqueId(),false,lottery.getName());
                    return item;
                }
            }
        }
        return null;
    }

    @Nullable
    private ItemStack getSpItem(Lottery lottery, Player player, List<ItemStack> spItems, List<Integer> spWeights, LotteryTimes lotteryTimes, int randomWeight) {
        for(int i =0;i<spItems.size();i++){
            randomWeight -=spWeights.get(i);
            if(randomWeight<=0){
                award = spItems.get(i).clone();
                msg = ChatColor.GOLD+LangUtils.LotteryPrefix+ChatColor.AQUA+LangUtils.BroadcastMsg.replace("%player%",player.getName()).replace("%lotteryName%",lottery.getName()).replace("%time%",Integer.toString(TimesUtils.getCurrentTimes(player.getUniqueId(),lottery.getName())));
                this.player = player;
                TimesUtils.clearCurrentTime(lotteryTimes);
                return award.clone();
            }
        }
        return null;
    }

    @Override
    public void sendMessage(){
        if(award==null)
            return;
        if(ConfigSetting.broadcast){
            ChatUtils.sendMessage(msg,award);
        }else {
            player.sendMessage(ChatColor.GOLD+LangUtils.BroadcastNotEnableMsg);
        }
    }

}
