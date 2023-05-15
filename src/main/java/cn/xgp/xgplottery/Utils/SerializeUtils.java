package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryBox;
import cn.xgp.xgplottery.Lottery.LotteryTimes;
import cn.xgp.xgplottery.XgpLottery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class SerializeUtils {

    public static int saveTaskId;

    public static void save(){
        saveLotteryData();
        saveJsonData();
    }

    public static void load(){
        createLotteryFolder();

        loadLotteryData();
        loadJsonData();
        saveTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(XgpLottery.instance, () -> {
            if(ConfigSetting.autoSaveMsg)
                XgpLottery.log("正在自动保存数据");
            save();
        }, ConfigSetting.autoSaveTime, ConfigSetting.autoSaveTime);
    }

    public static void createLotteryFolder(){
        File folder =new File(XgpLottery.instance.getDataFolder(), "Lottery");
        if(!folder.exists()){
            if(folder.mkdir()){
                XgpLottery.log("创建奖池文件夹成功");
            }
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
                                assert section != null;
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
            XgpLottery.log("奖池文件读取完成 ");
        });
    }
    public static void saveLotteryData(){
        File folder = new File(XgpLottery.instance.getDataFolder(), "Lottery");
        if(!folder.exists()){
            if(folder.mkdirs()){
                XgpLottery.log("创建奖池文件夹成功");
            }
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

    public static void loadJsonData(){
        File file = new File(XgpLottery.instance.getDataFolder(), "lotteryData.dat");
        if (!file.exists())
            return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String jsonData = jsonBuilder.toString();
            dataFromJson(jsonData);
            XgpLottery.log("读取数据成功");
        }catch (Exception e) {
            XgpLottery.log("读取 JSON 文件时发生错误：" + e.getMessage());
        }
    }

    public static void saveJsonData(){
        File file = new File(XgpLottery.instance.getDataFolder(), "lotteryData.dat");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(dataToJson());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String dataToJson(){
        DataContainer data = new DataContainer();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .create();
        return gson.toJson(data);
    }
    public static void dataFromJson(String json){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .create();
        DataContainer data = gson.fromJson(json, new TypeToken<DataContainer>(){}.getType());
        List<LotteryTimes> lotteryTimesList = new CopyOnWriteArrayList<>();
        List<LotteryTimes> currentTime = new CopyOnWriteArrayList<>();
        List<LotteryBox> lotteryBoxList = new ArrayList<>();


        if(data.lotteryBoxList!=null)
            lotteryBoxList= data.lotteryBoxList;
        if(data.currentTime!=null)
            currentTime = data.currentTime;
        if(data.lotteryTimesList!=null)
            lotteryTimesList = data.lotteryTimesList;

        for(LotteryBox lotteryBox :lotteryBoxList){
            XgpLottery.locations.add(lotteryBox.getLocation());

        }

        XgpLottery.lotteryBoxList = lotteryBoxList;
        XgpLottery.currentTime = currentTime;
        XgpLottery.totalTime = lotteryTimesList;
    }

    static class DataContainer{
        public List<LotteryTimes> lotteryTimesList;
        public List<LotteryTimes> currentTime;
        public List<LotteryBox> lotteryBoxList;

        public DataContainer(){
            this.lotteryTimesList = XgpLottery.totalTime;
            this.currentTime = XgpLottery.currentTime;
            this.lotteryBoxList = XgpLottery.lotteryBoxList;
        }
    }
    static class LocationAdapter extends TypeAdapter<Location> {
        @Override
        public void write(JsonWriter json, Location location) throws IOException {
            if (location == null) {
                json.nullValue();
                return;
            }
            json.beginObject();
            json.name("world").value(Objects.requireNonNull(location.getWorld()).getName());
            json.name("x").value(location.getX());
            json.name("y").value(location.getY());
            json.name("z").value(location.getZ());
            json.name("yaw").value(location.getYaw());
            json.name("pitch").value(location.getPitch());
            json.endObject();
        }

        @Override
        public Location read(JsonReader json) throws IOException {
            if (json.peek() == null) {
                return null;
            }
            json.beginObject();
            String worldName = null;
            double x = 0;
            double y = 0;
            double z = 0;
            float yaw = 0;
            float pitch = 0;

            while (json.hasNext()) {
                String name = json.nextName();
                switch (name) {
                    case "world":
                        worldName = json.nextString();
                        break;
                    case "x":
                        x = json.nextDouble();
                        break;
                    case "y":
                        y = json.nextDouble();
                        break;
                    case "z":
                        z = json.nextDouble();
                        break;
                    case "yaw":
                        yaw = (float) json.nextDouble();
                        break;
                    case "pitch":
                        pitch = (float) json.nextDouble();
                        break;
                    default:
                        json.skipValue();
                        break;
                }
            }
            json.endObject();
            if (worldName != null) {
                return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            }

            return null;
        }
    }
}




