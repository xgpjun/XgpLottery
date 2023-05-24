package cn.xgp.xgplottery.Gui.Impl.Show;

import cn.xgp.xgplottery.Gui.LotteryGui;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class ShowItemGUI extends LotteryGui {
    private final Inventory inv = Bukkit.createInventory(this,3*9, ChatColor.GOLD+"他获得的奖品");

    private final ItemStack award;


    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        for(int i=0;i<27;i++){
            inv.setItem(i,borderGlass);
        }
        inv.setItem(13,award);
        return this;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {

    }
}
