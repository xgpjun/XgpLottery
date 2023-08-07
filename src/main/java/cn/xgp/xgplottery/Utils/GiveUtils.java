package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.Award;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.XgpLottery;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class GiveUtils {

    public static void giveLottery(Player player , String lotteryName , int amount){
        if(ConfigSetting.giveLottery){
            if(ConfigSetting.giveKey)
                giveKey(player,lotteryName,amount);
            else
                giveTicket(player,lotteryName,amount);
        }else {
            open(player,lotteryName,false);
        }
    }

    public static void giveKey(Player player ,String lotteryName,int amount){
        Lottery lottery = XgpLottery.lotteryList.get(lotteryName);
        if(lottery==null)
            return;
        MyItem key = new MyItem(lottery.getKeyItemStack())
                .setDisplayName(lottery.getKeyName())
                .setLore(lottery.getKeyLore())
                .addEnchant()
                .setAmount(amount);
        addItem(player,NMSUtils.addTag(key.getItem(),true,lotteryName));
    }
    public static void giveTicket(Player player ,String lotteryName,int amount){
        Lottery lottery = XgpLottery.lotteryList.get(lotteryName);
        if(lottery==null)
            return;
        MyItem ticket = new MyItem(lottery.getTicketItemStack())
                .setDisplayName(lottery.getTicketName())
                .setLore(lottery.getTicketLore())
                .addEnchant()
                .setAmount(amount);
        addItem(player,NMSUtils.addTag(ticket.getItem(),false,lotteryName));
    }

    /**
     * 指令专用！
     */
    public static void open(Player player ,String lotteryName,boolean isMultiple){
        Lottery lottery = XgpLottery.lotteryList.get(lotteryName);
        player.closeInventory();
        lottery.open(player,true,isMultiple);
    }

    public static void addItem(Player player , ItemStack itemStack){
        Map<Integer, ItemStack> map = player.getInventory().addItem(itemStack);
        if(map.isEmpty())
            return;
        for(ItemStack item : map.values()){
            player.getWorld().dropItemNaturally(player.getLocation(),item);
        }
    }

    //处理给予奖品的逻辑
    public static void giveAward(Player player, Award award){
        if(award.isExecuteCommands()&&!award.getCommands().isEmpty()){
            //先执行指令
            for (String cmd : award.getCommands()){
                if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null){
                    cmd = PlaceholderAPI.setPlaceholders(player,cmd);
                }
                String finalCmd = cmd;
                Bukkit.getGlobalRegionScheduler().run(XgpLottery.instance, scheduledTask -> XgpLottery.instance.getServer().dispatchCommand(XgpLottery.instance.getServer().getConsoleSender(), finalCmd));
                player.sendMessage(ChatColor.GOLD+LangUtils.LotteryPrefix+"抽到了: "+ChatColor.GREEN+cmd);
                XgpLottery.log(player.getName()+"抽到了: "+cmd);
            }
        }
        //给予物品
        if(award.isGiveItem()){
            addItem(player,award.getItem().clone());
        }
    }

    public static void giveAward(Player player, List<Award> awards){
        for (Award value:awards){
            giveAward(player,value);
        }
    }
}
