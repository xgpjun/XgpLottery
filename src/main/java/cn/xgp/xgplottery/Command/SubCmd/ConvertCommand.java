package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.SqlUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConvertCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("xgplottery.manager")){
            sender.sendMessage(ChatColor.RED+ LangUtils.DontHavePermission);
            return true;
        }
        SqlUtils.convertToDatabase();
        sender.sendMessage(ChatColor.GREEN+LangUtils.ConvertSuccessfully);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
