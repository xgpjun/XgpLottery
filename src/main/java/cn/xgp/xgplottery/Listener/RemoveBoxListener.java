package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.Utils.BoxParticleUtils;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.VersionAdapterUtils;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import cn.xgp.xgplottery.XgpLottery;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;
import java.util.UUID;
@Data
@AllArgsConstructor
public class RemoveBoxListener implements Listener {
    private UUID uuid;


    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerRemoveBoxEvent(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(player.getUniqueId().equals(uuid)&& VersionAdapterUtils.ifMainHand(e)){
            e.setCancelled(true);
            if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                Location location = Objects.requireNonNull(e.getClickedBlock()).getLocation();
                if(XgpLottery.lotteryBoxList.contains(XgpLottery.getLotteryBoxByLocation(location))){
                    BoxParticleUtils.removeBox(XgpLottery.getLotteryBoxByLocation(location));
                    //delete location
                    XgpLottery.locations.remove(location);
                    player.sendMessage(ChatColor.GREEN+ LangUtils.RemoveBoxSuccessfully);
                    SerializeUtils.saveData();
                }else{
                    e.getPlayer().sendMessage(ChatColor.RED+LangUtils.RemoveBoxNotFound);
                }
                HandlerList.unregisterAll(this);
            }
        }
    }
}
