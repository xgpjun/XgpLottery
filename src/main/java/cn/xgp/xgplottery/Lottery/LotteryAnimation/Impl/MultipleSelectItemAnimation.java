package cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl;

import cn.xgp.xgplottery.Gui.Impl.Anim.MultipleSelectItemGui;
import cn.xgp.xgplottery.Listener.CloseListener;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.MultipleAnimation;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

public class MultipleSelectItemAnimation extends MultipleAnimation {
    public MultipleSelectItemAnimation(Player player, Lottery lottery) {
        super(player, lottery);
    }

    @Override
    public String toLore() {
        return LangUtils.MultipleSelectItemAnimation;
    }

    @Override
    public void playAnimation() {
        IntStream.range(0, 10).forEach(i -> awards.add(getOneAward()));
        MultipleSelectItemGui gui = new MultipleSelectItemGui(this);
        player.openInventory(gui.getInventory());
        taskID = Bukkit.getScheduler().runTaskTimer(XgpLottery.instance, new Runnable() {
            int counter = 0;
            @Override
            public void run() {
                counter++;
                gui.glassChange();
                float pitch = (float) Math.pow(2.0, counter / 18.0);
                player.playSound(player.getLocation(), sound,1.0f,pitch);
                if(counter==17){
                    player.playSound(player.getLocation(), finish,1.0f,1.0f);
                    setStop(true);
                    cancelTask();
                }
            }
        }, 0L, 5L).getTaskId();
        CloseListener closeListener = new CloseListener(taskID,player.getUniqueId(),this,true);
        Bukkit.getPluginManager().registerEvents(closeListener,XgpLottery.instance);
    }
}
