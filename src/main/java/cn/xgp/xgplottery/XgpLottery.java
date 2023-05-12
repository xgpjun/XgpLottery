package cn.xgp.xgplottery;

import cn.xgp.xgplottery.Command.GuiCommand;
import cn.xgp.xgplottery.Listener.GetNameListener;
import cn.xgp.xgplottery.Listener.GuiListener;
import cn.xgp.xgplottery.Listener.LotteryListener;
import cn.xgp.xgplottery.Lottery.*;
import cn.xgp.xgplottery.Utils.ConfigSetting;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.SerializeUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public final class XgpLottery extends JavaPlugin {

    public static JavaPlugin instance;
    public static Map<String,Lottery> lotteryList = new ConcurrentHashMap<>();
    public static List<LotteryBox> lotteryBoxList = new Vector<>();
    public static List<Location> locations = new Vector<>();
    //正在产生粒子的方块
    public static List<BoxParticle> boxParticleList;
    //记录次数
    public static List<LotteryTimes> lotteryTimesList = new Vector<>();
    //未保底次数
    public static List<LotteryTimes> currentTime = new Vector<>();

    @Override
    public void onLoad() {
        saveDefaultConfig();
        saveResource("lang\\zh_CN.yml",false);
    }


    @Override
    public void onEnable() {
        instance=this;
        //读取配置文件
        ConfigSetting.loadConfig(getConfig());
        SerializeUtils.load();

        //启动相关依赖
        enableDepend();

        log("§a"+LangUtils.EnableMessage);
        //注册命令
        Objects.requireNonNull(Bukkit.getPluginCommand("xgplottery")).setExecutor(new GuiCommand());
        //注册监听器
        Bukkit.getPluginManager().registerEvents(new GuiListener(),this);
        Bukkit.getPluginManager().registerEvents(new LotteryListener(),this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance.getLogger().info(LangUtils.DisableMessage);
        BoxParticle.clearAllParticle();
        saveConfig();

        SerializeUtils.save();
    }

    public static Future<String> getInput(Player player){
        CompletableFuture<String> future =new CompletableFuture<>();
        Bukkit.getScheduler().runTask(XgpLottery.instance,()->{
            GetNameListener listener =new GetNameListener(player.getUniqueId(),future,20);
            Bukkit.getPluginManager().registerEvents(listener,XgpLottery.instance);
        });
        return future;
    }

    public static LotteryBox getLotteryBoxByLocation(Location targetLocation) {
        for (LotteryBox lotteryBox : lotteryBoxList) {
            if (lotteryBox.getLocation().equals(targetLocation)) {
                return lotteryBox;
            }
        }
        return null;
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage("§6[XgpLottery] " + message);
    }

    static void enableDepend(){
        if (ConfigSetting.showProbability&&Bukkit.getPluginManager().getPlugin("ParticleLib") != null){
            boxParticleList = new Vector<>();
            BoxParticle.playAllParticle();
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            // 创建占位符扩展并注册
            new MyPlaceholder(XgpLottery.instance).register();
        }
    }

    public static void reload(){
        log("正在重载");
        instance.reloadConfig();
        ConfigSetting.loadConfig(instance.getConfig());
        SerializeUtils.load();
        enableDepend();
    }


}
