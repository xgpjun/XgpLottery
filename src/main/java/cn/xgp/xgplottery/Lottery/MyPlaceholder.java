package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.Utils.ConfigSetting;
import cn.xgp.xgplottery.Utils.TimesUtils;
import cn.xgp.xgplottery.XgpLottery;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class MyPlaceholder extends PlaceholderExpansion {

    private final JavaPlugin plugin;

    public MyPlaceholder(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public String getIdentifier() {
        return "XgpLottery";
    }

    @Override
    public String getAuthor() {
        return "1badsobig";
    }

    @Override
    public String getVersion() {
        return ConfigSetting.version;
    }
    @Override
    public boolean canRegister() {
        return true; // 可以注册占位符
    }

    /**
     *
     * %XgpLottery_player%
     * 总抽奖次数
     *
     * %XgpLottery_max_lotteryName%
     * 奖池的保底次数
     *
     * %XgpLottery_value_lotteryName%
     * 奖池售价
     *
     * %XgpLottery_playerName_lotteryName%
     * 获取指定玩家的指定奖池抽奖次数
     * %XgpLottery_this_lotteryName%
     * 当前玩家指定奖池抽奖次数
     *
     * %XgpLottery_top_rank_name%
     * %XgpLottery_top_rank_amount%
     * 总抽奖次数排名
     *
     * XgpLottery_playerName_lotteryName_current%
     * 当前抽奖次数
     *
     * %XgpLottery_top_lotteryName_rank_name%
     * 指定奖池抽奖排名的第rank名的玩家id
     * %XgpLottery_top_lotteryName_rank_amount%
     *
     */
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        String[] args = params.split("_");
        //总抽奖次数
        if(args.length==1){
            String playerName = args[0].equals("this")?player.getName():args[0];
            if(playerName==null)
                return null;
            UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
            return Integer.toString(TimesUtils.getAllTimes(uuid));
        }
        //玩家指定奖池抽奖次数
        if(args.length==2){
            String lotteryName = args[1];
            if(args[0].equals("max")){
                Lottery lottery = XgpLottery.lotteryList.get(lotteryName);
                if(lottery==null||lottery.getMaxTime()<1)
                    return "无";
                return String.valueOf(lottery.getMaxTime());
            }
            if(args[0].equals("value")){
                Lottery lottery = XgpLottery.lotteryList.get(lotteryName);
                if(lottery==null||lottery.getValue()<=0)
                    return "无";
                return String.valueOf(lottery.getValue());
            }

            String playerName = args[0].equals("this")?player.getName():args[0];
            if(playerName==null)
                return null;

            UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
            return Integer.toString(TimesUtils.getTimes(uuid,lotteryName));
        }
        //总抽奖次数排名
        //当前抽奖次数
        if(args.length==3){
            //总抽奖次数排名
            if(args[0].equals("top")){
                int rank;
                try{
                    rank = Integer.parseInt(args[1]);
                    LotteryTimes times = TimesUtils.getAllTimesTop(rank);
                    if(times==null)
                        return "无";
                    if(args[2].equals("name"))
                        return Bukkit.getOfflinePlayer(times.getUuid()).getName();
                    else if(args[2].equals("amount"))
                        return String.valueOf(times.getTimes());
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
            //当前抽奖次数
            else if(args[2].equals("current")){
                String playerName = args[0].equals("ThisPlayer")?player.getName():args[0];
                if(playerName==null)
                    return null;
                UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
                return String.valueOf(TimesUtils.getCurrentTimes(uuid,args[1]));
            }
        }

        if(args.length==4){
            if(args[0].equals("top")){
                String lotteryName = args[1];
                int rank = Integer.parseInt(args[2]);
                TimesTop timesTop = new TimesTop(true,lotteryName);
                LotteryTimes times = timesTop.getTimesByRank(rank);
                if(times==null)
                    return "无";
                if(args[3].equals("name")){
                    return Bukkit.getOfflinePlayer(times.getUuid()).getName();
                }
                else if(args[3].equals("amount")){
                    return String.valueOf(times.getTimes());
                }
            }
        }
        return null; // Placeholder is unknown by the Expansion
    }


}
