package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryMenuGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Utils.LotteryUtils;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AddCommand implements CommandExecutor {

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
        String option = args[1];
        if(args.length!=3||(!(option.equals("item"))&&!(option.equals("award")))){
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
        if(option.equals("item")){
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
}
