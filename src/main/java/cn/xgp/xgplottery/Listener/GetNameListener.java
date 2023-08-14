package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.XgpLottery;
import cn.xgp.xgplottery.common.FoliaLib.Wrapper.Task;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GetNameListener implements Listener {
    private final UUID uuid;
    private final CompletableFuture<String> future;
    private final Task timeoutTask;

    public GetNameListener(UUID uuid, CompletableFuture<String> future,long time) {
        this.uuid = uuid;
        this.future = future;
        this.timeoutTask = XgpLottery.foliaLibAPI.getScheduler().runTaskLater(()->{
            HandlerList.unregisterAll(this);
            future.complete("cancel");
        },time*20);
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