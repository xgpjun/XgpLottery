package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Gui.Impl.RecordGui;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecordCommand implements TabExecutor {
    /***
     * /xl record lotteryName
     * /xl record lotteryName playerName (For Administrator)
     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player )){
            sender.sendMessage(ChatColor.RED+ LangUtils.NotPlayerMsg);
            return true;
        }

        Player player = (Player) sender;
        if(args.length<2||args.length>3){
            sender.sendMessage(ChatColor.RED+LangUtils.WrongInput);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery record "+LangUtils.LotteryName+"\n" + ChatColor.GREEN + LangUtils.CmdRecord1);
            if(player.hasPermission("xgplottery.manager"))
                sender.sendMessage(ChatColor.AQUA + "/XgpLottery record "+LangUtils.LotteryName+" "+LangUtils.PlayerName+"\n" + ChatColor.GREEN + LangUtils.CmdRecord2);
            return true;
        }
        if(!XgpLottery.lotteryList.containsKey(args[1])){
            sender.sendMessage(ChatColor.RED+LangUtils.NotFoundLottery);
            return true;
        }
        if(!(sender.hasPermission("xgplottery.manager"))||args.length==2){
            Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance,()->{
                Inventory inv = new RecordGui(player,args[1],null).getInventory();
                Bukkit.getScheduler().runTask(XgpLottery.instance, () -> player.openInventory(inv));
            });
            return true;
        }

        OfflinePlayer p = Bukkit.getOfflinePlayer(args[2]);

        player.openInventory(new RecordGui(p,args[1],null).getInventory());

        return true;

    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2){
            List<String> strings = new ArrayList<>(XgpLottery.lotteryList.keySet());
            return XgpLotteryCommand.filter(strings,args);
        }
        return null;
    }
}
