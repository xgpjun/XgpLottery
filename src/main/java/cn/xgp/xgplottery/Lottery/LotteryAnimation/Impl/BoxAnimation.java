package cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl;

import cn.xgp.xgplottery.Gui.MyItem;
import cn.xgp.xgplottery.Gui.Impl.Anim.BoxAnimGui;
import cn.xgp.xgplottery.Listener.CloseListener;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoxAnimation extends LotteryAnimation {
    private final Player player;
    private final Lottery lottery;
    public boolean stop = false;
    private final boolean isCommand;

    public BoxAnimation(Player player,Lottery lottery){
        this(player,lottery,false);
    }

    public BoxAnimation(Player player,Lottery lottery,Boolean isCommand){
        this.player = player;
        this.lottery = lottery;
        this.isCommand = isCommand;
    }

    @Override
    public String getAnimationType() {
        return "BoxAnimation";
    }
    public String toLore(){
        return "物品滚动动画";
    }

    @Override
    public void playAnimation() {


        Inventory inventory = new BoxAnimGui().loadGui().getInventory();
        List<ItemStack> showItemList = new ArrayList<>();
        initItemList(showItemList, lottery);
        //get award
        ItemStack award = lottery.getCalculator().getAward(lottery,player);

        showItemList.set(20,award);
        player.openInventory(inventory);
        if(!isCommand){
            ItemStack item = player.getInventory().getItemInMainHand();
            item.setAmount(item.getAmount()-1);
        }

        int taskID = Bukkit.getScheduler().runTaskTimer(XgpLottery.instance, new Runnable() {
            int j = 0;
            @Override
            public void run() {
                for (int i = 9; i < 18; i++) {
                    MyItem guiitem = new MyItem(showItemList.get(i-9+j));
                    if(i==13)
                        inventory.setItem(i,guiitem.addEnchant().getItem());
                    else
                        inventory.setItem(i,guiitem.getItem());
                }
                j++;
                if (j >= showItemList.size() - 9) {
                    j = 0;
                }
                if (j == 17) { // 中奖物品的位置是第 20 个 21-4
                    stop = true;
                }
                float pitch = (float) Math.pow(2.0, j / 18.0);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP,1.0f,pitch);
                if (stop) {
                    // 停止循环
                    Bukkit.getScheduler().cancelTasks(XgpLottery.instance);
                    //给与物品
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1.0f,1.0f);
                    player.getInventory().addItem(award);
                }

            }

        }, 0L, 5L).getTaskId();
        CloseListener closeListener = new CloseListener(taskID,player.getUniqueId(),award,this);
        Bukkit.getPluginManager().registerEvents(closeListener,XgpLottery.instance);
    }

    private void initItemList(List<ItemStack> showItemList,Lottery lottery){
        List<ItemStack> items = lottery.getItems();
        Random random = new Random();
        for (int i = 0; i <= 40; i++) {
            showItemList.add(items.get(random.nextInt(items.size())).clone());
        }
    }

}
