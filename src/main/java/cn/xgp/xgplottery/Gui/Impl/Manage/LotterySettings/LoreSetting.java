package cn.xgp.xgplottery.Gui.Impl.Manage.LotterySettings;

import cn.xgp.xgplottery.Gui.Impl.Manage.LotterySetting;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.ReceiveUtils;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
public class LoreSetting extends LotteryGui {
    private final Inventory inv = Bukkit.createInventory(this,3*9, LangUtils.LoreSetting1);

    @Getter
    private List<String> loreList;
    private final LotterySetting lotterySetting;
    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        for(int i:smallBorder){
            inv.setItem(i,borderGlass);
        }
        inv.setItem(0,new MyItem(Material.COMPASS)
                .setDisplayName(ChatColor.GREEN+ LangUtils.PreviousInv1)
                .setLore(ChatColor.GOLD+ LangUtils.PreviousInv2)
                .getItem());
        inv.setItem(8,exit);
        inv.setItem(11,new MyItem(Material.PAPER)
                .setDisplayName(LangUtils.LoreSetting2)
                .setLore(loreList)
                .getItem());
        inv.setItem(13,new MyItem(Material.PAPER)
                .setDisplayName(LangUtils.LoreSetting3)
                .addLore(LangUtils.Operation+LangUtils.LeftClick)
                .getItem());
        inv.setItem(15,new MyItem(Material.PAPER)
                .setDisplayName(LangUtils.LoreSetting4)
                .addLore(LangUtils.Operation+LangUtils.LeftClick)
                .getItem());
        return null;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        int slot = e.getRawSlot();
        switch (slot){
            case 0: player.openInventory(lotterySetting.getInventory());break;
            case 8: player.closeInventory(); break;
            case 13: ReceiveUtils.addLore(player,this);SerializeUtils.saveLotteryData(); break;
            case 15: ReceiveUtils.delLore(player,this);SerializeUtils.saveLotteryData(); break;
        }
    }
}
