package cn.xgp.xgplottery;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Listener.GuiListener;
import cn.xgp.xgplottery.Listener.LoginListener;
import cn.xgp.xgplottery.Listener.LotteryListener;
import cn.xgp.xgplottery.Lottery.*;
import cn.xgp.xgplottery.Utils.*;
import cn.xgp.xgplottery.bStats.Metrics;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class XgpLottery extends JavaPlugin implements PluginMessageListener {

    public static JavaPlugin instance;
    public static PlayerPointsAPI ppAPI;
    public static Economy eco;
    public static Map<String,Lottery> lotteryList = new ConcurrentHashMap<>();
    public static List<LotteryBox> lotteryBoxList = new CopyOnWriteArrayList<>();
    public static List<Location> locations = new CopyOnWriteArrayList<>();
    //正在产生粒子的方块
    public static List<BoxParticle> boxParticleList = new CopyOnWriteArrayList<>();

    public static List<CumulativeRewards> rewards = new CopyOnWriteArrayList<>();

    //当启用数据库模式时，这些字段将不会被实际应用
    //记录次数
    public static List<LotteryTimes> totalTime = new CopyOnWriteArrayList<>();
    //未保底次数
    public static List<LotteryTimes> currentTime = new CopyOnWriteArrayList<>();
    //总次数
    public static List<LotteryTimes> allTimes = new CopyOnWriteArrayList<>();
    //累计抽奖奖励 领取次数
    public static List<LotteryTimes> rewardsTimes = new CopyOnWriteArrayList<>();

    @Override
    public void onLoad() {
        saveDefaultConfig();
        File zhFile = new File(getDataFolder(), "lang/zh_CN.yml");
        if (!zhFile.exists()) {
            saveResource("lang/zh_CN.yml", false);
        }
        File enFile = new File(getDataFolder(), "lang/en_US.yml");
        if (!enFile.exists()) {
            saveResource("lang/en_US.yml", false);
        }
        File dbFile = new File(getDataFolder(), "database.yml");
        if (!dbFile.exists()) {
            saveResource("database.yml", false);
        }
    }


    @Override
    public void onEnable() {

        instance=this;
        Metrics.enable();

        //读取配置文件
        ConfigSetting.loadConfig(getConfig());

        if(ConfigSetting.enableDatabase)
            SqlUtils.getConnection();


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

        Bukkit.getPluginManager().registerEvents(new LoginListener(),this);

        Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance, ConfigSetting::updateConfig);

        //bc
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        SerializeUtils.save();

        SqlUtils.closeConnection();
        log(LangUtils.DisableMessage);
        Bukkit.getScheduler().cancelTask(SerializeUtils.saveTaskId);
        Bukkit.getScheduler().cancelTask(TimesUtils.taskId);

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

    public static void warning(String message) {
        Bukkit.getConsoleSender().sendMessage("§e[XgpLottery] " +"§e"+ message);
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
                Plugin plugin = Bukkit.getPluginManager().getPlugin("PlayerPoints");
                ppAPI = ((PlayerPoints) plugin).getAPI();
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

        Bukkit.getScheduler().cancelTask(SerializeUtils.saveTaskId);
        instance.reloadConfig();
        ConfigSetting.loadConfig(instance.getConfig());
        log(LangUtils.ReloadMessage);
        SqlUtils.closeConnection();
        if(ConfigSetting.enableDatabase)
            SqlUtils.getConnection();

        SerializeUtils.load();
        enableDepend();
    }


    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        System.out.println(123);
        System.out.println(Arrays.toString(message));

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        if (subChannel.equals("XgpLottery")) {
            // 数据处理
            short len = in.readShort();
            byte[] bytes = new byte[len];
            in.readFully(bytes);

            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(bytes));
            try {
                String json = msgin.readUTF();
                ChatUtils.sendText(json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }



        }
    }
}
