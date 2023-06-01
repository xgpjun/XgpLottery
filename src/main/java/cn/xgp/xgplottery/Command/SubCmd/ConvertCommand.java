package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.SqlUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConvertCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("xgplottery.manager")){
            sender.sendMessage(ChatColor.RED+ LangUtils.DontHavePermission);
            return true;
        }
        if(args.length==1){
            SqlUtils.convertToDatabase();
            sender.sendMessage(ChatColor.GREEN+LangUtils.ConvertSuccessfully);
            return true;
        }
        if(args.length==2&&args[1].equals("file")){
            SqlUtils.convertToFile();
            sender.sendMessage(ChatColor.GREEN+LangUtils.ConvertSuccessfully);
            return true;
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 2){
            List<String> strings = new ArrayList<>(Collections.singletonList("file"));
            return XgpLotteryCommand.filter(strings,args);
        }
        return new ArrayList<>();
    }
}
