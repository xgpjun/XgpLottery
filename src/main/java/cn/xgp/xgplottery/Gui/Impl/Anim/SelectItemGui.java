package cn.xgp.xgplottery.Gui.Impl.Anim;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl.SelectItemAnimation;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.ChatUtils;
import cn.xgp.xgplottery.Utils.nmsUtils;
import cn.xgp.xgplottery.XgpLottery;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@AllArgsConstructor
public class SelectItemGui extends AnimHolder{
    private final Inventory inv = Bukkit.createInventory(this,6*9, ChatColor.GOLD+"祈愿!");
    private final SelectItemAnimation selectItemAnimation;


    static ItemStack selectGlass;

    static {

        if(nmsUtils.versionToInt<13){
            Material select = Material.valueOf("STAINED_GLASS_PANE");
            selectGlass = new MyItem(select,1,(byte)1)
                    .setDisplayName(ChatColor.GOLD+"点击翻开，获得物品")
                    .setLore(ChatColor.GRAY+ "我的回合，抽卡！")
                    .getItem();
        }else {
            selectGlass = new MyItem(Material.ORANGE_STAINED_GLASS_PANE)
                    .setDisplayName(ChatColor.GOLD+"点击翻开，获得物品")
                    .setLore(ChatColor.GRAY+ "我的回合，抽卡！")
                    .getItem();
        }
    }
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
            selectItemAnimation.stop = true;
            inv.setItem(e.getRawSlot(),new MyItem(selectItemAnimation.getAward()).addEnchant().getItem());
            player.playSound(player.getLocation(), LotteryAnimation.getFinish(),1.0f,1.0f);
            player.getInventory().addItem(selectItemAnimation.getAward());
            selectItemAnimation.getCalculator().sendMessage();

            int item = selectItemAnimation.getLottery().getAmount();
            int sp = selectItemAnimation.getLottery().getSpAmount();
            Random random = new Random();
            Bukkit.getScheduler().runTaskLater(XgpLottery.instance,()->{
                for(int i=0;i<54;i++){
                    if(e.getRawSlot()==i)
                        continue;
                    int next = random.nextInt(item+sp);
                    ItemStack itemStack = next>=item?selectItemAnimation.getLottery().getSpItems().get(next-item):selectItemAnimation.getLottery().getItems().get(next);
                    inv.setItem(i,itemStack);
                }
            },40);

        }
    }
}
