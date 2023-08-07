package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Utils.GiveUtils;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
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
            sender.sendMessage(ChatColor.RED+LangUtils.DontHavePermission);
            return true;
        }
        if(!(args.length >= 4 && args.length <= 5)){
            sender.sendMessage(ChatColor.RED+LangUtils.WrongInput);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery give "+LangUtils.PlayerName+" key "+LangUtils.LotteryName+" "+LangUtils.Amount+"\n" + ChatColor.GREEN + LangUtils.CmdGive1);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery give "+LangUtils.PlayerName+" ticket "+LangUtils.LotteryName+" "+LangUtils.Amount+"\n" + ChatColor.GREEN + LangUtils.CmdGive2);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery give "+LangUtils.PlayerName+" open "+LangUtils.LotteryName+" (multiple)\n" + ChatColor.GREEN + LangUtils.CmdGive3);
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);
        Lottery lottery = XgpLottery.lotteryList.get(args[3]);
        String lotteryName;
        String option = args[2];
        int amount = 1;
        if (player == null) {
            sender.sendMessage(ChatColor.RED + LangUtils.NotFoundPlayer);
            return true;
        }
        if (lottery == null) {
            sender.sendMessage(ChatColor.RED + LangUtils.NotFoundLottery);
            return true;
        }
        lotteryName = lottery.getName();
        switch (option) {
            case "key":{
                if (args.length == 5) {
                    try {
                        amount = Integer.parseInt(args[4]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + LangUtils.WrongInput);
                        sender.sendMessage(ChatColor.RED + LangUtils.CmdHelpMsg);
                        return true;
                    }
                }
                GiveUtils.giveKey(player, lotteryName, amount);
                sender.sendMessage(ChatColor.GREEN+LangUtils.GiveSuccessfully+player.getName());
                break;
            }

            case "ticket":{
                if (args.length == 5) {
                    try {
                        amount = Integer.parseInt(args[4]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + LangUtils.WrongInput);
                        sender.sendMessage(ChatColor.RED + LangUtils.CmdHelpMsg);
                        return true;
                    }
                }
                GiveUtils.giveTicket(player, lotteryName, amount);
                sender.sendMessage(ChatColor.GREEN+LangUtils.GiveSuccessfully+player.getName());
                break;
            }
            case "open":{
                GiveUtils.open(player,lotteryName, args.length == 5);
                sender.sendMessage(ChatColor.GREEN+LangUtils.GiveSuccessfully+player.getName());
                break;
            }
            default:
                sender.sendMessage(ChatColor.RED + LangUtils.WrongInput);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2){
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
            return Collections.singletonList(LangUtils.Amount);
        }
        if(args.length==5&&args[3].equals("open")){
            List<String> strings = new ArrayList<>(Collections.singletonList("multiple"));
            return XgpLotteryCommand.filter(strings,args);
        }
        return new ArrayList<>();
    }

}
