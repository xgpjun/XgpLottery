package cn.xgp.xgplottery.Gui.Impl.Pool;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Gui.PlayerGui;
import cn.xgp.xgplottery.Lottery.Award;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.ConfigSetting;
import cn.xgp.xgplottery.Utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;


public class LotteryPoolShow extends PlayerGui {
    private final Inventory inv;
    private final List<Award> awards;
    Lottery lottery;

    int page;
    int size;
    PlayerGui forReturn;

    public LotteryPoolShow(Lottery lottery,List<Award> awards,PlayerGui playerGui){
        this.awards = awards;
        this.lottery = lottery;
        this.forReturn = playerGui;
        inv = Bukkit.createInventory(this,6*9, ChatColor.GREEN+LangUtils.AwardList);
        size =  (int) Math.ceil( (double)awards.size() / 45);
    }
    @Override
    public @NotNull Inventory getInventory() {
        return getPage(1);
    }
    @Override
    public LotteryGui loadGui() {
        for (int index = 45;index<=53;index++){
            inv.setItem(index,borderGlass);
        }
        inv.setItem(45,previousPage);
        inv.setItem(53,nextPage);
        return this;
    }

    public Inventory getPage(int page){
        this.page = Math.max(1, Math.min(page, size));
        inv.clear();
        loadGui();
        inv.setItem(49,new MyItem(Material.DIAMOND)
                .setDisplayName(ChatColor.GOLD+LangUtils.CurrentPage+ChatColor.AQUA+ this.page)
                .addLore(ChatColor.BLUE+LangUtils.TotalPage+ChatColor.AQUA+ this.size)
                .addLore(ChatColor.BLUE+LangUtils.PreviousInv1)
                .getItem());
        if(awards.size()==0)
            return inv;
        if(ConfigSetting.showProbability){
            DecimalFormat df = new DecimalFormat("0.00%");
            for (int i=0;i<45;i++) {
                int index = i+(this.page-1)*45;
                if(index>=awards.size())
                    break;
                Award award = awards.get(index);
                ItemStack guiItem = award.getRecordDisplayItem();
                int weight = award.getWeight();
                int sum = lottery.getWeightSum();
                String str = weight==0? "0.00%":df.format((double) weight/sum);
                inv.setItem(i, new MyItem(guiItem)
                        .addLore(ChatColor.GOLD +LangUtils.Weight+"/"+LangUtils.WeightSum+ChatColor.AQUA+":"+ChatColor.GREEN+weight +"/"+sum)
                        .addLore(ChatColor.GOLD +LangUtils.Probability+ChatColor.GREEN+str)
                        .getItem());
            }
        }else {
            for (int i=0;i<45;i++) {
                int index = i+(this.page-1)*45;
                if(index>=awards.size())
                    break;
                Award award = awards.get(index);
                ItemStack guiItem = award.getRecordDisplayItem();
                inv.setItem(i, guiItem);
            }
        }

        return inv;

    }
    public void handleClick(InventoryClickEvent e){
        if(e.isLeftClick()&&e.getRawSlot()==49){
            if (forReturn==null)
                e.getWhoClicked().closeInventory();
            else
                e.getWhoClicked().openInventory(forReturn.getInventory());
        }

        if(e.getRawSlot()==45){
            e.getWhoClicked().openInventory(getPage(this.page-1));
        }else if(e.getRawSlot()==53){
            e.getWhoClicked().openInventory(getPage(this.page+1));
        }
    }

}
