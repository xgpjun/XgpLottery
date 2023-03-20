package cn.xgp.xgplottery.Gui;

import cn.xgp.xgplottery.Utils.GuiItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class LotteryGui implements InventoryHolder {
    @NotNull
    @Override
    public abstract Inventory getInventory();
    public abstract LotteryGui LoadGui();
    public void setBorder(Inventory gui){
        int[] border = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53};
        GuiItem borderItem = new GuiItem(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack borderGlass = borderItem.setDisplayName(ChatColor.GRAY+"我也是有边界的>_<").addLore(ChatColor.GRAY+"这是分界线捏，没有别的东西了~").getItem();
        for(int i:border){
            gui.setItem(i,borderGlass);
        }
    }
}
