package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Lottery.BoxParticle;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryBox;
import cn.xgp.xgplottery.Lottery.LotteryTimes;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeleteCommand implements TabExecutor {

    /*
    //记录次数
    public static List<LotteryTimes> lotteryTimesList = new CopyOnWriteArrayList<>();
    //未保底次数
    public static List<LotteryTimes> currentTime = new CopyOnWriteArrayList<>();
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length!=2){
            sender.sendMessage(ChatColor.RED+"输入格式有误");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery delete [奖池名称]\n" + ChatColor.GREEN + "删除指定奖池，本命令会并清除掉相关数据");
            return true;
        }
        String name = args[1];
        Lottery lottery = XgpLottery.lotteryList.get(name);
        if(lottery==null){
            sender.sendMessage(ChatColor.RED+"啊咧咧？ 没找到奖池呢~");
            return true;
        }

        //删除lotteryList
        File folder =new File(XgpLottery.instance.getDataFolder(), "Lottery");
        String fileName=name+".yml";
        File file=new File(folder,fileName);
        file.delete();
        XgpLottery.lotteryList.remove(name);
        //删除lotteryBoxList
        List<LotteryBox> delList = new ArrayList<>();
        for(LotteryBox lotteryBox:XgpLottery.lotteryBoxList) {
            if(lotteryBox.getLotteryName().equals(name)){
                delList.add(lotteryBox);
            }
        }
        List<Location> locationList = new ArrayList<>();
        for(LotteryBox lotteryBox:delList){
            XgpLottery.lotteryBoxList.remove(lotteryBox);
            locationList.add(lotteryBox.getLocation());
        }
        //获取获得的location位置的粒子特效
        List<BoxParticle> boxParticles = new ArrayList<>();
        for(BoxParticle boxParticle :XgpLottery.boxParticleList){
            for(Location location : locationList){
                if(boxParticle.location.equals(location)){
                    boxParticles.add(boxParticle);
                }
            }
        }
        //删除boxParticle
        for (BoxParticle boxParticle:boxParticles){
            boxParticle.clearParticle();
            XgpLottery.boxParticleList.remove(boxParticle);
        }
        //删除locations
        for(Location location:locationList){
            XgpLottery.locations.remove(location);
        }
        //删除lotteryTime
        List<LotteryTimes> times = new ArrayList<>();
        for(LotteryTimes lotteryTimes: XgpLottery.totalTime){
            if(lotteryTimes.getLotteryName().equals(name)){
                times.add(lotteryTimes);
            }
        }
        for(LotteryTimes lotteryTimes:times){
            XgpLottery.totalTime.remove(lotteryTimes);
        }
        List<LotteryTimes> cTimes = new ArrayList<>();
        for(LotteryTimes lotteryTimes: XgpLottery.currentTime){
            if(lotteryTimes.getLotteryName().equals(name)){
                cTimes.add(lotteryTimes);
            }
        }
        for(LotteryTimes lotteryTimes:cTimes){
            XgpLottery.currentTime.remove(lotteryTimes);
        }

        SerializeUtils.save();
        sender.sendMessage(ChatColor.GREEN+"删除成功！");
        return true;
    }
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2){
            List<String> strings = new ArrayList<>(XgpLottery.lotteryList.keySet());
            return XgpLotteryCommand.filter(strings,args);
        }
        return new ArrayList<>();
    }
}
