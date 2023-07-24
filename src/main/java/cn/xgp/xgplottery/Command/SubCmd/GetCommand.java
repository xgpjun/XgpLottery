package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.NMSUtils;
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

public class GetCommand implements TabExecutor {

    /***
     * /xl get ticket 123
     * /xl get key 123
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player &&sender.hasPermission("xgplottery.manager"))){
            sender.sendMessage(ChatColor.RED+ LangUtils.DontHavePermission);
            return true;
        }
        if(args.length<3||(!args[1].equals("ticket")&&!args[1].equals("key"))){
            sender.sendMessage(ChatColor.RED+LangUtils.WrongInput);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery get ticket "+LangUtils.LotteryName+  "\n" + ChatColor.GREEN + LangUtils.CmdGet1);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery get key "+LangUtils.LotteryName+"\n" + ChatColor.GREEN + LangUtils.CmdGet2);
            return true;
        }

        Player player = (Player) sender;
        String name = args[2];
        if(!XgpLottery.lotteryList.containsKey(name)){
            player.sendMessage(ChatColor.RED+LangUtils.NotFoundLottery);
            return true;
        }
        ItemStack item = VersionAdapterUtils.getItemInMainHand(player);
        Lottery lottery = XgpLottery.lotteryList.get(name);
        if(item.getType()== Material.AIR){
            player.sendMessage(ChatColor.RED+LangUtils.NotFoundItemInHand);
            return true;
        }
        MyItem guiItem = new MyItem(item);
        if ("ticket".equals(args[1])) {
            guiItem.setDisplayName(lottery.getTicketName())
                    .addLore(lottery.getTicketLore())
                    .addEnchant();
            VersionAdapterUtils.setItemInMainHand(player, NMSUtils.addTag(guiItem.getItem(), false, name));
        } else {
            guiItem.setDisplayName(lottery.getKeyName())
                    .addLore(lottery.getKeyName())
                    .addEnchant();
            VersionAdapterUtils.setItemInMainHand(player, NMSUtils.addTag(guiItem.getItem(), true, name));
        }
        return true;
    }
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2){
            return XgpLotteryCommand.filter(new ArrayList<>(Arrays.asList("key", "ticket")),args);
        }
        if(args.length == 3){
            List<String> strings = new ArrayList<>(XgpLottery.lotteryList.keySet());
            return XgpLotteryCommand.filter(strings,args);
        }
        return new ArrayList<>();
    }
}
