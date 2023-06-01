package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryBox;
import cn.xgp.xgplottery.Utils.BoxParticleUtils;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.VersionAdapterUtils;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import cn.xgp.xgplottery.XgpLottery;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.milkbowl.vault.chat.Chat;
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
public class SelectBoxListener implements Listener {

    private UUID uuid;
    private Lottery lottery;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerSelectBoxEvent(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(player.getUniqueId().equals(uuid)&& VersionAdapterUtils.ifMainHand(e)){
            e.setCancelled(true);
            if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                Location location = Objects.requireNonNull(e.getClickedBlock()).getLocation();
                if(XgpLottery.getLotteryBoxByLocation(location)!=null){
                    player.sendMessage(ChatColor.RED+ LangUtils.SetBoxRepeatedly.replace("%lotteryName%",Objects.requireNonNull(XgpLottery.getLotteryBoxByLocation(location)).getLotteryName()));
                }else{
                    BoxParticleUtils.addBox(new LotteryBox(lottery.getName(),location));
                    player.sendMessage(ChatColor.GREEN+LangUtils.SetBoxSuccessful);
                    XgpLottery.locations.add(location);
                    SerializeUtils.saveData();
                }
                HandlerList.unregisterAll(this);
            }
        }
    }
}
