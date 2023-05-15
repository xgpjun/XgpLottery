package cn.xgp.xgplottery.Lottery.ProbabilityCalculator.Impl;

import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryTimes;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.ProbabilityCalculator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.*;
public class Custom extends ProbabilityCalculator{

    @Override
    public String getCalculatorType() {
        return "Custom";
    }
    public String toLore(){
        return "自定义概率";
    }

    @Override
    public ItemStack getAward(Lottery lottery,Player player) {
        int maxTime = lottery.getMaxTime();
        int spWeight = lottery.getSpWeightSum();
        int commWeight = lottery.getCommonWeightSum();
        int weightSum = spWeight+commWeight;

        List<ItemStack> items = new ArrayList<>(lottery.getItems()) ;
        List<ItemStack> spItems = new ArrayList<>(lottery.getSpItems()) ;
        List<Integer> weights = new ArrayList<>(lottery.getWeights()) ;
        List<Integer> spWeights = new ArrayList<>(lottery.getSpWeights()) ;

        //拥有保底机制
        if(maxTime>0){
            LotteryTimes lotteryTimes = LotteryTimes.getCurrentLotteryTimes(player.getUniqueId(),lottery.getName());
            int currentTime = lotteryTimes.getTimes();
            //抽奖次数大于等于保底次数
            if(currentTime>=maxTime){
                int randomWeight = new Random().nextInt(spWeight)+1;
                player.sendMessage(ChatColor.GOLD+"[抽奖小助手]"+ChatColor.GREEN+"这是你的第"+ LotteryTimes.getCurrentTimes(player.getUniqueId(),lottery.getName()) +"次抽奖！");
                player.sendMessage(ChatColor.GOLD+"你抽到了保底物品！");
                lotteryTimes.setTimes(0);
                for(int i =0;i<spItems.size();i++){
                    randomWeight -=spWeights.get(i);
                    if(randomWeight<=0){
                        return new ItemStack(spItems.get(i));
                    }
                }
                return new ItemStack(Material.DIAMOND);
            }
            //抽奖次数未到达保底次数
            else {
                int randomWeight = new Random().nextInt(weightSum)+1;
                //抽到保底物品
                if(randomWeight<=spWeight){
                    player.sendMessage(ChatColor.GOLD+"[抽奖小助手]"+ChatColor.GREEN+"这是你的第"+ LotteryTimes.getCurrentTimes(player.getUniqueId(),lottery.getName()) +"次抽奖！");
                    player.sendMessage(ChatColor.GOLD+"你抽到了保底物品！");
                    lotteryTimes.setTimes(0);
                    for(int i =0;i<spItems.size();i++){
                        randomWeight -=spWeights.get(i);
                        if(randomWeight<=0){
                            return new ItemStack(spItems.get(i));
                        }
                    }
                }
                //没抽到保底物品
                else {
                    randomWeight = randomWeight-spWeight;
                    for(int i =0;i<items.size();i++){
                        randomWeight -=weights.get(i);
                        if(randomWeight<=0){
                            player.sendMessage(ChatColor.GOLD+"[抽奖小助手]"+ChatColor.GREEN+"这是你的第"+ LotteryTimes.getCurrentTimes(player.getUniqueId(),lottery.getName()) +"次未中保底物品");
                            return new ItemStack(items.get(i));
                        }
                    }
                }
            }
        }
        else{
            player.sendMessage(ChatColor.GOLD+"[抽奖小助手]"+ChatColor.GREEN+"这是你的第"+ LotteryTimes.getCurrentTimes(player.getUniqueId(),lottery.getName()) +"次抽奖，但是这个奖池并没有保底~");
            int randomWeight = new Random().nextInt(commWeight)+1;
            for(int i =0;i<items.size();i++){
                ItemStack item = items.get(i);
                randomWeight -=weights.get(i);
                if(randomWeight<=0){
                    return new ItemStack(item);
                }
            }
        }
        return null;
    }

}
