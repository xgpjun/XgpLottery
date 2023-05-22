package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.XgpLottery;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PapiCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(XgpLottery.ppAPI==null)
            sender.sendMessage(ChatColor.RED+ "未检测到PlaceHolderAPI");
        if(args.length==2&&args[0].equals("papi")){
            String str = args[1];
            str = PlaceholderAPI.setPlaceholders((Player) sender,str);
            sender.sendMessage(str);
        }else {
            sender.sendMessage(ChatColor.RED + "输入格式有误");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery papi [占位符]\n" + ChatColor.GREEN + "测试占位符用");

        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
