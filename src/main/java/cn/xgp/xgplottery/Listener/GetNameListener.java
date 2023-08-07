package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.XgpLottery;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GetNameListener implements Listener {
    private final UUID uuid;
    private final CompletableFuture<String> future;
    private final ScheduledTask timeoutTask;

    public GetNameListener(Player player, CompletableFuture<String> future, long time) {
        this.uuid = player.getUniqueId();
        this.future = future;
        timeoutTask = player.getScheduler().runDelayed(XgpLottery.instance, task->{
            HandlerList.unregisterAll(this);
            future.complete("cancel");
        },null,time*20);
    }

    @EventHandler
    public void getString(AsyncPlayerChatEvent e){
        if(e.getPlayer().getUniqueId().equals(uuid)){
            e.setCancelled(true);
            timeoutTask.cancel();
            future.complete(e.getMessage());
            HandlerList.unregisterAll(this);
        }
    }
}