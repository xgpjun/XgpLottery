package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.Gui.Impl.Anim.AnimHolder;
import cn.xgp.xgplottery.Lottery.Award;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.Utils.GiveUtils;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.List;
import java.util.UUID;


/**
 * 具体发放奖品实现的类
 */
public class CloseListener implements Listener {
    private final ScheduledTask taskId;
    private final UUID uuid;
    private final List<Award> awards;
    private final LotteryAnimation anim;
    private final boolean isTimer;

    public CloseListener(ScheduledTask taskId, UUID uuid, LotteryAnimation anim, boolean isTimer){
        this.uuid = uuid;
        this.taskId = taskId;
        this.awards = anim.getAwards();
        this.anim = anim;
        this.isTimer = isTimer;
    }

    public CloseListener(ScheduledTask taskId,UUID uuid,LotteryAnimation anim){
        this(taskId,uuid,anim,false);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(isUniquePlayer(e.getWhoClicked())&&e.getInventory().getHolder() instanceof AnimHolder){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void dropItemEvent(PlayerDropItemEvent e){
        if(isUniquePlayer(e.getPlayer())&& e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof AnimHolder){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if(isUniquePlayer(e.getPlayer())&&e.getInventory().getHolder() instanceof AnimHolder){
            // 中止多线程
            clear();
            //给与奖品
            if(!anim.isStop()|| isTimer) {
                Player player = (Player) e.getPlayer();
                player.playSound(player.getLocation(), LotteryAnimation.getFinish(), 1.0f, 1.0f);
                GiveUtils.giveAward(player,awards);
                anim.getCalculator().sendMessage();
            }
        }
    }

    public void clear(){
        taskId.cancel();
        HandlerList.unregisterAll(this);
    }

    private boolean isUniquePlayer(HumanEntity player){
        return player.getUniqueId().equals(uuid);
    }

}
