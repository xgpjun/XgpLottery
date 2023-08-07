package cn.xgp.xgplottery.Gui;

import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
    protected static int[] border = {1,2,3,4,5,6,7,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53};

    protected static int[] smallBorder = {0,1,2,3,4,5,6,7,8,9,17,18,19,20,21,22,23,24,25,26};
    protected static ItemStack borderGlass;
    protected static ItemStack exit;
    protected static Material command;
    protected static Material writable_book;

    protected static ItemStack nextPage = new MyItem(Material.PAPER).setDisplayName(ChatColor.GREEN+LangUtils.NextPage).getItem();
    protected static ItemStack previousPage = new MyItem(Material.PAPER).setDisplayName(ChatColor.GREEN+LangUtils.PreviousPage).getItem();


    static {
        borderGlass = new MyItem(Material.GRAY_STAINED_GLASS_PANE)
                .setDisplayName(ChatColor.GRAY+LangUtils.BorderGlass1)
                .setLore(ChatColor.GRAY+ LangUtils.BorderGlass2)
                .getItem();
        exit = new MyItem(Material.BARRIER)
                    .setDisplayName(ChatColor.RED+LangUtils.Exit1)
                    .setLore(ChatColor.GOLD+ LangUtils.Exit2)
                    .getItem();
        command = Material.COMMAND_BLOCK;
        writable_book = Material.WRITABLE_BOOK;

    }

    public void setBorder(Inventory gui){

        for (int i : border) {
            gui.setItem(i, borderGlass);
        }
        gui.setItem(0, new MyItem(Material.COMPASS)
                .setDisplayName(ChatColor.GREEN + LangUtils.PreviousInv1)
                .setLore(ChatColor.GOLD + LangUtils.PreviousInv2)
                .getItem());

        gui.setItem(8, exit);
    }

    public int findSlot(int tar) {
        for (int i = 0; i < slot.length; i++) {
            if (slot[i] == tar)
                return i;
        }
        return -1;
    }

    public boolean takeValue(Lottery lottery, Player player, int cost) {
        switch (lottery.getSellType()) {
            case POINTS: {
                return XgpLottery.ppAPI.take(player.getUniqueId(), cost);
            }
            case MONEY: {
                boolean hasMoney = XgpLottery.eco.has(player, cost);
                if (hasMoney) {
                    XgpLottery.eco.withdrawPlayer(player, cost);
                }
                return hasMoney;
            }
            case EXP: {
                int rawLevel = player.getLevel();
                if (rawLevel < cost)
                    return false;
                player.setLevel(rawLevel - cost);
                return true;
            }
        }
        return false;
    }

    public abstract void handleClick(InventoryClickEvent e);
}
