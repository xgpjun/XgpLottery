package cn.xgp.xgplottery.Gui.Impl.Anim;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Award;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl.MultipleSelectItemAnimation;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.MathUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
public class MultipleSelectItemGui extends AnimHolder{
    private final Inventory inv = Bukkit.createInventory(this,6*9, LangUtils.SelectItemGuiTitle2);
    private final MultipleSelectItemAnimation animation;
    private int chosenCount = 0;
    private final List<Integer> chosenSlot = new ArrayList<>();

    public MultipleSelectItemGui(MultipleSelectItemAnimation animation){
        this.animation = animation;
    }
    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {

        return this;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        int slot = e.getRawSlot();
        if(animation.isStop()&&slot>=0&&slot<=53){
            if(chosenCount>=10||chosenSlot.contains(slot)){
                return;
            }
            Player player = (Player) e.getWhoClicked();
            setAward(slot,animation.getAwards().get(chosenCount));
            chosenCount++;
            player.playSound(player.getLocation(), LotteryAnimation.getFinish(),1.0f,1.0f);
            chosenSlot.add(slot);
            if(chosenCount==10){
                Bukkit.getScheduler().runTaskLater(XgpLottery.instance, this::setRandomAward,40);
            }
        }
    }

    public void setAward(int index, Award award){
        inv.setItem(index,award.getRecordDisplayItem());
    }

    void setRandomAward(){
        IntStream.range(0,54).forEach(i->{
            if(!chosenSlot.contains(i)){
               inv.setItem(i,animation.getLottery().showFakeItem());
            }
        });
    }

    public void glassChange(){
        IntStream.range(0,54).forEach(i->{
            int index = MathUtils.getRandomInt(1,15);
            inv.setItem(i,addInfo(glasses[index]));
        });
    }

    private ItemStack addInfo(ItemStack glass){
        return new MyItem(glass).setDisplayName(ChatColor.GOLD+ LangUtils.SelectGlass1)
                .setLore(ChatColor.GRAY+ LangUtils.SelectGlass2)
                .getItem();
    }
}
