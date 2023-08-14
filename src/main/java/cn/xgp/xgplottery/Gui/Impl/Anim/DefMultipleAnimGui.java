package cn.xgp.xgplottery.Gui.Impl.Anim;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Award;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DefMultipleAnimGui extends AnimHolder{

    private final Inventory inv = Bukkit.createInventory(this,6*9, LangUtils.SelectItemGuiTitle2);

    int[] borderSlot = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,25,26,27,28,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53};
    int[] awardSlot = {20,21,22,23,24,29,30,31,32,33};
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

    public void borderChange(){
        for (int i : borderSlot) {
            int index = MathUtils.getRandomInt(1,15);
            inv.setItem(i,addInfo(glasses[index]));
        }
    }

    public void setAward(int index, Award award){
        inv.setItem(awardSlot[index],award.getRecordDisplayItem());
    }
}
