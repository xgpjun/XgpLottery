package cn.xgp.xgplottery.Gui.Impl.Anim;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MarqueeAnimGui extends AnimHolder{
    private final Inventory inv = Bukkit.createInventory(this,6*9, ChatColor.GOLD+ LangUtils.SelectItemGuiTitle);

    private ItemStack temp;

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        return null;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {

    }

    public void nexStep(int index){
        if(temp != null){
            if(index==0){
                inv.setItem(53,temp);
            }else {
                inv.setItem(index-1,temp);
            }
        }
        temp = inv.getItem(index);
        if (temp != null) {
            inv.setItem(index,new MyItem(glasses[5]).setDisplayName(ChatColor.GREEN+String.valueOf(ChatColor.BOLD)+"选择中").getItem());
        }
    }
}
