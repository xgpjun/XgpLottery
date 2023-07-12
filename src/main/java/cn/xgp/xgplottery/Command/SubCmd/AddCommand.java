package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Lottery.Award;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import cn.xgp.xgplottery.Utils.VersionAdapterUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCommand implements TabExecutor {

    /*
    /xl add item 123
    /xl add award 123
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player &&sender.hasPermission("xgplottery.manager"))){
            sender.sendMessage(ChatColor.RED+ LangUtils.DontHavePermission);
            return true;
        }

        Player player = (Player) sender;
        if(args.length!=3||(!(args[1].equals("item"))&&!(args[1].equals("award")))){
            sender.sendMessage(ChatColor.RED+LangUtils.WrongInput);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery add item "+LangUtils.LotteryName+"\n" + ChatColor.GREEN + LangUtils.CmdAdd1);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery add award "+LangUtils.LotteryName+"\n" + ChatColor.GREEN + LangUtils.CmdAdd2);
            return true;
        }
        String name = args[2];
        Lottery lottery = XgpLottery.lotteryList.get(name);
        if(lottery==null){
            player.sendMessage(ChatColor.RED+LangUtils.NotFoundLottery);
            return true;
        }
        ItemStack item = VersionAdapterUtils.getItemInMainHand(player);
        if(item.getType().equals(Material.AIR)){
            player.sendMessage(ChatColor.RED+LangUtils.NotFoundItemInHand);
            return true;
        }
        if(args[1].equals("item")){
            if(lottery.getAmount()>=45){
                player.sendMessage(ChatColor.RED+LangUtils.LotteryIsFull);
                return true;
            }
            lottery.getAwards().add(new Award(item));
            player.sendMessage(ChatColor.GREEN+LangUtils.AddItemSuccessfully);
        }else {
            if(lottery.getSpAmount()>=45){
                player.sendMessage(ChatColor.RED+LangUtils.LotteryIsFull);
                return true;
            }
            lottery.getSpAwards().add(new Award(item));
            System.out.println(item.getClass());
            player.sendMessage(ChatColor.GREEN+LangUtils.AddItemSuccessfully);
        }
        SerializeUtils.saveLotteryData();
        return true;
    }
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2){
            return XgpLotteryCommand.filter(new ArrayList<>(Arrays.asList("item", "award")),args);
        }
        if(args.length == 3){
            List<String> strings = new ArrayList<>(XgpLottery.lotteryList.keySet());
            return XgpLotteryCommand.filter(strings,args);
        }
        return new ArrayList<>();
    }
}
