package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolShow;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ShowCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player &&(sender.hasPermission("xgplottery.show")||sender.hasPermission("xgplottery.manager")))){
            sender.sendMessage(ChatColor.RED+ LangUtils.DontHavePermission);

            return true;
        }
        if(args.length!=2&&args.length!=3){
            sender.sendMessage(ChatColor.RED+LangUtils.WrongInput);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery show "+LangUtils.LotteryName+"\n" + ChatColor.GREEN + LangUtils.CmdShow);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery show "+LangUtils.LotteryName+" sp"+"\n" + ChatColor.GREEN + LangUtils.CmdShow);
            return true;
        }
        String name = args[1];
        Lottery lottery = XgpLottery.lotteryList.get(name);
        if(lottery==null){
            sender.sendMessage(ChatColor.RED+LangUtils.NotFoundLottery);
            return true;
        }
        Player player = ((Player) sender);
        if(args.length==3){
            player.openInventory(new LotteryPoolShow(lottery,lottery.getSpAwards(),null).getInventory());
        }else {
            player.openInventory(new LotteryPoolShow(lottery,lottery.getAwards(),null).getInventory());
        }




        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
