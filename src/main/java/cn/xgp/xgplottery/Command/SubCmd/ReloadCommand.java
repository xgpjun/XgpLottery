package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryMenuGui;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender.hasPermission("xgplottery.manager"))){
            sender.sendMessage(ChatColor.RED+"你没有权限这么做！");
            return true;
        }
        if(args.length!=1){
            sender.sendMessage(ChatColor.RED + "输入格式有误");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery reload\n" + ChatColor.GREEN + "重载插件");
            return true;
        }
        XgpLottery.reload();
        sender.sendMessage(ChatColor.GREEN+"重载成功!");
        return true;
    }
}
