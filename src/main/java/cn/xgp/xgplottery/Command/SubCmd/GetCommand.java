package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.VersionAdapterUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetCommand implements TabExecutor {

    /***
     * /xl get ticket 123
     * /xl get key 123
     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player &&sender.hasPermission("xgplottery.manager"))){
            sender.sendMessage(ChatColor.RED+"你没有权限这么做！");
            return true;
        }
        if(args.length!=3||(!args[1].equals("ticket")&&!args[1].equals("key"))){
            sender.sendMessage(ChatColor.RED+"输入格式有误");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery get ticket [奖池名称]\n" + ChatColor.GREEN + "把手中的物品设置为指定奖池的抽奖券");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery get key [奖池名称]\n" + ChatColor.GREEN + "把手中的物品设置为指定奖池的抽奖箱钥匙");
            return true;
        }

        Player player = (Player) sender;
        String name = args[2];
        if(!XgpLottery.lotteryList.containsKey(name)){
            player.sendMessage(ChatColor.RED+"啊咧咧？ 没找到奖池呢~");
            return true;
        }
        ItemStack item = VersionAdapterUtils.getItemInMainHand(player);
        if(item.getType()== Material.AIR){
            player.sendMessage(ChatColor.RED+"没找到手上有物品捏~");
            return true;
        }
        MyItem guiItem = new MyItem(item);
        if(args[1].equals("ticket")){
            guiItem.setDisplayName(ChatColor.GOLD+name+"-抽奖券")
                    .setLore(ChatColor.GOLD+"✦"+ChatColor.AQUA+"右键以抽奖")
                    .addEnchant();
        }else {
            guiItem.setDisplayName(ChatColor.GOLD+name+"-抽奖箱钥匙")
                    .setLore(ChatColor.GOLD+"✦"+ChatColor.AQUA+"使用方法：手持右键抽奖箱")
                    .addEnchant();
        }
        VersionAdapterUtils.setItemInMainHand(player,guiItem.getItem());
        return true;
    }
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2){
            return XgpLotteryCommand.filter(new ArrayList<>(Arrays.asList("key", "ticket")),args);
        }
        if(args.length == 3){
            List<String> strings = new ArrayList<>(XgpLottery.lotteryList.keySet());
            return XgpLotteryCommand.filter(strings,args);
        }
        return new ArrayList<>();
    }
}
