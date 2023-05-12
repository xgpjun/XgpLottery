package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolShow;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryBox;
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
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR)||e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            Player player = e.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            // 判断物品类型或其他条件
            if(e.getHand()== EquipmentSlot.HAND&&item.getType() != Material.AIR&&item.getItemMeta()!=null&&item.getItemMeta().hasLore()){
                if (item.getItemMeta().getLore().contains(ChatColor.AQUA+ "右键以抽奖")&&item.getEnchantmentLevel(Enchantment.ARROW_INFINITE)==19) {
                    e.setCancelled(true);
                    String name = item.getItemMeta().getDisplayName();
                    name = ChatColor.stripColor(name.split("-")[0]);
                    if(XgpLottery.lotteryList.containsKey(name)){
                        Lottery lottery = XgpLottery.lotteryList.get(name);
                        if(!player.isSneaking()){
                            //抽奖
                            if(lottery.getWeightSum()<=0){
                                player.sendMessage(ChatColor.RED+"奖池现在还是空的！");
                            }else{
                                lottery.getAnimation(player,lottery).playAnimation();
                            }

                        }else {
                            //显示奖池
                            player.openInventory(new LotteryPoolShow(lottery).getInventory());
                        }
                    }else {
                        player.sendMessage(ChatColor.RED+"物品出错了，请联系管理员");
                    }

                }
            }
        }

    }

    @EventHandler
    public void playerClickBox(PlayerInteractEvent e){
        if(e.getHand()== EquipmentSlot.HAND&&e.getClickedBlock() != null&&XgpLottery.locations.contains(e.getClickedBlock().getLocation())){
            e.setCancelled(true);
            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                Location location = e.getClickedBlock().getLocation();
                Lottery lottery = LotteryBox.getLotteryByLocation(location);
                Player player = e.getPlayer();
                if(!player.isSneaking()&&lottery!=null){
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if(item.getType() != Material.AIR&&item.getItemMeta()!=null&&item.getItemMeta().hasLore()){
                        if (item.getItemMeta().getLore().contains(ChatColor.GOLD+"✦"+ChatColor.AQUA+"手持右键以开启抽奖箱"+ChatColor.GOLD+"✦")&&item.getEnchantmentLevel(Enchantment.ARROW_INFINITE)==19) {
                            String name = item.getItemMeta().getDisplayName();
                            String[] parts = name.split("-");
                            name = ChatColor.stripColor(parts[0]);
                            if(name.equals(lottery.getName())){
                                //抽奖
                                if(lottery.getWeightSum()<=0){
                                    player.sendMessage(ChatColor.RED+"奖池现在还是空的！");
                                }else {
                                    lottery.getAnimation(player,lottery).playAnimation();
                                }
                            }else {
                                player.sendMessage(ChatColor.RED+"这个好像不是该宝箱的钥匙~");
                            }
                        }
                    }
                    else{
                        e.getPlayer().sendMessage(ChatColor.GOLD+"[温馨提示]"+ChatColor.GREEN+ "请手持钥匙右键打开\n"+ChatColor.GOLD+"[温馨提示]"+ChatColor.GREEN+"你手中的物品好像不对捏");
                    }
                }
                else if(player.isSneaking()&&lottery!=null){
                    player.openInventory(new LotteryPoolShow(lottery).getInventory());
                }
            }else if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                Player player = e.getPlayer();
                player.sendMessage(ChatColor.GOLD+"[温馨提示]"+ChatColor.GREEN+ "请手持钥匙右键打开\n"+ChatColor.GOLD+"[温馨提示]"+ChatColor.GREEN+"shift+右键打开奖池预览");
            }
        }
    }

}
