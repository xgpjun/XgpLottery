package cn.xgp.xgplottery.Gui.Impl.Anim;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Award;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BoxMultipleAnimGui extends AnimHolder{
    private final Inventory inv = Bukkit.createInventory(this,6*9, ChatColor.GOLD+ LangUtils.SelectItemGuiTitle);

    int[] borderSlot = {0,1,2,3,4,5,6,7,8,9,17,18,19,25,26,27,28,34,35,36,37,43,44,45,46,47,48,49,50,51,52,53};
    int[] awardSlot = {29,30,31,32,33,38,39,40,41,42};
    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        for (int i : awardSlot) {
            inv.setItem(i, new MyItem(glasses[0])
                    .setDisplayName(LangUtils.AwardItemName)
                    .setLore(LangUtils.AwardItemLore)
                    .addEnchant()
                    .getItem());
        }
        for (int i : borderSlot) {
            inv.setItem(i,addInfo(glasses[7]));
        }
        inv.setItem(4,glasses[1]);
        inv.setItem(22,glasses[1]);
        return this;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {

    }

    private ItemStack addInfo(ItemStack glass){
        return new MyItem(glass).setDisplayName(LangUtils.BorderGlass3)
                .setLore(LangUtils.BorderGlass4)
                .getItem();
    }

    public void setAward(int index, Award award){
        inv.setItem(awardSlot[index],award.getRecordDisplayItem());
    }
}
