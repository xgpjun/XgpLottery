package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.LotteryBox;
import cn.xgp.xgplottery.Lottery.LotteryTimes;
import cn.xgp.xgplottery.XgpLottery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class old {
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
        for(LotteryTimes lotteryTimes :XgpLottery.totalTime){
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
                            XgpLottery.totalTime.add(lotteryTimes);
                            // 在这里处理你的逻辑，使用 `lottery`、`playerUUID` 和 `times` 数据进行操作
                            // 例如创建一个 `LotteryTimes` 对象，并添加到适当的列表中
                        }
                    }
                }
            XgpLottery.log("抽奖总次数读取完成");
        });

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
            XgpLottery.log("玩家保底未中次数读取完成 ");
        });

    }

    public static void savePlayerTotalLotteryTimes(Player player, String lotteryName){
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
    public static String toJson(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Location.class, new SerializeUtils.LocationAdapter())
                .create();
        return gson.toJson(XgpLottery.lotteryBoxList);
    }

    public static List<LotteryBox> fromJson(String json){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Location.class, new SerializeUtils.LocationAdapter())
                .create();
        Type listType = new TypeToken<List<LotteryBox>>() {}.getType();
        return gson.fromJson(json,listType);
    }


}
