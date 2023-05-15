package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Gui.Impl.Shop.LotteryShop;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ShopCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player&&(sender.hasPermission("xgplottery.shop")||sender.hasPermission("xgplottery.manager")))){
            sender.sendMessage(ChatColor.RED+"你没有权限这么做！");

            return true;
        }
        if(args.length!=1){
            sender.sendMessage(ChatColor.RED + "输入格式有误");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery shop\n" + ChatColor.GREEN + "打开商店");
            return true;
        }
        Player player = (Player) sender;
        player.openInventory(new LotteryShop(player).getInventory());
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
