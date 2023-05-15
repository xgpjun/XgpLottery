package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Utils.SerializeUtils;
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

public class AddCommand implements TabExecutor {

    /*
    /xl add item 123
    /xl add award 123
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player &&sender.hasPermission("xgplottery.manager"))){
            sender.sendMessage(ChatColor.RED+"你没有权限这么做！");
            return true;
        }

        Player player = (Player) sender;
        if(args.length!=3||(!(args[1].equals("item"))&&!(args[1].equals("award")))){
            sender.sendMessage(ChatColor.RED+"输入格式有误");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery add item [奖池名称]\n" + ChatColor.GREEN + "把手中的物品加入指定奖池的普通物品列表");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery add award [奖池名称]\n" + ChatColor.GREEN + "把手中的物品加入指定奖池的保底物品列表");
            return true;
        }
        String name = args[2];
        Lottery lottery = XgpLottery.lotteryList.get(name);
        if(lottery==null){
            player.sendMessage(ChatColor.RED+"啊咧咧？ 没找到奖池呢~");
            return true;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getType().equals(Material.AIR)){
            player.sendMessage(ChatColor.RED+"没找到手上有物品捏~");
            return true;
        }
        if(args[1].equals("item")){
            if(lottery.getAmount()>=45){
                player.sendMessage(ChatColor.RED+name+"奖池已经满啦！");
                return true;
            }
            lottery.addItem(item.clone());
            player.sendMessage(ChatColor.GREEN+"向"+name+"添加物品成功！");
        }else {
            if(lottery.getSpAmount()>=45){
                player.sendMessage(ChatColor.RED+name+"奖池已经满啦！");
                return true;
            }
            lottery.addSpItem(item.clone());
            player.sendMessage(ChatColor.GREEN+"向"+name+"添加保底物品成功！");
        }
        SerializeUtils.saveLotteryData();
        return true;
    }
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("xgplottery.manager")){
            return null;
        }
        if(args.length == 2){
            return XgpLotteryCommand.filter(new ArrayList<>(Arrays.asList("item", "award")),args);
        }
        if(args.length == 3){
            List<String> strings = new ArrayList<>(XgpLottery.lotteryList.keySet());
            return XgpLotteryCommand.filter(strings,args);
        }
        return null;
    }
}
