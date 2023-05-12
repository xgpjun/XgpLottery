package cn.xgp.xgplottery.Lottery.ProbabilityCalculator.Impl;

import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryTimes;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.ProbabilityCalculator;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Custom extends ProbabilityCalculator{


    @Override
    public String getCalculatorType() {
        return "Custom";
    }
    public String toLore(){
        return "自定义概率";
    }

    @Override
    public void giveItem(Lottery lottery , Player player){
        Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance, () -> {
            int randomWeight = new Random().nextInt(lottery.getWeightSum())+1;
            List<ItemStack> items = new ArrayList<>(lottery.getItems()) ;
            List<Integer> weights = new ArrayList<>(lottery.getWeights()) ;

            for(int i =0;i<items.size();i++){
                ItemStack item = items.get(i);
                randomWeight -=weights.get(i);
                if(randomWeight<=0){
                    ItemStack givenItem = new ItemStack(item);
                    player.getInventory().addItem(givenItem);
                    return;
                }
            }
        });
    }

    @Override
    public ItemStack getAward(Lottery lottery,Player player) {
        int maxTime = lottery.getMaxTime();
        if(maxTime>0){
            LotteryTimes lotteryTimes = LotteryTimes.getCurrentLotteryTimes(player.getUniqueId(),lottery.getName());
            int currentTime = lotteryTimes.getTimes();
            if(currentTime>=maxTime){
                player.sendMessage(ChatColor.GOLD+"[抽奖小助手]"+ChatColor.GREEN+"这是你的第"+ LotteryTimes.getCurrentTimes(player.getUniqueId(),lottery.getName()) +"次抽奖！");
                player.sendMessage(ChatColor.GOLD+"你抽到了保底物品！");
                lotteryTimes.clearTimes(0,player);
                return new ItemStack(Material.DIAMOND);
            }
            player.sendMessage(ChatColor.GOLD+"[抽奖小助手]"+ChatColor.GREEN+"这是你的第"+ LotteryTimes.getCurrentTimes(player.getUniqueId(),lottery.getName()) +"次未中保底物品");
        }

        int randomWeight = new Random().nextInt(lottery.getWeightSum())+1;
        List<ItemStack> items = new ArrayList<>(lottery.getItems()) ;
        List<Integer> weights = new ArrayList<>(lottery.getWeights()) ;
        for(int i =0;i<items.size();i++){
            ItemStack item = items.get(i);
            randomWeight -=weights.get(i);
            if(randomWeight<=0){
                return new ItemStack(item);
            }
        }
        return null;
    }
}
