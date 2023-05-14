package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryBox;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length!=2){
            sender.sendMessage(ChatColor.RED+"输入格式有误");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery delete [奖池名称]\n" + ChatColor.GREEN + "删除指定奖池，本命令会自动重载插件，并清除掉相关数据");
            return true;
        }
        String name = args[1];
        Lottery lottery = XgpLottery.lotteryList.get(name);
        if(lottery==null){
            sender.sendMessage(ChatColor.RED+"啊咧咧？ 没找到奖池呢~");
            return true;
        }

        File folder =new File(XgpLottery.instance.getDataFolder(), "Lottery");
        String fileName=name+".yml";
        File file=new File(folder,fileName);
        file.delete();

        XgpLottery.lotteryList.remove(name);
        List<LotteryBox> delList = new ArrayList<>();
        for(LotteryBox lotteryBox:XgpLottery.lotteryBoxList) {
            if(lotteryBox.getLotteryName().equals(name)){
                delList.add(lotteryBox);
            }
        }
        for(LotteryBox lotteryBox:delList){
            XgpLottery.lotteryBoxList.remove(lotteryBox);
            XgpLottery.locations.remove(lotteryBox.getLocation());
        }
        SerializeUtils.saveLotteryBoxData();

        file = new File(XgpLottery.instance.getDataFolder(), "lotteryTimes.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("total."+name,null);
        config.set("current."+name,null);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XgpLottery.reload();


        return true;
    }
}
