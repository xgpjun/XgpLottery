package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GiveUtils {

    public static void giveLottery(Player player , String lotteryName){
        if(ConfigSetting.giveLottery){
            if(ConfigSetting.giveKey)
                giveKey(player,lotteryName,1);
            else
                giveTicket(player,lotteryName,1);
        }
        else
            open(player,lotteryName);
    }

    public static void giveKey(Player player ,String lotteryName,int amount){
        MyItem key = new MyItem(Material.BLAZE_ROD)
                .setDisplayName(ChatColor.GOLD+lotteryName+"-抽奖箱钥匙")
                .setLore(ChatColor.GOLD+"✦"+ChatColor.AQUA+"使用方法：手持右键抽奖箱"+ChatColor.GOLD+"✦")
                .addEnchant()
                .setAmount(amount);
        player.getInventory().addItem(key.getItem());

    }
    public static void giveTicket(Player player ,String lotteryName,int amount){
        MyItem ticket = new MyItem(Material.PAPER)
                .setDisplayName(ChatColor.GOLD+lotteryName+"-抽奖券")
                .setLore(ChatColor.GOLD+"✦"+ChatColor.AQUA+"右键以抽奖"+ChatColor.GOLD+"✦")
                .addEnchant()
                .setAmount(amount);
        player.getInventory().addItem(ticket.getItem());
    }
    public static void open(Player player ,String lotteryName){
        Lottery lottery = XgpLottery.lotteryList.get(lotteryName);
        player.closeInventory();
        lottery.open(player,true);
    }
}
