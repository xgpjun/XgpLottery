package cn.xgp.xgplottery.Lottery;

import lombok.AllArgsConstructor;
import lombok.Data;

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
