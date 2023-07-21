package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Listener.RemoveBoxListener;
import cn.xgp.xgplottery.Listener.SelectBoxListener;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Utils.ChatUtils;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoxCommand implements TabExecutor {

    /***
     *             /xl box create 123
     *             /xl box remove
     *             /xl box list
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player && sender.hasPermission("xgplottery.manager"))) {
            sender.sendMessage(ChatColor.RED + LangUtils.DontHavePermission);
            return true;
        }

        if ((args.length != 2 && args.length != 3) || (!args[1].equals("list") && !args[1].equals("remove") && !args[1].equals("create"))) {
            sender.sendMessage(ChatColor.RED + LangUtils.WrongInput);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery box create " + LangUtils.LotteryName + "\n" + ChatColor.GREEN + LangUtils.CmdBox1);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery box remove\n" + ChatColor.GREEN + LangUtils.CmdBox2);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery box list\n" + ChatColor.GREEN + "查看抽奖箱列表");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 2) {
            if (args[1].equals("create")) {
                player.sendMessage(ChatColor.AQUA + LangUtils.LotteryPrefix + ChatColor.GREEN + LangUtils.CreateBox);
                Bukkit.getPluginManager().registerEvents(new RemoveBoxListener(player.getUniqueId()), XgpLottery.instance);
                return true;
            } else if (args[1].equals("list")) {
                ChatUtils.sendBoxList(player);
            }
        }
        if (args.length == 3) {
            if (args[1].equals("create")) {
                Lottery lottery = XgpLottery.lotteryList.get(args[2]);
                if (lottery == null) {
                    sender.sendMessage(ChatColor.RED + LangUtils.NotFoundLottery);
                    return true;
                }
                player.sendMessage(ChatColor.AQUA + LangUtils.LotteryPrefix + ChatColor.GREEN + LangUtils.RemoveBox);
                Bukkit.getPluginManager().registerEvents(new SelectBoxListener(player.getUniqueId(), lottery), XgpLottery.instance);
            } else if (args[1].equals("list")) {
                try {
                    int index = Integer.parseInt(args[2]);
                    Location location = XgpLottery.lotteryBoxList.get(index).getLocation();
                    player.teleport(location.add(0, 2, 0));
                    return true;
                } catch (Exception e) {
                    XgpLottery.log(e.getMessage());
                }
            }
        }
        return true;

    }
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2){
            return XgpLotteryCommand.filter(new ArrayList<>(Arrays.asList("create", "remove", "list")), args);
        }
        if(args.length == 3&&args[1].equals("create")){
            List<String> strings = new ArrayList<>(XgpLottery.lotteryList.keySet());
            return XgpLotteryCommand.filter(strings,args);
        }
        return new ArrayList<>();
    }
}
