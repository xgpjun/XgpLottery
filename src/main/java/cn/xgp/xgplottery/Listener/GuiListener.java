package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.Gui.Impl.Anim.AnimHolder;
import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryCreateGui;
import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryManageGui;
import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryMenuGui;
import cn.xgp.xgplottery.Gui.PlayerGui;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolGui;
import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolShow;
import cn.xgp.xgplottery.Gui.Impl.Pool.SpecialPoolGui;
import cn.xgp.xgplottery.Gui.Impl.Pool.SpecialPoolShow;
import cn.xgp.xgplottery.Gui.Impl.Shop.LotteryShop;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Utils.GiveUtils;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import cn.xgp.xgplottery.XgpLottery;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiListener implements Listener {

    @EventHandler
    public void clickItem(InventoryClickEvent e){
        if(e.getInventory().getHolder()==null||!(e.getInventory().getHolder() instanceof LotteryGui))
            return;
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
