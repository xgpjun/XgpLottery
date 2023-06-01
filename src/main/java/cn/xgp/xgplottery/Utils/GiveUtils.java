package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

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
        MyItem key = new MyItem(Material.BONE)
                .setDisplayName(ChatColor.GOLD+lotteryName+"-"+LangUtils.KeyName)
                .setLore(ChatColor.GOLD+"✦"+ChatColor.AQUA+LangUtils.KeyLore)
                .addEnchant()
                .setAmount(amount);
        addItem(player,key.getItem());
    }
    public static void giveTicket(Player player ,String lotteryName,int amount){
        MyItem ticket = new MyItem(Material.PAPER)
                .setDisplayName(ChatColor.GOLD+lotteryName+"-"+LangUtils.TicketName)
                .setLore(ChatColor.GOLD+"✦"+ChatColor.AQUA+LangUtils.TicketLore)
                .addEnchant()
                .setAmount(amount);
        addItem(player,ticket.getItem());
    }
    public static void open(Player player ,String lotteryName){
        Lottery lottery = XgpLottery.lotteryList.get(lotteryName);
        player.closeInventory();
        lottery.open(player,true);
    }

    public static void addItem(Player player , ItemStack itemStack){
        Map<Integer, ItemStack> map = player.getInventory().addItem(itemStack);
        if(map.isEmpty())
            return;
        for(ItemStack item : map.values()){
            player.getWorld().dropItemNaturally(player.getLocation(),item);
        }
    }
}
