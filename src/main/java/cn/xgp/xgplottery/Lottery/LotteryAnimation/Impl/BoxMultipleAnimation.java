package cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl;

import cn.xgp.xgplottery.Gui.Impl.Anim.BoxMultipleAnimGui;
import cn.xgp.xgplottery.Listener.CloseListener;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.MultipleAnimation;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class BoxMultipleAnimation extends MultipleAnimation {
    List<ItemStack> showItemList = new ArrayList<>();
    public BoxMultipleAnimation(Player player, Lottery lottery) {
        super(player, lottery);
    }

    @Override
    public String toLore() {
        return LangUtils.BoxMultipleAnimation;
    }

    @Override
    public void playAnimation() {
        initItemList(showItemList,lottery);
        //获得十个奖品。
        IntStream.range(0, 10).forEach(i -> awards.add(getOneAward()));

        BoxMultipleAnimGui gui = new BoxMultipleAnimGui();
        Inventory inv = gui.getInventory();
        player.openInventory(inv);

        taskID = Bukkit.getScheduler().runTaskTimer(XgpLottery.instance,new Runnable (){
            int counter = 0;
            int index = 0;
            @Override
            public void run() {
                counter++;
                float pitch = (float) Math.pow(2.0, counter / 30.0);
                for (int i = 10; i < 17; i++) {
                    MyItem myItem = new MyItem(showItemList.get(i-9+counter));
                    if(i==13)
                        inv.setItem(i,myItem.addEnchant().getItem());
                    else
                        inv.setItem(i,myItem.getItem());
                }
                player.playSound(player.getLocation(), sound,1.0f,pitch);

                if(counter%3==0){
                    gui.setAward(index,awards.get(index));
                    player.playSound(player.getLocation(), finish,0.2f,1.0f);
                    index++;
                    if(index==10){
                        setStop(true);
                    }
                }
                if(isStop()){
                    cancelTask();
                    player.playSound(player.getLocation(), finish,1.0f,1.0f);
                }
            }
        },0L,5L).getTaskId();
        CloseListener closeListener = new CloseListener(taskID,player.getUniqueId(),this,true);
        Bukkit.getPluginManager().registerEvents(closeListener,XgpLottery.instance);
    }

    private void initItemList(List<ItemStack> showItemList, Lottery lottery){
        Random random = new Random();
        int item = lottery.getAmount();
        int sp = lottery.getSpAmount();
        for (int i = 0; i <= 45; i++) {
            int next = random.nextInt(item+sp);
            ItemStack itemStack = next>=item?lottery.getSpAwards().get(next-item).getRecordDisplayItem().clone():lottery.getAwards().get(next).getRecordDisplayItem().clone();
            showItemList.add(itemStack);
        }
    }
}
