package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolShow;
import cn.xgp.xgplottery.Lottery.Lottery;
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
            sender.sendMessage(ChatColor.RED+"你没有权限这么做！");

            return true;
        }
        if(args.length!=2){
            sender.sendMessage(ChatColor.RED+"输入格式有误");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery show [奖池名称]\n" + ChatColor.GREEN + "打开指定奖池预览");
            return true;
        }
        String name = args[1];
        Lottery lottery = XgpLottery.lotteryList.get(name);
        if(lottery==null){
            sender.sendMessage(ChatColor.RED+"啊咧咧？ 没找到奖池呢~");
            return true;
        }
        Player player = ((Player) sender);
        player.openInventory(new LotteryPoolShow(lottery).getInventory());


        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
