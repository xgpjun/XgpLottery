package cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl;

import cn.xgp.xgplottery.Gui.Impl.Anim.SimpleMultipleAnimGui;
import cn.xgp.xgplottery.Listener.CloseListener;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.MultipleAnimation;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class SimpleMultipleAnimation extends MultipleAnimation {

    public SimpleMultipleAnimation(Player player, Lottery lottery) {
        super(player, lottery);
    }

    @Override
    public String toLore() {
        return LangUtils.SimpleMultipleAnimation;
    }

    @Override
    public void playAnimation() {
        IntStream.range(0, 10).forEach(i -> awards.add(getOneAward()));
        SimpleMultipleAnimGui gui = new SimpleMultipleAnimGui();

        player.openInventory(gui.getInventory());
        taskID = Bukkit.getAsyncScheduler().runAtFixedRate(XgpLottery.instance, new Consumer<ScheduledTask>() {
            int counter=0;

            @Override
            public void accept(ScheduledTask scheduledTask) {
                gui.setNextItem(awards.get(counter).getRecordDisplayItem());
                float pitch = (float) Math.pow(2.0, counter / 10.0);
                player.playSound(player.getLocation(), sound,0.2f,pitch);
                counter++;
                if(counter==10){
                    player.playSound(player.getLocation(), finish,1.0f,1.0f);
                    setStop(true);
                    cancelTask();
                }
            }
        },1500L,1000L, TimeUnit.MILLISECONDS);
        CloseListener closeListener = new CloseListener(taskID,player.getUniqueId(),this,true);
        Bukkit.getPluginManager().registerEvents(closeListener,XgpLottery.instance);
    }
}
