package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.Gui.Impl.LotteryMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiListener implements Listener {

    @EventHandler
    public void GuiProtect(InventoryClickEvent e){
        if(e.getInventory().getHolder()==null)
            return;
        if(e.getInventory().getHolder() instanceof LotteryMenu){
            if(e.getSlot()>=0&&e.getSlot()<=53)
                e.setCancelled(true);
        }

    }
}
