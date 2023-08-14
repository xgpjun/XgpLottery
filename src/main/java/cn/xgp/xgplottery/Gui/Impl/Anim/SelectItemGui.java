package cn.xgp.xgplottery.Gui.Impl.Anim;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl.SelectItemAnimation;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.GiveUtils;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class SelectItemGui extends AnimHolder{
    private final Inventory inv = Bukkit.createInventory(this,6*9, LangUtils.SelectItemGuiTitle);
    private final SelectItemAnimation selectItemAnimation;


    static ItemStack selectGlass = new MyItem(glasses[1])
            .setDisplayName(ChatColor.GOLD+ LangUtils.SelectGlass1)
            .setLore(ChatColor.GRAY+ LangUtils.SelectGlass2)
            .getItem();

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        for(int i =0;i<54;i++){
            inv.setItem(i,selectGlass);
        }
        return this;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        if(e.getRawSlot()>=0&&e.getRawSlot()<=53&&!selectItemAnimation.isStop()){
            Player player = (Player) e.getWhoClicked();
            selectItemAnimation.setStop(true);
            inv.setItem(e.getRawSlot(),new MyItem(selectItemAnimation.getAwards().get(0).getRecordDisplayItem()).addEnchant().getItem());
            player.playSound(player.getLocation(), LotteryAnimation.getFinish(),1.0f,1.0f);
            GiveUtils.giveAward(player,selectItemAnimation.getAwards().get(0));
            selectItemAnimation.getCalculator().sendMessage();
            Bukkit.getScheduler().runTaskLater(XgpLottery.instance,()->{
                for(int i=0;i<54;i++){
                    if(e.getRawSlot()==i)
                        continue;
                    inv.setItem(i,selectItemAnimation.getLottery().showFakeItem());
                }
            },40);

        }
    }
}
