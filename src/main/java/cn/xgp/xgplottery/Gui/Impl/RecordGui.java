package cn.xgp.xgplottery.Gui.Impl;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Gui.PlayerGui;
import cn.xgp.xgplottery.Lottery.LotteryRecord;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RecordGui extends PlayerGui {
    int size;
    int page;
    Inventory inv;
    String lotteryName;
    List<ItemStack> itemList;

    public RecordGui(Player player,String lotteryName){
        this.lotteryName = lotteryName;
        itemList = new LotteryRecord(player.getUniqueId(),lotteryName).getRecord();
        Collections.reverse(itemList);
        inv = Bukkit.createInventory(this,6*9, ChatColor.AQUA+ player.getName()+ChatColor.GOLD+ LangUtils.RecordTitle.replace("%lotteryName%",ChatColor.AQUA+ lotteryName+ChatColor.GOLD));
        size =  (int) Math.ceil( (double)itemList.size() / 45);

    }
    public RecordGui(OfflinePlayer player, String lotteryName){
        this.lotteryName = lotteryName;
        itemList = new LotteryRecord(player.getUniqueId(),lotteryName).getRecord();
        Collections.reverse(itemList);
        inv = Bukkit.createInventory(this,6*9, ChatColor.AQUA+ player.getName()+ChatColor.GOLD+ LangUtils.RecordTitle.replace("%lotteryName%",ChatColor.AQUA+ lotteryName+ChatColor.GOLD));
        size =  (int) Math.ceil( (double)itemList.size() / 45);
    }

    @Override
    public @NotNull Inventory getInventory() {
        if(itemList.size()==0)
            inv = Bukkit.createInventory(this,6*9, ChatColor.AQUA+ LangUtils.RecordNotFound);
        return getPage(1);
    }

    @Override
    public LotteryGui loadGui() {
        for (int index = 46;index<=52;index++){
            inv.setItem(index,borderGlass);
        }
        inv.setItem(45,new MyItem(Material.PAPER).setDisplayName(ChatColor.GREEN+LangUtils.PreviousPage).getItem());
        inv.setItem(53,new MyItem(Material.PAPER).setDisplayName(ChatColor.GREEN+LangUtils.NextPage).getItem());
        return this;
    }

    public Inventory getPage(int page){
        this.page = Math.max(1, Math.min(page, size));
        inv.clear();
        loadGui();
        inv.setItem(49,new MyItem(Material.DIAMOND).setDisplayName(ChatColor.GREEN+LangUtils.CurrentPage+ChatColor.AQUA+ this.page).addLore(ChatColor.GREEN+LangUtils.TotalPage+ChatColor.AQUA+ this.size).getItem());

        if(itemList.size()==0)
            return inv;

        for (int i=0;i<45;i++){
            int index = i+(this.page-1)*45;
            if(index>=itemList.size())
                break;
            inv.setItem(i,itemList.get(index));
        }
        return inv;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        if(e.getRawSlot()==45){
            e.getWhoClicked().openInventory(getPage(this.page-1));
        }else if(e.getRawSlot()==53){
            e.getWhoClicked().openInventory(getPage(this.page+1));
        }
    }
}
