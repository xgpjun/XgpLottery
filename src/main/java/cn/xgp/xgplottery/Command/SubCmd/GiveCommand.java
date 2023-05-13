package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Gui.MyItem;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveCommand implements CommandExecutor {
    /**
     * usage
     * /xl give [Player] key [Lottery] amount
     * /xl give [Player] ticket [lottery] amount
     * /xl give [Player] open [lottery]
     * args = 0:give 1:player 2:option 3:lottery amount
     * */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length>=4&&args.length<=5){
            Player player = Bukkit.getPlayer(args[1]);
            Lottery lottery = XgpLottery.lotteryList.get(args[3]);
            String lotteryName;
            String option = args[2];
            int amount = 1;
            if(player==null) {
                sender.sendMessage(ChatColor.RED + "未找到该玩家");
                return true;
            }
            if(lottery==null) {
                sender.sendMessage(ChatColor.RED + "啊咧咧，没有找到奖池捏~");
                return true;
            }
            lotteryName = lottery.getName();
            if(args.length==5){
                try{
                    amount = Integer.parseInt(args[4]);
                }catch (NumberFormatException e){
                    sender.sendMessage(ChatColor.RED+"格式输入有误");
                    sender.sendMessage(ChatColor.RED+"使用/xgplottery help查看帮助");
                    return true;
                }
            }
            switch (option){
                case "key": giveKey(player, lotteryName,amount);break;
                case "ticket": giveTicket(player,lotteryName,amount);break;
                case "open": open(player,lotteryName);break;
                default: sender.sendMessage(ChatColor.RED+"您的输入有误");
            }
        }else {
            sender.sendMessage(ChatColor.GREEN+"------XgpLottery"+ChatColor.AQUA+" Help"+ChatColor.GREEN+"------\n"+ChatColor.GOLD+ "你也可以使用"+ ChatColor.GREEN+"/xl  " + ChatColor.GREEN+"/lottery "+ChatColor.GOLD+"作为替代命令");
            sender.sendMessage(ChatColor.AQUA+"/XgpLottery give [玩家名称] key [奖池名称] (数量)\n"+ChatColor.GREEN+"给与玩家默认材质的开箱钥匙");
            sender.sendMessage(ChatColor.AQUA+"/XgpLottery give [玩家名称] ticket [奖池名称] (数量)\n"+ChatColor.GREEN+"给与玩家默认材质的奖券");
            sender.sendMessage(ChatColor.AQUA+"/XgpLottery give [玩家名称] open [奖池名称]\n"+ChatColor.GREEN+"直接让玩家进行抽奖");

        }
        return true;
    }

    public static void giveKey(Player player ,String lotteryName,int amount){
        MyItem key = new MyItem(Material.BLAZE_ROD)
                .setDisplayName(ChatColor.GOLD+lotteryName+"-抽奖箱钥匙")
                .setLore(ChatColor.GOLD+"✦"+ChatColor.AQUA+"使用方法：手持右键抽奖箱"+ChatColor.GOLD+"✦")
                .addEnchant()
                .setAmount(amount);
        player.getInventory().addItem(key.getItem());

    }
    public static void giveTicket(Player player ,String lotteryName,int amount){
        MyItem ticket = new MyItem(Material.PAPER)
                .setDisplayName(ChatColor.GOLD+lotteryName+"-抽奖券")
                .setLore(ChatColor.GOLD+"✦"+ChatColor.AQUA+"右键以抽奖"+ChatColor.GOLD+"✦")
                .addEnchant()
                .setAmount(amount);
        player.getInventory().addItem(ticket.getItem());
    }
    public static void open(Player player ,String lotteryName){
        Lottery lottery = XgpLottery.lotteryList.get(lotteryName);
        player.closeInventory();
        lottery.open(player,true);
    }

}
