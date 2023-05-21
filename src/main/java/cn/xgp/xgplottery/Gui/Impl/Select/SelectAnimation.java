package cn.xgp.xgplottery.Gui.Impl.Select;


import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class SelectAnimation extends LotteryGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9,"选择");
    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }
    @Override
    public LotteryGui loadGui() {
        setBorder(inv);
        int index = 0;
        for(String str: LotteryAnimation.animationList){
            inv.setItem(slot[index],new MyItem(Material.CHEST)
                    .setDisplayName(str)
                    .getItem());
            index++;
            if(index==4*7)
                break;
        }
        return this;
    }
    public void handleClick(InventoryClickEvent e){

    }
}
