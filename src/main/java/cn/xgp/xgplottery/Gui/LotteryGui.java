package cn.xgp.xgplottery.Gui;

import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.nmsUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class LotteryGui implements InventoryHolder {
    @NotNull
    @Override
    public abstract Inventory getInventory();
    public abstract LotteryGui loadGui();
    protected static int[] slot = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};
    static int[] border = {1,2,3,4,5,6,7,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53};
    protected static ItemStack borderGlass;
    protected static ItemStack exit;
    static {
        if(nmsUtils.versionToInt<13){
            Material stainedGlassPane = Material.valueOf("STAINED_GLASS_PANE");
            borderGlass = new MyItem(stainedGlassPane,1,(byte)7)
                    .setDisplayName(ChatColor.GRAY+ LangUtils.BorderGlass1)
                    .setLore(ChatColor.GRAY+ LangUtils.BorderGlass2)
                    .getItem();
        }else {
            borderGlass = new MyItem(Material.GRAY_STAINED_GLASS_PANE)
                    .setDisplayName(ChatColor.GRAY+LangUtils.BorderGlass1)
                    .setLore(ChatColor.GRAY+ LangUtils.BorderGlass2)
                    .getItem();
        }

        if(nmsUtils.versionToInt<8){
            exit = new MyItem(Material.BEDROCK)
                    .setDisplayName(ChatColor.RED+LangUtils.Exit1)
                    .setLore(ChatColor.GOLD+ LangUtils.Exit2)
                    .getItem();
        }else {
            exit = new MyItem(Material.BARRIER)
                    .setDisplayName(ChatColor.RED+LangUtils.Exit1)
                    .setLore(ChatColor.GOLD+ LangUtils.Exit2)
                    .getItem();
        }
    }

    public void setBorder(Inventory gui){

        for(int i:border){
            gui.setItem(i,borderGlass);
        }
        gui.setItem(0,new MyItem(Material.COMPASS)
                .setDisplayName(ChatColor.GREEN+LangUtils.PreviousInv1)
                .setLore(ChatColor.GOLD+ LangUtils.PreviousInv2)
                .getItem());

        gui.setItem(8,exit);
    }
    public abstract void handleClick(InventoryClickEvent e);
}
