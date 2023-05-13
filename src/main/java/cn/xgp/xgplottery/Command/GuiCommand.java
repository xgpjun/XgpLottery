package cn.xgp.xgplottery.Command;

import cn.xgp.xgplottery.Gui.GuiItem;
import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryMenuGui;
import cn.xgp.xgplottery.Listener.SelectBoxListener;
import cn.xgp.xgplottery.Lottery.BoxParticle;
import cn.xgp.xgplottery.Lottery.Lottery;

import cn.xgp.xgplottery.Utils.ConfigSetting;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import cn.xgp.xgplottery.XgpLottery;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GuiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length==1&&args[0].equals("menu")){
            Player player= (Player) sender;
            player.openInventory(new LotteryMenuGui().getInventory());
        }
        else if(args.length==1&&args[0].equals("testsave")){
            SerializeUtils.saveLotteryData();
        }
        else if(args.length==1&&args[0].equals("say")){
            Player player= (Player) sender;

            Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance,()->{
                try{
                    player.sendMessage("请输入：");
                    try{
                        String answer = XgpLottery.getInput(player).get(15, TimeUnit.SECONDS);
                        player.sendMessage("你输入的是："+answer);
                    }catch (TimeoutException e){
                        player.sendMessage("给你输入的时间已经过了，已取消");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            // /xl add 123
        }
        else if(args.length==2&&args[0].equals("add")){
            Player player = (Player) sender;
            String name = args[1];
            if(XgpLottery.lotteryList.containsKey(name)){
                Lottery lottery = XgpLottery.lotteryList.get(name);
                if(lottery.getAmount()==45||player.getInventory().getItemInMainHand().getType()== Material.AIR){
                    player.sendMessage(ChatColor.RED+name+"奖池已经满啦！");
                }else {
                    lottery.addItem(player.getInventory().getItemInMainHand().clone());
                    player.sendMessage("向"+name+"添加物品成功！");
                    SerializeUtils.saveLotteryData();
                }
            }else {
                player.sendMessage(ChatColor.RED+"啊咧咧？ 没找到奖池呢~");
            }

        }
        else if(args.length==2&&args[0].equals("open")){
            Player player = (Player) sender;
            String name = args[1];
            if(XgpLottery.lotteryList.containsKey(name)){
                Lottery lottery = XgpLottery.lotteryList.get(name);
                //lottery.getCalculator().giveItem(lottery,player);
            }else {
                player.sendMessage(ChatColor.RED+"啊咧咧？ 没找到奖池呢~");
            }
        }
        else if(args.length==2&&args[0].equals("anim")){
            Player player = (Player) sender;
            String name = args[1];
            if(XgpLottery.lotteryList.containsKey(name)){
                Lottery lottery = XgpLottery.lotteryList.get(name);
                lottery.getAnimation(player,lottery).playAnimation();
            }else {
                player.sendMessage(ChatColor.RED+"啊咧咧？ 没找到奖池呢~");
            }
        }
        else if(args.length==1&&args[0].equals("testgive")){
            Player player = (Player) sender;
            ItemStack award = new ItemStack(Material.DIAMOND);
            player.getInventory().addItem(award);
        }
        if(args.length==2&&args[0].equals("playsound")){
            Player player = (Player)  sender;
            Sound sound;
            try {
                sound = Sound.valueOf(args[1]);
            } catch (IllegalArgumentException e) {
                sender.sendMessage("无效的声音！");
                return true;
            }

            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
            sender.sendMessage("播放声音：" + sound.name());
        }
        if(args.length==2&&args[0].equals("paper")){
            Player player = (Player)  sender;
            String name = args[1];
            if(XgpLottery.lotteryList.containsKey(name)){
                Lottery lottery = XgpLottery.lotteryList.get(name);
                ItemStack item = player.getInventory().getItemInMainHand();
                if(item.getType()!=Material.AIR){
                    GuiItem guiItem = new GuiItem(item);
                    guiItem.setDisplayName(ChatColor.GOLD+lottery.getName()+"-抽奖券")
                            .setLore(ChatColor.GOLD+"✦"+ChatColor.AQUA+"右键以抽奖"+ChatColor.GOLD+"✦").addEnchant();
                    player.getInventory().setItemInMainHand(guiItem.getItem());
                }else {
                    player.sendMessage("没找到手上有物品捏");
                }
            }else {
                player.sendMessage(ChatColor.RED+"啊咧咧？ 没找到奖池呢~");
            }
        }
        if(args.length==1&&args[0].equals("amount")){
            Player player = (Player)  sender;
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            player.sendMessage(String.valueOf(itemStack.getAmount()));
        }
        if(args.length==2&&args[0].equals("createbox")){
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage("请右键点击方块以创建箱子,左键删除已标记的方块");
                Lottery lottery = findLotteryByName(args[1]);
                if(lottery==null){
                    player.sendMessage(ChatColor.RED+"啊咧咧？ 没找到奖池呢~");
                }else {
                    Bukkit.getPluginManager().registerEvents(new SelectBoxListener(player.getUniqueId(),lottery),XgpLottery.instance);
                }
                return true;
            } else {
                sender.sendMessage("该命令只能由玩家执行");
                return false;
            }
        }
        if(args.length==1&&args[0].equals("loadall")){
            if(ConfigSetting.enableParticle)
                BoxParticle.playAllParticle();
            else
                sender.sendMessage(ChatColor.RED+"未启用粒子特效，请在config.yml中修改");
        }
        if(args.length==1&&args[0].equals("clearall")){
            BoxParticle.clearAllParticle();
        }
        if(args.length==2&&args[0].equals("papi")){
            String str = args[1];
            str = PlaceholderAPI.setPlaceholders((Player) sender,str);
            sender.sendMessage(str);
        }
        if(args.length==2&&args[0].equals("key")){
            Player player = (Player)  sender;
            String name = args[1];
            if(XgpLottery.lotteryList.containsKey(name)){
                Lottery lottery = XgpLottery.lotteryList.get(name);
                ItemStack item = player.getInventory().getItemInMainHand();
                if(item.getType()!=Material.AIR){
                    GuiItem guiItem = new GuiItem(item);
                    guiItem.setDisplayName(ChatColor.GOLD+lottery.getName()+"-抽奖箱钥匙")
                            .setLore(ChatColor.GOLD+"✦"+ChatColor.AQUA+"使用方法：手持右键抽奖箱"+ChatColor.GOLD+"✦")
                            .addEnchant();
                    player.getInventory().setItemInMainHand(guiItem.getItem());
                }else {
                    player.sendMessage("没找到手上有物品捏");
                }
            }else {
                player.sendMessage(ChatColor.RED+"啊咧咧？ 没找到奖池呢~");
            }
        }
        if(args.length==1&&args[0].equals("reload")){
            XgpLottery.reload();
            sender.sendMessage(ChatColor.GREEN+"重载成功");
        }

        return true;
    }
    private Lottery findLotteryByName(String str){
        Lottery lottery = null;
        if(XgpLottery.lotteryList.containsKey(str)){
            lottery = XgpLottery.lotteryList.get(str);
        }
        return lottery;
    }


}
