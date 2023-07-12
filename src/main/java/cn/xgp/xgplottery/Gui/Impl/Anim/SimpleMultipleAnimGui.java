package cn.xgp.xgplottery.Gui.Impl.Anim;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.MathUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;

public class SimpleMultipleAnimGui extends AnimHolder{
    private final Inventory inv = Bukkit.createInventory(this,6*9, ChatColor.GOLD+ LangUtils.SelectItemGuiTitle);
    int index = 0;
    int taskID;

    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        IntStream.range(0,27).forEach(i-> inv.setItem(i,new MyItem(glasses[7]).setDisplayName(ChatColor.GRAY+LangUtils.BorderGlass1).setLore(ChatColor.GRAY+LangUtils.BorderGlass2).getItem()));
        IntStream.range(27,36).forEach(i-> inv.setItem(i,new MyItem(glasses[1]).setDisplayName(ChatColor.GOLD+LangUtils.BorderGlass1).setLore(ChatColor.GOLD+LangUtils.BorderGlass2).getItem()));
        IntStream.range(45,54).forEach(i-> inv.setItem(i,new MyItem(glasses[1]).setDisplayName(ChatColor.GOLD+LangUtils.BorderGlass1).setLore(ChatColor.GOLD+LangUtils.BorderGlass2).getItem()));
        inv.setItem(13,null);
        return this;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {

    }

    public void setNextItem(ItemStack item){
        if(index<9)
            inv.setItem(36+index,item);
        inv.setItem(13,item);
        index++;

        if(index==10){
            taskID = Bukkit.getScheduler().runTaskTimer(XgpLottery.instance, new Runnable() {
                int counter=0;
                @Override
                public void run() {
                    counter++;
                    ItemStack glass = new MyItem(glasses[MathUtils.getRandomInt(0,15)]).setDisplayName(ChatColor.GOLD+"恭喜！").getItem();
                    IntStream.range(0,27).forEach(i->{
                        if(i!=13){
                            inv.setItem(i,glass);
                        }
                    });
                    if(counter==20){
                        cancelTask();
                    }
                }
            },0L,5L).getTaskId();
        }
    }

    void cancelTask() {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}
