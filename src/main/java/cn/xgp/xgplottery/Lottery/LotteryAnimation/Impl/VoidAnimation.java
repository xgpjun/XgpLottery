package cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl;

import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.Utils.GiveUtils;
import org.bukkit.entity.Player;

public class VoidAnimation extends LotteryAnimation {

    public VoidAnimation(Player player, Lottery lottery) {
        super(player, lottery);
    }

    @Override
    public String toLore() {
        return "无动画抽奖";
    }

    @Override
    public void playAnimation() {
        awards.add(getOneAward());
        player.playSound(player.getLocation(), LotteryAnimation.getFinish(), 1.0f, 1.0f);
        GiveUtils.giveAward(player, awards);
        calculator.sendMessage();
    }
}
