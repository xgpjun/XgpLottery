package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Utils.LangUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PapiCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("xgplottery.manager")){
            sender.sendMessage(ChatColor.RED+ LangUtils.DontHavePermission);
            return true;
        }

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI")==null)
            sender.sendMessage(ChatColor.RED+ LangUtils.NotFoundAPI+"PlaceHolderAPI");
        if(args.length==2&&args[0].equals("papi")){
            String str = args[1];
            str = PlaceholderAPI.setPlaceholders((Player) sender,str);
            sender.sendMessage(str);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
