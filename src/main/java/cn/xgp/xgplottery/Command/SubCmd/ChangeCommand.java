package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ChangeCommand implements CommandExecutor {

    /*
    /xl change 123
     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("xgplottery.manager")){
            sender.sendMessage(ChatColor.RED+"你没有权限这么做！");
            return true;
        }
        if(!(args.length ==2)){
            sender.sendMessage(ChatColor.RED+"输入格式有误");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery change [奖池名称]\n" + ChatColor.GREEN + "更改指定奖池的售卖模式");
            return true;
        }
        Lottery lottery =XgpLottery.lotteryList.get(args[1]);
        if(lottery==null){
            sender.sendMessage(ChatColor.RED+"啊咧咧？ 没找到奖池呢~");
            return true;
        }
        lottery.setPoint(!lottery.isPoint());
        String str = lottery.isPoint()?"点券":"金币";
        sender.sendMessage(ChatColor.GREEN+"成功更换为"+ChatColor.AQUA+str);
        return true;
    }
}
