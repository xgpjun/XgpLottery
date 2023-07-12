package cn.xgp.xgplottery.Utils;

import java.util.Random;

public class MathUtils {

    public static int getRandomInt(){
        return new Random().nextInt();
    }

    /**
     *
     * @param min 最小值
     * @param max 最大值
     * @return [min,max]闭区间的一个整数
     */
    public static int getRandomInt(int min,int max){
        max += 1;
        if(max-min<=0){
            return 0;
        }
        return new Random().nextInt(max-min) + min;
    }
    public static int getRandomInt(int min,int max,long seed){
        max += 1;
        if(max-min>=0){
            return 0;
        }
        return new Random(seed).nextInt(max-min) + min;
    }
}
