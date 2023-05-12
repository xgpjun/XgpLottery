package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryBox;
import cn.xgp.xgplottery.Utils.BoxParticleUtils;
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
import org.bukkit.inventory.EquipmentSlot;

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
        if(player.getUniqueId().equals(uuid)&&e.getHand().equals(EquipmentSlot.HAND)){
            e.setCancelled(true);
            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                e.getPlayer().sendMessage("right");
                Location location = Objects.requireNonNull(e.getClickedBlock()).getLocation();

                if(XgpLottery.getLotteryBoxByLocation(location)!=null){
                    player.sendMessage("这个方块已经是"+XgpLottery.getLotteryBoxByLocation(location).getLotteryName()+"的抽奖箱了！");
                }else{
                    BoxParticleUtils.addBox(new LotteryBox(lottery.getName(),location));
                    XgpLottery.locations.add(location);
                    SerializeUtils.saveLotteryBoxData();
                }
                HandlerList.unregisterAll(this);
            }else if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                e.getPlayer().sendMessage("left");
                Location location = Objects.requireNonNull(e.getClickedBlock()).getLocation();
                if(XgpLottery.lotteryBoxList.contains(XgpLottery.getLotteryBoxByLocation(location))){
                    BoxParticleUtils.removeBox(XgpLottery.getLotteryBoxByLocation(location));
                    //delete location
                    XgpLottery.locations.remove(location);
                }else{
                    e.getPlayer().sendMessage(ChatColor.RED+"这个方块好像并不是抽奖箱");
                }
                HandlerList.unregisterAll(this);
            }else {
                e.getPlayer().sendMessage(ChatColor.GOLD +"右键标记方块，左键删除方块！");
            }
        }
    }
}
