package cn.xgp.xgplottery.Lottery.ProbabilityCalculator;

import cn.xgp.xgplottery.Lottery.Award;
import cn.xgp.xgplottery.Lottery.Lottery;
import lombok.Getter;
import org.bukkit.entity.Player;

public abstract class ProbabilityCalculator {
    public abstract Award getAward(Lottery lottery, Player player);
    public abstract void sendMessage();
    @Getter
    protected boolean isSpecial;
}
