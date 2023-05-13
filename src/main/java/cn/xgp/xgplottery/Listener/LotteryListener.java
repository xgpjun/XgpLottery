package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolShow;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryBox;
import cn.xgp.xgplottery.Lottery.LotteryTimes;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class LotteryListener implements Listener {
    @EventHandler
    public void playerUsePaper(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(e.getHand()== EquipmentSlot.HAND&&item.getType() != Material.AIR&&item.getItemMeta()!=null&&item.getItemMeta().hasLore()){
            if(item.getItemMeta().getLore().contains(ChatColor.GOLD+"✦"+ChatColor.AQUA+ "右键以抽奖"+ChatColor.GOLD+"✦")&&item.getEnchantmentLevel(Enchantment.ARROW_INFINITE)==19){
                e.setCancelled(true);
                String name = item.getItemMeta().getDisplayName();
                name = ChatColor.stripColor(name.split("-")[0]);
                Lottery lottery;
                if(XgpLottery.lotteryList.containsKey(name)){
                    lottery = XgpLottery.lotteryList.get(name);
                }else {
                    player.sendMessage(ChatColor.RED+"物品出错了，请联系管理员");
                    return;
                }
                if(e.getAction().equals(Action.RIGHT_CLICK_AIR)||e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                    // 判断物品类型或其他条件
                    if(!player.isSneaking()){
                        //抽奖
                        if(lottery.getWeightSum()<=0){
                            player.sendMessage(ChatColor.RED+"奖池现在还是空的！");
                            return;
                        }else{
                            lottery.open(player,false);
                        }
                    }else {
                        //显示奖池
                        player.openInventory(new LotteryPoolShow(lottery).getInventory());
                    }
                }
                if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)||e.getAction().equals(Action.LEFT_CLICK_AIR) ){
                    sendTextBox(player,lottery);
                }
            }
        }



    }

    @EventHandler
    public void playerClickBox(PlayerInteractEvent e){
        if(e.getHand()== EquipmentSlot.HAND&&e.getClickedBlock() != null&&XgpLottery.locations.contains(e.getClickedBlock().getLocation())){
            e.setCancelled(true);
            Location location = e.getClickedBlock().getLocation();
            Lottery lottery = LotteryBox.getLotteryByLocation(location);
            Player player = e.getPlayer();
            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                if(!player.isSneaking()&&lottery!=null){
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if(item.getType() != Material.AIR&&item.getItemMeta()!=null&&item.getItemMeta().hasLore()){
                        if (item.getItemMeta().getLore().contains(ChatColor.GOLD+"✦"+ChatColor.AQUA+"使用方法：手持右键抽奖箱"+ChatColor.GOLD+"✦")&&item.getEnchantmentLevel(Enchantment.ARROW_INFINITE)==19) {
                            String name = item.getItemMeta().getDisplayName();
                            String[] parts = name.split("-");
                            name = ChatColor.stripColor(parts[0]);
                            if(name.equals(lottery.getName())){
                                //抽奖
                                if(lottery.getWeightSum()<=0){
                                    player.sendMessage(ChatColor.RED+"奖池现在还是空的！");
                                }else {
                                    lottery.open(player,false);
                                }
                            }else {
                                player.sendMessage(ChatColor.RED+"这个好像不是该宝箱的钥匙~");
                            }
                        }
                    }
                    else{
                        e.getPlayer().sendMessage(ChatColor.GOLD+"[温馨提示]"+ChatColor.GREEN+ "左键查看奖池信息");
                    }
                }
                else if(player.isSneaking()&&lottery!=null){
                    player.openInventory(new LotteryPoolShow(lottery).getInventory());
                }
            }else if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                sendTextBox(player,lottery);
                player.sendMessage();
            }
        }
    }

    public void sendTextBox(Player player, Lottery lottery) {
        String[] str = new String[4];
        str[0] = ChatColor.GOLD+"[温馨提示]"+ChatColor.GREEN+ "请手持钥匙右键打开";
        str[1] = ChatColor.GOLD+"[温馨提示]"+ChatColor.GREEN+"shift+右键打开奖池预览";
        str[2] = ChatColor.GOLD+"[奖池信息]"+ChatColor.GREEN+"奖池名称："+ChatColor.AQUA+lottery.getName();
        if(lottery.getMaxTime()>0){
            str[3] = ChatColor.GOLD+"[奖池信息]"+ChatColor.GREEN+"奖池保底数："+ChatColor.AQUA+lottery.getMaxTime()+ChatColor.GREEN+"您当前未保底抽数："+ ChatColor.AQUA+ LotteryTimes.getCurrentLotteryTimes(player.getUniqueId(),lottery.getName()).getTimes();
        }else
            str[3] = ChatColor.GOLD+"[奖池信息] 当前奖池未开启保底机制";
        int maxLength = 0;
        for (String line : str) {
            if (line.length() > maxLength) {
                maxLength = line.length();
            }
        }

        StringBuilder horizontalLine = new StringBuilder();
        for (int i = 0; i < maxLength + 4; i++) {
            horizontalLine.append("-");
        }
        player.sendMessage(" " + ChatColor.DARK_PURPLE + horizontalLine);

        for (String line : str) {
            String formattedLine = ChatColor.DARK_PURPLE + "| " + ChatColor.RESET + line;
            player.sendMessage(formattedLine);
        }

        player.sendMessage(" " + ChatColor.DARK_PURPLE + horizontalLine);
    }


}
