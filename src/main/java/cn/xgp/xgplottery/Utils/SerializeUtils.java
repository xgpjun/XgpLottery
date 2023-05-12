package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryBox;
import cn.xgp.xgplottery.Lottery.LotteryTimes;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.Impl.Custom;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.Impl.Maximum;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.ProbabilityCalculator;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SerializeUtils {

    public static void save(){
        saveLotteryData();
        saveLotteryBoxData();
        saveLotteryTimes();
    }

    public static void load(){
        SerializeUtils.createLotteryFolder();
        SerializeUtils.loadLotteryData();
        SerializeUtils.loadLotteryBoxData();
        loadCurrentLotteryTimes();
        SerializeUtils.loadTotalLotteryTimes();
    }

    public static ProbabilityCalculator readCalculator(String calculator){
        switch (calculator){
            case "Maximum": return new Maximum();
            case "Custom":
            default: return new Custom();
        }
    }

    public static void createLotteryFolder(){
        File folder =new File(XgpLottery.instance.getDataFolder(), "Lottery");
        if(!folder.exists()){
            folder.mkdir();
        }
        File file1 =new File(folder,"default.yml");
        try{
            file1.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //读取Lottery文件夹里的所有yml文件
    public static void loadLotteryData(){
        Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance, () -> {
            File folder =new File(XgpLottery.instance.getDataFolder(), "Lottery");
            if(folder.isDirectory()){
                File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
                if (files != null) {
                    for (File file : files) {
                        // 读取YML文件中已序列化的对象信息
                        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(file);
                        if(yamlConfig.contains("isLottery")&&yamlConfig.getBoolean("isLottery")){
                            try {
                                // 使用deserialize方法将YAML配置文件中的Map转换为Lottery对象
                                ConfigurationSection section = yamlConfig.getConfigurationSection("data");
                                Lottery lottery = Lottery.deserialize(section.getValues(false));
                                lottery.setName(file.getName().split("\\.")[0]);
                                XgpLottery.lotteryList.put(lottery.getName(),lottery);
                            } catch (Exception e) {
                                // 如果反序列化失败，则在控制台上记录错误消息
                                XgpLottery.instance.getLogger().warning("读取文件失败： " + file.getName());
                            }
                        }
                    }
                }
            }
            XgpLottery.instance.getLogger().info("奖池文件读取完成 ");
        });
    }
    public static void saveLotteryData(){
        File folder = new File(XgpLottery.instance.getDataFolder(), "Lottery");
        if(!folder.exists()){
            folder.mkdirs();
        }
        for(Lottery lottery:XgpLottery.lotteryList.values()){
            String fileName=lottery.getName()+".yml";
            File file=new File(folder,fileName);
            YamlConfiguration yamlConfig =new YamlConfiguration();
            yamlConfig.set("isLottery",true);
            yamlConfig.set("data",lottery.serialize());
            try{
                yamlConfig.save(file);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public static void saveLotteryBoxData() {
        // 创建保存数据的文件
        File file = new File(XgpLottery.instance.getDataFolder(), "lotteryBox.yml");
        // 创建新的配置文件
        YamlConfiguration yml = new YamlConfiguration();
        for (int i = 0; i < XgpLottery.lotteryBoxList.size(); i++) {
            LotteryBox lotteryBox = XgpLottery.lotteryBoxList.get(i);
            // 生成唯一的数据路径，例如 data.0、data.1、data.2，以便区分每个 LotteryBox 对象
            String dataPath = "data." + i;
            // 将 LotteryBox 对象序列化后存入配置文件的相应路径下
            yml.set(dataPath, lotteryBox.serialize());
        }

        // 保存配置文件到指定的文件路径
        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadLotteryBoxData() {
        // 创建保存数据的文件
        File file = new File(XgpLottery.instance.getDataFolder(), "lotteryBox.yml");

        // 检查文件是否存在
        if (file.exists()) {
            // 从文件加载配置
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

            // 清空现有的 lotteryBoxList
            XgpLottery.lotteryBoxList.clear();

            // 获取所有数据路径，例如 data.0、data.1、data.2
            ConfigurationSection dataSection = yml.getConfigurationSection("data");
            if (dataSection != null) {
                Set<String> dataPaths = dataSection.getKeys(false);

                // 遍历所有数据路径
                for (String dataPath : dataPaths) {
                    // 根据数据路径获取 LotteryBox 对象的序列化数据
                    Map<String, Object> serializedData = yml.getConfigurationSection("data." + dataPath).getValues(false);

                    // 反序列化为 LotteryBox 对象并添加到 lotteryBoxList 中
                    LotteryBox lotteryBox = LotteryBox.deserialize(serializedData);
                    XgpLottery.lotteryBoxList.add(lotteryBox);
                    XgpLottery.locations.add(lotteryBox.getLocation());
                }
            }
        }

    }
    public static void saveLotteryTimes(){
        File file = new File(XgpLottery.instance.getDataFolder(), "lotteryTimes.yml");
        // 创建新的配置文件
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        for(LotteryTimes lotteryTimes :XgpLottery.lotteryTimesList){
            String lottery = lotteryTimes.getLotteryName();
            UUID uuid = lotteryTimes.getUuid();
            int times = lotteryTimes.getTimes();
            String dataPath = "total."+lottery+"."+uuid.toString();
            yml.set(dataPath,times);
        }
        for(LotteryTimes lotteryTimes :XgpLottery.currentTime){
            String lottery = lotteryTimes.getLotteryName();
            UUID uuid = lotteryTimes.getUuid();
            int times = lotteryTimes.getTimes();
            String dataPath = "current."+lottery+"."+uuid.toString();
            yml.set(dataPath,times);
        }
        // 保存配置文件到指定的文件路径
        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayerTotalLotteryTimes(Player player,String lotteryName){
        File file = new File(XgpLottery.instance.getDataFolder(), "lotteryTimes.yml");
        LotteryTimes lotteryTimes = LotteryTimes.getLotteryTimes(player.getUniqueId(),lotteryName);
        String dataPath =  "total."+lotteryName+"."+player.getUniqueId();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        yml.set(dataPath,lotteryTimes.getTimes());
        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadTotalLotteryTimes(){
        Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance, () -> {
            File file = new File(XgpLottery.instance.getDataFolder(), "lotteryTimes.yml");
            YamlConfiguration y = YamlConfiguration.loadConfiguration(file);
            if (!y.contains("total")) {
                // 配置文件中不存在"total"部分
                return;
            }
            ConfigurationSection yml = y.getConfigurationSection("total");
            // 遍历第一层（奖池名称）
            if(yml!=null)
                for (String lottery : yml.getKeys(false)) {
                    ConfigurationSection lotterySection = yml.getConfigurationSection(lottery);
                    if (lotterySection != null) {
                        // 遍历第二层（玩家 UUID）
                        for (String uuid : lotterySection.getKeys(false)) {
                            UUID playerUUID;
                            try {
                                // 将 UUID 字符串解析为 UUID 对象
                                playerUUID = UUID.fromString(uuid);
                            } catch (IllegalArgumentException e) {
                                // UUID 字符串无效，跳过该键
                                continue;
                            }
                            // 获取玩家的抽奖次数值
                            int times = lotterySection.getInt(uuid);
                            LotteryTimes lotteryTimes = new LotteryTimes(playerUUID,times,lottery);
                            XgpLottery.lotteryTimesList.add(lotteryTimes);
                            // 在这里处理你的逻辑，使用 `lottery`、`playerUUID` 和 `times` 数据进行操作
                            // 例如创建一个 `LotteryTimes` 对象，并添加到适当的列表中
                        }
                    }
                }
            XgpLottery.instance.getLogger().info("抽奖总次数读取完成");
        });

    }

    public static void savePlayerCurrentLotteryTimes(Player player,String lotteryName){
        File file = new File(XgpLottery.instance.getDataFolder(), "lotteryTimes.yml");
        LotteryTimes lotteryTimes = LotteryTimes.getCurrentLotteryTimes(player.getUniqueId(),lotteryName);
        String dataPath =  "current."+lotteryName+"."+player.getUniqueId();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        yml.set(dataPath,lotteryTimes.getTimes());
        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadCurrentLotteryTimes(){

        Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance, () -> {
            File file = new File(XgpLottery.instance.getDataFolder(), "lotteryTimes.yml");

            YamlConfiguration y = YamlConfiguration.loadConfiguration(file);
            if (!y.contains("current")) {
                // 配置文件中不存在"current"部分
                return;
            }
            ConfigurationSection yml = y.getConfigurationSection("current");
            // 遍历第一层（奖池名称）
            if(yml!=null) {
                for (String lottery : yml.getKeys(false)) {
                    ConfigurationSection lotterySection = yml.getConfigurationSection(lottery);
                    if (lotterySection != null) {
                        // 遍历第二层（玩家 UUID）
                        for (String uuid : lotterySection.getKeys(false)) {
                            UUID playerUUID;
                            try {
                                // 将 UUID 字符串解析为 UUID 对象
                                playerUUID = UUID.fromString(uuid);
                            } catch (IllegalArgumentException e) {
                                // UUID 字符串无效，跳过该键
                                continue;
                            }
                            // 获取玩家的抽奖次数值
                            int times = lotterySection.getInt(uuid);
                            LotteryTimes lotteryTimes = new LotteryTimes(playerUUID, times, lottery);
                            XgpLottery.currentTime.add(lotteryTimes);
                        }
                    }
                }
            }
            XgpLottery.instance.getLogger().info("玩家保底未中次数读取完成 ");
        });

    }

}
