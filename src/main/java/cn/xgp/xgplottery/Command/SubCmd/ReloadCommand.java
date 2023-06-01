package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender.hasPermission("xgplottery.manager"))){
            sender.sendMessage(ChatColor.RED+ LangUtils.DontHavePermission);
            return true;
        }
        if(args.length!=1){
            sender.sendMessage(ChatColor.RED + LangUtils.WrongInput);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery reload\n" + ChatColor.GREEN + LangUtils.CmdReload);
            return true;
        }
        XgpLottery.reload();
        sender.sendMessage(ChatColor.GREEN+LangUtils.ReloadMessage);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
