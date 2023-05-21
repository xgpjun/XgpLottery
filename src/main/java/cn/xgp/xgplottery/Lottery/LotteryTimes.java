package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.XgpLottery;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class LotteryTimes {
    private String lotteryName;
    private UUID uuid;
    private int times;

    public LotteryTimes(UUID uuid,String lotteryName){
        this( lotteryName,uuid,0);
    }

    public LotteryTimes(UUID uuid){
        this(null,uuid,0 );
    }

}
