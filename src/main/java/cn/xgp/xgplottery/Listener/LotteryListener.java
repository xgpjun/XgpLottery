package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.Gui.Impl.PlayerLotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryBox;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.NMSUtils;
import cn.xgp.xgplottery.Utils.TimesUtils;
import cn.xgp.xgplottery.Utils.VersionAdapterUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class LotteryListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerUsePaper(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = VersionAdapterUtils.getItemInMainHand(player);
        if(VersionAdapterUtils.ifMainHand(e) &&item.getType() != Material.AIR&&item.getItemMeta()!=null&&item.getItemMeta().hasLore()){
            String lotteryName = NMSUtils.getLotteryFromTag(item);
            if(lotteryName==null)
                return;
            e.setCancelled(true);
            Lottery lottery = XgpLottery.lotteryList.get(lotteryName);

            if(e.getAction().equals(Action.RIGHT_CLICK_AIR)||e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){

                if ("VoidAnimation".equals(lottery.getAnimation())) {
                    if (lottery.isCheckFull() && VersionAdapterUtils.getPlayerEmptySlot(player) < 1) {
                        player.sendMessage(LangUtils.PlayerGui5);
                        return;
                    }
                    if (NMSUtils.checkTag(item, false, lottery.getName())) {
                        lottery.open(player, false, false);
                        return;
                    }
                    return;
                }
                // 判断物品类型或其他条件
                if (!player.isSneaking()) {
                    //抽奖界面
                    player.openInventory(new PlayerLotteryGui(player, lottery, false).getInventory());
                } else {
                    sendTextBox(player, lottery);
                }
            }
            if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)||e.getAction().equals(Action.LEFT_CLICK_AIR) ){
                sendTextBox(player,lottery);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
    public void playerClickBox(PlayerInteractEvent e){
        if(VersionAdapterUtils.ifMainHand(e)&&e.getClickedBlock() != null&&XgpLottery.locations.contains(e.getClickedBlock().getLocation())){
            e.setCancelled(true);
            Location location = e.getClickedBlock().getLocation();
            Lottery lottery = LotteryBox.getLotteryByLocation(location);
            Player player = e.getPlayer();
            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                if(!player.isSneaking()&&lottery!=null) {
                    if ("VoidAnimation".equals(lottery.getAnimation())) {
                        if (lottery.isCheckFull() && VersionAdapterUtils.getPlayerEmptySlot(player) < 1) {
                            player.sendMessage(LangUtils.PlayerGui5);
                            return;
                        }
                        ItemStack item = VersionAdapterUtils.getItemInMainHand(player);
                        if (NMSUtils.checkTag(item, true, lottery.getName())) {
                            lottery.open(player, false, false);
                            return;
                        }
                        return;

                    }
                    player.openInventory(new PlayerLotteryGui(player, lottery, true).getInventory());
                }
            }else if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                assert lottery != null;
                sendTextBox(player,lottery);
            }
        }
    }

    @EventHandler
    public void playerPlaceBlock(BlockPlaceEvent e){
        ItemStack item = VersionAdapterUtils.getItemInMainHand(e.getPlayer());
        if(NMSUtils.checkTag(item)){
            e.setCancelled(true);
        }
    }

    public void sendTextBox(Player player, Lottery lottery) {
        String[] str = new String[3];
        str[0] = ChatColor.GOLD+LangUtils.LotteryPrefix+ChatColor.GREEN+ LangUtils.BoxInformation1;
        str[1] = ChatColor.GOLD+LangUtils.LotteryPrefix+ChatColor.GREEN+ LangUtils.BoxInformation3+ ChatColor.AQUA+lottery.getName();
        if(lottery.getMaxTime()>0){
            int times = 0;
            if(TimesUtils.getCurrentLotteryTimes(player.getUniqueId(),lottery.getName())!=null)
                times = TimesUtils.getCurrentLotteryTimes(player.getUniqueId(),lottery.getName()).getTimes();

            str[2] = ChatColor.GOLD+LangUtils.LotteryPrefix+ChatColor.GREEN+ LangUtils.BoxInformation4 +ChatColor.AQUA+lottery.getMaxTime()+ChatColor.GREEN+ LangUtils.BoxInformation5 + ChatColor.AQUA+ times;
        }else
            str[2] = ChatColor.GOLD+LangUtils.LotteryPrefix+ LangUtils.BoxInformation6;
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
        player.sendMessage(" " + ChatColor.BOLD+ ChatColor.GOLD + horizontalLine);

        for (String line : str) {
            String formattedLine = String.valueOf(ChatColor.BOLD)+ ChatColor.AQUA + "| " + ChatColor.RESET + line;
            player.sendMessage(formattedLine);
        }

        player.sendMessage(" " +ChatColor.BOLD+  ChatColor.GOLD + horizontalLine);
    }


}
