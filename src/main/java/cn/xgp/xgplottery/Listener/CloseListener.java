package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.Gui.AnimHolder;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl.BoxAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CloseListener implements Listener {
    private final int taskId;
    private final UUID uuid;
    private final ItemStack award;
    private final BoxAnimation boxAnimation;




    public CloseListener(int taskId,UUID uuid,ItemStack award,BoxAnimation boxAnimation){
        this.uuid = uuid;
        this.taskId = taskId;
        this.award = new ItemStack(award);
        this.boxAnimation = boxAnimation;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(isUniquePlayer(e.getWhoClicked())&&e.getInventory().getHolder() instanceof AnimHolder){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void dropItemEvent(PlayerDropItemEvent e){
        if(isUniquePlayer(e.getPlayer())&& e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof BoxAnimation){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if(!boxAnimation.stop&&isUniquePlayer(e.getPlayer())&&e.getInventory().getHolder() instanceof AnimHolder){
            // 中止多线程
            clear();
            //给与奖品
            Player player = (Player) e.getPlayer();
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1.0f,1.0f);
            player.getInventory().addItem(award);
        }
    }
    public void clear(){
        Bukkit.getScheduler().cancelTask(taskId);
        HandlerList.unregisterAll(this);
    }

    private boolean isUniquePlayer(HumanEntity player){
        return player.getUniqueId().equals(uuid);
    }

}
