package cn.xgp.xgplottery.Lottery.LotteryAnimation;

import cn.xgp.xgplottery.Lottery.Lottery;
import org.bukkit.entity.Player;

public abstract class MultipleAnimation extends LotteryAnimation{
    public MultipleAnimation(Player player, Lottery lottery) {
        super(player, lottery);
    }
}
