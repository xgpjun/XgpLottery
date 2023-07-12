package cn.xgp.xgplottery.Gui.Impl.Anim;

import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BoxAnimGui extends AnimHolder {
    private final Inventory inv = Bukkit.createInventory(this,3*9,ChatColor.GOLD+LangUtils.SelectItemGuiTitle);

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    static ItemStack selectGlass = new MyItem(glasses[1])
            .setDisplayName(ChatColor.GOLD+LangUtils.SelectGlass3)
            .setLore(ChatColor.GRAY+ LangUtils.SelectGlass3)
            .getItem();;

    @Override
    public BoxAnimGui loadGui() {
        for(int i =0;i<27;i++){
            inv.setItem(i,borderGlass);
            if(i==8)
                i=17;
        }

        inv.setItem(4,selectGlass);
        inv.setItem(22,selectGlass);
        return this;
    }
    public void handleClick(InventoryClickEvent e){

    }
}
