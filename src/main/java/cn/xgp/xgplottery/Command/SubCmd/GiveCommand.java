package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Gui.MyItem;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Utils.GiveUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GiveCommand implements TabExecutor {
    /**
     * usage
     * /xl give [Player] key [Lottery] amount
     * /xl give [Player] ticket [lottery] amount
     * /xl give [Player] open [lottery]
     * args = 0:give 1:player 2:option 3:lottery amount
     * */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!sender.hasPermission("xgplottery.manager")){
            sender.sendMessage(ChatColor.RED+"你没有权限这么做！");
            return true;
        }
        if(!(args.length >= 4 && args.length <= 5)){
            sender.sendMessage(ChatColor.RED+"输入格式有误");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery give [玩家名称] key [奖池名称] (数量)\n" + ChatColor.GREEN + "给与玩家默认材质的开箱钥匙");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery give [玩家名称] ticket [奖池名称] (数量)\n" + ChatColor.GREEN + "给与玩家默认材质的奖券");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery give [玩家名称] open [奖池名称]\n" + ChatColor.GREEN + "直接让玩家进行抽奖");
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);
        Lottery lottery = XgpLottery.lotteryList.get(args[3]);
        String lotteryName;
        String option = args[2];
        int amount = 1;
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "未找到该玩家");
            return true;
        }
        if (lottery == null) {
            sender.sendMessage(ChatColor.RED + "啊咧咧，没有找到奖池捏~");
            return true;
        }
        lotteryName = lottery.getName();
        if (args.length == 5) {
            try {
                amount = Integer.parseInt(args[4]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "格式输入有误");
                sender.sendMessage(ChatColor.RED + "使用/xgplottery help查看帮助");
                return true;
            }
        }
        switch (option) {
            case "key":
                GiveUtils.giveKey(player, lotteryName, amount);
                sender.sendMessage(ChatColor.GREEN+"成功给与"+player.getName());
                break;
            case "ticket":
                GiveUtils.giveTicket(player, lotteryName, amount);
                sender.sendMessage(ChatColor.GREEN+"成功给与"+player.getName());
                break;
            case "open":
                GiveUtils.open(player, lotteryName);
                sender.sendMessage(ChatColor.GREEN+"成功给与"+player.getName());
                break;
            default:
                sender.sendMessage(ChatColor.RED + "您的输入有误");
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("xgplottery.manager")){
            return null;
        }
        if(args.length == 3){
            return XgpLotteryCommand.filter(new ArrayList<>(Arrays.asList("key", "ticket","open")) ,args);
        }
        if(args.length == 4){
            List<String> strings = new ArrayList<>(XgpLottery.lotteryList.keySet());
            return XgpLotteryCommand.filter(strings,args);
        }
        if(args.length==5&&(args[3].equals("key")||args[3].equals("ticket"))){
            return Collections.singletonList("<数量>");
        }
        return null;
    }

}
