package cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl;

import cn.xgp.xgplottery.Gui.Impl.Anim.DefMultipleAnimGui;
import cn.xgp.xgplottery.Listener.CloseListener;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.MultipleAnimation;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

public class DefaultMultipleAnimation extends MultipleAnimation {

    public DefaultMultipleAnimation(Player player, Lottery lottery) {
        super(player, lottery);
    }

    @Override
    public String toLore() {
        return "默认十连抽动画";
    }

    @Override
    public void playAnimation() {
        //获得十个奖品。
        IntStream.range(0, 10).forEach(i -> awards.add(getOneAward()));

        DefMultipleAnimGui gui = new DefMultipleAnimGui();
        player.openInventory(gui.getInventory());

        taskID = Bukkit.getScheduler().runTaskTimer(XgpLottery.instance,new Runnable (){
            int counter = 0;
            int index = 0;
            @Override
            public void run() {
                counter++;
                float pitch = (float) Math.pow(2.0, counter / 30.0);
                //3->出现一个奖品 30 结束
                if(counter<=21||
                        (counter<=27&&counter%2==0)||
                        counter==30){
                    gui.borderChange();
                    player.playSound(player.getLocation(), sound,1.0f,pitch);
                }

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
}
