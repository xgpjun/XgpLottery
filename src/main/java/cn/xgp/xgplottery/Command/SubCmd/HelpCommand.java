package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ChatColor.GREEN+"------XgpLottery"+ChatColor.AQUA+" Help"+ChatColor.GREEN+"------\n"+ChatColor.GOLD+ "你也可以使用"+ ChatColor.GREEN+"/xl  " + ChatColor.GREEN+"/lottery "+ChatColor.GOLD+"作为替代命令");
        if(sender.hasPermission("xgplottery.manager")){
            if(args.length != 2||args[1].equals("1")) {
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery help <页码>");
                sender.sendMessage(ChatColor.GREEN + "获取帮助信息");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery add item [奖池名称]\n" + ChatColor.GREEN + "把手中的物品加入指定奖池的普通物品列表");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery add award [奖池名称]\n" + ChatColor.GREEN + "把手中的物品加入指定奖池的保底物品列表");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery box create [奖池名称]\n" + ChatColor.GREEN + "创建一个指定奖池的抽奖箱");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery box remove\n" + ChatColor.GREEN + "删除一个抽奖箱");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery change [奖池名称]\n" + ChatColor.GREEN + "更改指定奖池的售卖模式");
                sender.sendMessage(ChatColor.GREEN + "------页数<1 / 3>------");
            }else if (args[1].equals("2")) {
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery delete [奖池名称]\n" + ChatColor.GREEN + "删除指定奖池，本命令会并清除掉相关数据");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery get ticket [奖池名称]\n" + ChatColor.GREEN + "把手中的物品设置为指定奖池的抽奖券");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery get key [奖池名称]\n" + ChatColor.GREEN + "把手中的物品设置为指定奖池的抽奖箱钥匙");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery give [玩家名称] key [奖池名称] (数量)\n" + ChatColor.GREEN + "给与玩家默认材质的开箱钥匙");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery give [玩家名称] ticket [奖池名称] (数量)\n" + ChatColor.GREEN + "给与玩家默认材质的奖券");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery give [玩家名称] open [奖池名称]\n" + ChatColor.GREEN + "直接让玩家进行抽奖");

                sender.sendMessage(ChatColor.GREEN + "------页数<2 / 3>------");
            }else if(args[1].equals("3")){
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery menu\n" + ChatColor.GREEN + "打开管理页面");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery papi [占位符]\n" + ChatColor.GREEN + "测试占位符用");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery particle show\n" + ChatColor.GREEN + "启用所有粒子特效");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery particle clear\n" + ChatColor.GREEN + "关闭所有粒子特效");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery reload\n" + ChatColor.GREEN + "重载插件");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery shop\n" + ChatColor.GREEN + "打开商店");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery show [奖池名称]\n" + ChatColor.GREEN + "打开指定奖池预览");
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery top [奖池名称] (current)\n" + ChatColor.GREEN + "列出该奖池的所有玩家抽奖次数（未保底次数）");
                sender.sendMessage(ChatColor.GREEN + "------页数<3 / 3>------");
            }
        }else {
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery shop\n" + ChatColor.GREEN + "打开商店");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery show [奖池名称]\n" + ChatColor.GREEN + "打开指定奖池预览");

        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!sender.hasPermission("xgplottery.manager"))
            return new ArrayList<>();
        if(args.length == 2){
            return XgpLotteryCommand.filter(new ArrayList<>(Arrays.asList("<页数>", "1","2","3")),args);
        }
        return new ArrayList<>();
    }
}
