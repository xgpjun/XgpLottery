package cn.xgp.xgplottery.Gui.Impl.Anim;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import cn.xgp.xgplottery.common.FoliaLib.Wrapper.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ColorfulAnimGui extends AnimHolder{
    boolean isBegin,isSpecial;
    ItemStack award;
    List<int[]> batch = new ArrayList<>();
    private final Inventory inv = Bukkit.createInventory(this,6*9, LangUtils.SelectItemGuiTitle);
    Task taskID;
    public ColorfulAnimGui(boolean isSpecial,ItemStack award){
        this.award = award;
        this.isSpecial = isSpecial;
    }
    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        IntStream.range(0,54).forEach(i->inv.setItem(i,new MyItem(glasses[0]).setDisplayName(LangUtils.Click).setLore(LangUtils.BorderGlass4).getItem() ));
        batch.add(new int[]{0});
        batch.add(new int[]{10});
        batch.add(new int[]{11});
        batch.add(new int[]{21});
        batch.add(new int[]{22});
        batch.add(new int[]{32,33,41,42});
        batch.add(new int[]{43,44,52,53});
        batch.add(new int[]{23,24,25,26,32,33,34,35,41,42,50,51});
        batch.add(new int[]{3,4,5,6,7,8,12,13,14,15,16,17,21,22,30,31,39,40,48,49});

        return this;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        if(e.getRawSlot()<53&&e.getRawSlot()>0){
            Player player = (Player) e.getWhoClicked();
            if(isBegin)
                return;
            isBegin=true;
            startAnim(player);
//            player.playSound(player.getLocation(), LotteryAnimation.getFinish(),1.0f,1.0f);
        }
    }



    public void startAnim(Player player){
        IntStream.range(0,54).forEach(i->inv.setItem(i,null));
        taskID = XgpLottery.foliaLibAPI.getScheduler().runTaskTimer(new Runnable() {
            final ItemStack colorGlass = new MyItem(glasses[isSpecial?1:10]).setDisplayName(LangUtils.BorderGlass3).setLore(LangUtils.BorderGlass4).getItem();
            final ItemStack glass = new MyItem(glasses[3]).setDisplayName(LangUtils.BorderGlass3).setLore(LangUtils.BorderGlass4).getItem();
            int counter = 0;
            @Override
            public void run() {
                if(counter<=8){
                    for (int index:batch.get(counter)){
                        if(counter<4){
                            inv.setItem(index,glass);
                        }else{
                            inv.setItem(index,colorGlass);
                        }
                    }
                }else if(counter==9){
                    IntStream.range(0,54).forEach(i->inv.setItem(i,colorGlass));
                }
                float pitch = (float) Math.pow(2.0, counter / 10.0);
                player.playSound(player.getLocation(), LotteryAnimation.getSound(),0.5f,pitch);
                counter++;
                if(counter==11){
                    IntStream.range(0,54).forEach(i->inv.setItem(i,null));
                    inv.setItem(22,award);
                    player.playSound(player.getLocation(), LotteryAnimation.getFinish(),1.0f,1.0f);
                    cancelTask();
                }
                if(inv.getViewers().isEmpty()){
                    cancelTask();
                }
            }
        }, 10L, 15L);
    }
    void cancelTask() {
        taskID.cancel();
    }

}
