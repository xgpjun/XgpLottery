package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.TimesTop;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopCommand implements TabExecutor {
    /*
   /XgpLottery top [奖池名称] (current)
    */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("xgplottery.manager")){
            sender.sendMessage(ChatColor.RED+"你没有权限这么做！");
            return true;
        }
        if(args.length!=3&&args.length!=2){
            sender.sendMessage(ChatColor.RED + "输入格式有误");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery top [奖池名称] (current)\n" + ChatColor.GREEN + "列出该奖池的所有玩家抽奖次数（未保底次数）");
            return true;
        }
        Lottery lottery = XgpLottery.lotteryList.get(args[1]);
        if (lottery == null) {
            sender.sendMessage(ChatColor.RED + "啊咧咧，没有找到奖池捏~");
            return true;
        }
        if(args.length==2){
            TimesTop top = new TimesTop(true,lottery.getName());
            sender.sendMessage(ChatColor.GREEN + "---这是该奖池的总抽奖次数排行榜---");
            int i =1;
            for(String str: top.top){
                sender.sendMessage(ChatColor.GREEN+Integer.toString(i)+" "+ str);
                i++;
                if(i>=10)
                    break;
            }
        }else {
            TimesTop top = new TimesTop(false,lottery.getName());
            sender.sendMessage(ChatColor.GREEN + "---这是该奖池的当前未保底次数排行榜---");
            int i =1;
            for(String str: top.top){
                sender.sendMessage(ChatColor.GREEN+Integer.toString(i)+" "+ str);
                i++;
                if(i>=10)
                    break;
            }
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("xgplottery.manager")){
            return null;
        }

        if(args.length == 2){
            List<String> strings = new ArrayList<>(XgpLottery.lotteryList.keySet());
            return XgpLotteryCommand.filter(strings,args);
        }
        if(args.length == 3){
            return Collections.singletonList("current");
        }
        return null;
    }


}
