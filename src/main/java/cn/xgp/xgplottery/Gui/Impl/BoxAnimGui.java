package cn.xgp.xgplottery.Gui.Impl;

import cn.xgp.xgplottery.Gui.AnimHolder;
import cn.xgp.xgplottery.Gui.GuiItem;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BoxAnimGui extends AnimHolder {
    private final Inventory inv = Bukkit.createInventory(this,3*9,"祈愿!");

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    public BoxAnimGui(Lottery lottery){

    }

    @Override
    public AnimHolder loadGui() {
        ItemStack borderGlass = new GuiItem(Material.GRAY_STAINED_GLASS_PANE)
                .setDisplayName(ChatColor.GRAY+"我也是有边界的>_<")
                .setLore(ChatColor.GRAY+ "这是分界线捏，没有别的东西了~")
                .getItem();
        for(int i =0;i<27;i++){
            inv.setItem(i,borderGlass);
            if(i==8)
                i=17;
        }
        ItemStack selectGlass = new GuiItem(Material.ORANGE_STAINED_GLASS_PANE)
                .setDisplayName(ChatColor.GOLD+"看看抽到了啥！")
                .setLore(ChatColor.GRAY+ "看看抽到了啥！")
                .getItem();
        inv.setItem(4,selectGlass);
        inv.setItem(22,selectGlass);
        return this;
    }
}
