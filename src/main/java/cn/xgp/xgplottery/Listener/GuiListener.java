package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Gui.PlayerGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class GuiListener implements Listener {

    @EventHandler
    public void clickItem(InventoryClickEvent e){
        if(e.getInventory().getHolder()==null||!(e.getInventory().getHolder() instanceof LotteryGui))
            return;
        if(e.isShiftClick())
            e.setCancelled(true);
        if((e.getRawSlot()>=0&&e.getRawSlot()<=53)||e.getInventory().getHolder() instanceof PlayerGui)
            e.setCancelled(true);
        ((LotteryGui) e.getInventory().getHolder()).handleClick(e);
    }

    @EventHandler
    public void dragItem(InventoryDragEvent e){
        if(e.getInventory().getHolder() == null)
            return;
        if(e.getInventory().getHolder() instanceof LotteryGui){
            e.setCancelled(true);
        }
    }
}
