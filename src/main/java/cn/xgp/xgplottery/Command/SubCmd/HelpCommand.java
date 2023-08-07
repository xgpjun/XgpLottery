package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Utils.LangUtils;
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
        sender.sendMessage(ChatColor.GREEN+"------XgpLottery"+ChatColor.AQUA+" Help"+ChatColor.GREEN+"------\n"+ChatColor.GOLD+ LangUtils.CmdHelpTitle1+ ChatColor.GREEN+"/xl  /lottery"+ChatColor.GOLD+LangUtils.CmdHelpTitle2);
        if(sender.hasPermission("xgplottery.manager")){
            if(args.length != 2||args[1].equals("1")) {
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery help " + LangUtils.Page);
                sender.sendMessage(ChatColor.GREEN + LangUtils.CmdHelp);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery add item " + LangUtils.LotteryName + "\n" + ChatColor.GREEN + LangUtils.CmdAdd1);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery add award " + LangUtils.LotteryName + "\n" + ChatColor.GREEN + LangUtils.CmdAdd2);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery box create " + LangUtils.LotteryName + "\n" + ChatColor.GREEN + LangUtils.CmdBox1);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery box remove\n" + ChatColor.GREEN + LangUtils.CmdBox2);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery box list\n" + ChatColor.GREEN + LangUtils.CmdBox3);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery change " + LangUtils.LotteryName + "\n" + ChatColor.GREEN + LangUtils.CmdChange);
                sender.sendMessage(ChatColor.GREEN + "------<1 / 4>------");
            }else if (args[1].equals("2")) {
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery delete "+LangUtils.LotteryName+"\n" + ChatColor.GREEN + LangUtils.CmdDelete);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery get ticket "+LangUtils.LotteryName+  "\n" + ChatColor.GREEN + LangUtils.CmdGet1);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery get key "+LangUtils.LotteryName+"\n" + ChatColor.GREEN + LangUtils.CmdGet2);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery give "+LangUtils.PlayerName+" key "+LangUtils.LotteryName+" "+LangUtils.Amount+"\n" + ChatColor.GREEN + LangUtils.CmdGive1);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery give "+LangUtils.PlayerName+" ticket "+LangUtils.LotteryName+" "+LangUtils.Amount+"\n" + ChatColor.GREEN + LangUtils.CmdGive2);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery give "+LangUtils.PlayerName+" open "+LangUtils.LotteryName+" (multiple)\n" + ChatColor.GREEN + LangUtils.CmdGive3);

                sender.sendMessage(ChatColor.GREEN + "------<2 / 4>------");
            }else if(args[1].equals("3")){
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery menu\n" + ChatColor.GREEN + LangUtils.CmdMenu);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery papi\n" + ChatColor.GREEN + LangUtils.CmdPapi);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery particle show\n" + ChatColor.GREEN + LangUtils.CmdParticle1);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery particle clear\n" + ChatColor.GREEN + LangUtils.CmdParticle2);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery reload\n" + ChatColor.GREEN + LangUtils.CmdReload);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery record "+LangUtils.LotteryName+"\n" + ChatColor.GREEN + LangUtils.CmdRecord1);
                sender.sendMessage(ChatColor.GREEN + "------<3 / 4>------");
            }else if(args[1].equals("4")){
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery record "+LangUtils.LotteryName+" "+LangUtils.PlayerName+"\n" + ChatColor.GREEN + LangUtils.CmdRecord2);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery reward\n" + ChatColor.GREEN + LangUtils.CmdReward1);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery shop\n" + ChatColor.GREEN + LangUtils.CmdShop);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery show "+LangUtils.LotteryName+"\n" + ChatColor.GREEN + LangUtils.CmdShow);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery show "+LangUtils.LotteryName+" sp"+"\n" + ChatColor.GREEN + LangUtils.CmdShow);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery top "+LangUtils.LotteryName+" (current)\n" + ChatColor.GREEN + LangUtils.CmdTop);
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery convert (file)\n" + ChatColor.GREEN + LangUtils.CmdConvert);
                sender.sendMessage(ChatColor.GREEN + "------<4 / 4>------");
            }
        }else {
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery shop\n" + ChatColor.GREEN + LangUtils.CmdShop);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery show "+LangUtils.LotteryName+"\n" + ChatColor.GREEN + LangUtils.CmdShow);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery show "+LangUtils.LotteryName+" sp"+"\n" + ChatColor.GREEN + LangUtils.CmdShow);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery record "+LangUtils.LotteryName+"\n" + ChatColor.GREEN + LangUtils.CmdRecord1);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery reward\n" + ChatColor.GREEN + LangUtils.CmdReward1);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!sender.hasPermission("xgplottery.manager"))
            return new ArrayList<>();
        if(args.length == 2){
            return XgpLotteryCommand.filter(new ArrayList<>(Arrays.asList("1","2","3","4")),args);
        }
        return new ArrayList<>();
    }
}
