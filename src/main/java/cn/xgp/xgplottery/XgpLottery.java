package cn.xgp.xgplottery;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Listener.GetNameListener;
import cn.xgp.xgplottery.Listener.GuiListener;
import cn.xgp.xgplottery.Listener.LotteryListener;
import cn.xgp.xgplottery.Lottery.*;
import cn.xgp.xgplottery.Utils.*;

import cn.xgp.xgplottery.bStats.Metrics;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;


public final class XgpLottery extends JavaPlugin {

    public static JavaPlugin instance;
    public static PlayerPointsAPI ppAPI;
    public static Economy eco;
    public static Map<String,Lottery> lotteryList = new ConcurrentHashMap<>();
    public static List<LotteryBox> lotteryBoxList = new CopyOnWriteArrayList<>();
    public static List<Location> locations = new ArrayList<>();
    //正在产生粒子的方块
    public static List<BoxParticle> boxParticleList = new ArrayList<>();
    //记录次数
    public static List<LotteryTimes> totalTime = new CopyOnWriteArrayList<>();
    //未保底次数
    public static List<LotteryTimes> currentTime = new CopyOnWriteArrayList<>();
    //总次数
    public static List<LotteryTimes> allTimes = new CopyOnWriteArrayList<>();

    @Override
    public void onLoad() {
        saveDefaultConfig();
        saveResource("lang\\zh_CN.yml",false);
    }


    @Override
    public void onEnable() {
        instance=this;
        Metrics.enable();
        System.setProperty("file.encoding","UTF8");

        //读取配置文件
        ConfigSetting.loadConfig(getConfig());
        SerializeUtils.load();
        TimesUtils.autoLoadTop();
        //启动相关依赖
        enableDepend();

        log(LangUtils.EnableMessage);
        //注册命令

        Objects.requireNonNull(Bukkit.getPluginCommand("xgplottery")).setExecutor(new XgpLotteryCommand());

        //注册监听器
        Bukkit.getPluginManager().registerEvents(new GuiListener(),this);

        Bukkit.getPluginManager().registerEvents(new LotteryListener(),this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log(LangUtils.DisableMessage);
        Bukkit.getScheduler().cancelTask(SerializeUtils.saveTaskId);
        Bukkit.getScheduler().cancelTask(TimesUtils.taskId);

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
        if(lotteryBoxList==null)
            return null;
        for (LotteryBox lotteryBox : lotteryBoxList) {
            if (lotteryBox.getLocation().equals(targetLocation)) {
                return lotteryBox;
            }
        }
        return null;
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage("§6[XgpLottery] " +"§a"+ message);
    }

    static void enableDepend(){
        if (instance.getConfig().getBoolean("EnableParticle")&&Bukkit.getPluginManager().getPlugin("ParticleLib") != null){
            BoxParticle.playAllParticle();
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            // 创建占位符扩展并注册
            new MyPlaceholder(XgpLottery.instance).register();
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
            try{
                Class<?> playerPointsClass = Class.forName("org.black_ixx.playerpoints.PlayerPoints");
                Method getInstanceMethod = playerPointsClass.getMethod("getInstance");
                ppAPI = PlayerPoints.getInstance().getAPI();
                }catch (Exception e){
                XgpLottery.instance.getLogger().warning("加载PlayerPoints依赖失败，请尝试更新到最新版本");
            }
        }
        if(Bukkit.getPluginManager().isPluginEnabled("Vault")){
            RegisteredServiceProvider<Economy> rsp = instance.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                eco =rsp.getProvider();
            }
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
