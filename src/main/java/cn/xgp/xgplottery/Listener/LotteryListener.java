package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolShow;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryBox;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.VersionAdapterUtils;
import cn.xgp.xgplottery.Utils.TimesUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class LotteryListener implements Listener {


    @EventHandler
    public void playerUsePaper(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = VersionAdapterUtils.getItemInMainHand(player);
        if(VersionAdapterUtils.ifMainHand(e) &&item.getType() != Material.AIR&&item.getItemMeta()!=null&&item.getItemMeta().hasLore()){
            if(Objects.requireNonNull(item.getItemMeta().getLore()).contains(ChatColor.GOLD+"✦"+ChatColor.AQUA+ LangUtils.TicketLore)&&item.getEnchantmentLevel(Enchantment.ARROW_INFINITE)==19){
                e.setCancelled(true);
                String name = item.getItemMeta().getDisplayName();
                name = ChatColor.stripColor(name.split("-")[0]);
                Lottery lottery;
                if(XgpLottery.lotteryList.containsKey(name)){
                    lottery = XgpLottery.lotteryList.get(name);
                }else {
                    player.sendMessage(ChatColor.RED+ LangUtils.ItemWrongMsg);
                    return;
                }
                if(e.getAction().equals(Action.RIGHT_CLICK_AIR)||e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                    // 判断物品类型或其他条件
                    if(!player.isSneaking()){
                        //抽奖
                        lottery.open(player,false);
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerClickBox(PlayerInteractEvent e){
        if(VersionAdapterUtils.ifMainHand(e)&&e.getClickedBlock() != null&&XgpLottery.locations.contains(e.getClickedBlock().getLocation())){
            e.setCancelled(true);
            Location location = e.getClickedBlock().getLocation();
            Lottery lottery = LotteryBox.getLotteryByLocation(location);
            Player player = e.getPlayer();
            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                if(!player.isSneaking()&&lottery!=null){
                    ItemStack item = VersionAdapterUtils.getItemInMainHand(player);
                    if(item.getType() != Material.AIR&&item.getItemMeta()!=null&&item.getItemMeta().hasLore()){
                        if (Objects.requireNonNull(item.getItemMeta().getLore()).contains(ChatColor.GOLD+"✦"+ChatColor.AQUA+LangUtils.KeyLore)&&item.getEnchantmentLevel(Enchantment.ARROW_INFINITE)==19) {
                            String name = item.getItemMeta().getDisplayName();
                            String[] parts = name.split("-");
                            name = ChatColor.stripColor(parts[0]);
                            if(name.equals(lottery.getName())){
                                //抽奖
                                lottery.open(player,false);
                            }
                        }
                    }
                    else{
                        e.getPlayer().sendMessage(ChatColor.GOLD+LangUtils.LotteryPrefix+ChatColor.GREEN+ LangUtils.LeftClickTips);
                    }
                }
                else if(player.isSneaking()&&lottery!=null){
                    player.openInventory(new LotteryPoolShow(lottery).getInventory());
                }
            }else if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                assert lottery != null;
                sendTextBox(player,lottery);
            }
        }
    }

    public void sendTextBox(Player player, Lottery lottery) {
        String[] str = new String[4];
        str[0] = ChatColor.GOLD+LangUtils.LotteryPrefix+ChatColor.GREEN+ LangUtils.BoxInformation1;
        str[1] = ChatColor.GOLD+LangUtils.LotteryPrefix+ChatColor.GREEN+ LangUtils.BoxInformation2;
        str[2] = ChatColor.GOLD+LangUtils.LotteryPrefix+ChatColor.GREEN+ LangUtils.BoxInformation3+ ChatColor.AQUA+lottery.getName();
        if(lottery.getMaxTime()>0){
            int times = 0;
            if(TimesUtils.getCurrentLotteryTimes(player.getUniqueId(),lottery.getName())!=null)
                times = TimesUtils.getCurrentLotteryTimes(player.getUniqueId(),lottery.getName()).getTimes();

            str[3] = ChatColor.GOLD+LangUtils.LotteryPrefix+ChatColor.GREEN+ LangUtils.BoxInformation4 +ChatColor.AQUA+lottery.getMaxTime()+ChatColor.GREEN+ LangUtils.BoxInformation5 + ChatColor.AQUA+ times;
        }else
            str[3] = ChatColor.GOLD+LangUtils.LotteryPrefix+ LangUtils.BoxInformation6;
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
