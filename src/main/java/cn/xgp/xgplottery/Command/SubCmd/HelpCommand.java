package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ChatColor.GREEN+"------XgpLottery"+ChatColor.AQUA+" Help"+ChatColor.GREEN+"------");
        sender.sendMessage(ChatColor.GOLD+ "你也可以使用"+ ChatColor.GREEN+"/xl  " + ChatColor.GREEN+"/lottery "+ChatColor.GOLD+"作为替代命令");
        if(args.length==1||(args.length>1&& args[1].equals("1"))){
            sender.sendMessage(ChatColor.AQUA+"/XgpLottery help <页码>");
            sender.sendMessage(ChatColor.GREEN+"获取帮助信息");

            sender.sendMessage(ChatColor.GREEN+"------页数<1/2>------");
        }else if(args.length==2){

        }
        return true;
    }
}
