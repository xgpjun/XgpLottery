package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryMenuGui;
import cn.xgp.xgplottery.Listener.RemoveBoxListener;
import cn.xgp.xgplottery.Listener.SelectBoxListener;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Utils.LotteryUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BoxCommand implements CommandExecutor {

    /***
     *
     *             /xl box create 123
     *             /xl box remove
     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player &&sender.hasPermission("xgplottery.manager"))){
            sender.sendMessage(ChatColor.RED+"你没有权限这么做！");
            return true;
        }
        Player player = (Player) sender;
        if(args.length==2&&args[1].equals("remove")){
            player.sendMessage(ChatColor.AQUA+"[操作提示]"+ChatColor.GREEN+"左键点击方块删除抽奖箱");
            Bukkit.getPluginManager().registerEvents(new RemoveBoxListener(player.getUniqueId()),XgpLottery.instance);
            return true;
        }
        Lottery lottery = XgpLottery.lotteryList.get(args[2]);
        if(lottery==null){
            sender.sendMessage(ChatColor.RED+"啊咧咧？ 没找到奖池呢~");
            return true;
        }
        if((args.length==3&&args[1].equals("create"))){
            player.sendMessage(ChatColor.AQUA+"[操作提示]"+ChatColor.GREEN+"左键点击方块标记为抽奖箱");
            Bukkit.getPluginManager().registerEvents(new SelectBoxListener(player.getUniqueId(),lottery),XgpLottery.instance);
            return true;
        }
        sender.sendMessage(ChatColor.RED+"输入格式有误");
        sender.sendMessage(ChatColor.AQUA + "/XgpLottery box create [奖池名称]\n" + ChatColor.GREEN + "创建一个指定奖池的抽奖箱");
        sender.sendMessage(ChatColor.AQUA + "/XgpLottery box remove\n" + ChatColor.GREEN + "删除一个抽奖箱");

        return true;
    }

}
