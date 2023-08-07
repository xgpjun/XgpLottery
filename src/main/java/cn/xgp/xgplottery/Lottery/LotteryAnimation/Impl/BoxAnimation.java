package cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl;

import cn.xgp.xgplottery.Gui.Impl.Anim.BoxAnimGui;
import cn.xgp.xgplottery.Listener.CloseListener;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class BoxAnimation extends LotteryAnimation {

    public BoxAnimation(Player player,Lottery lottery){
        super(player,lottery);
    }

    @Override
    public String toLore(){
        return LangUtils.ScrollingAnimation;
    }

    @Override
    public void playAnimation() {
        awards.add(getOneAward());

        Inventory inventory = new BoxAnimGui().loadGui().getInventory();
        List<ItemStack> showItemList = new ArrayList<>();
        initItemList(showItemList, lottery);
        //get award
        showItemList.set(20,awards.get(0).getRecordDisplayItem());
        player.openInventory(inventory);
        taskID = Bukkit.getAsyncScheduler().runAtFixedRate(XgpLottery.instance, new Consumer<ScheduledTask>() {
            int j = 0;
            @Override
            public void accept(ScheduledTask scheduledTask) {
                for (int i = 9; i < 18; i++) {
                    MyItem myItem = new MyItem(showItemList.get(i - 9 + j));
                    if (i == 13)
                        inventory.setItem(i, myItem.addEnchant().getItem());
                    else
                        inventory.setItem(i, myItem.getItem());
                }
                j++;
                if (j >= showItemList.size() - 9) {
                    j = 0;
                }
                if (j == 17) { // 中奖物品的位置是第 17   21-4
                    setStop(true);
                }
                float pitch = (float) Math.pow(2.0, j / 18.0);
                player.playSound(player.getLocation(), sound, 1.0f, pitch);
                if (isStop()) {
                    cancelTask();
                    player.playSound(player.getLocation(), finish, 1.0f, 1.0f);
                }
            }
        },0L,250L, TimeUnit.MILLISECONDS);
        CloseListener closeListener = new CloseListener(taskID,player.getUniqueId(),this,true);
        Bukkit.getPluginManager().registerEvents(closeListener,XgpLottery.instance);
    }



    private void initItemList(List<ItemStack> showItemList,Lottery lottery){
        Random random = new Random();
        int item = lottery.getAmount();
        int sp = lottery.getSpAmount();
        for (int i = 0; i <= 40; i++) {
            int next = random.nextInt(item+sp);
            ItemStack itemStack = next>=item?lottery.getSpAwards().get(next-item).getRecordDisplayItem().clone():lottery.getAwards().get(next).getRecordDisplayItem().clone();
            showItemList.add(itemStack);
        }
    }
}
