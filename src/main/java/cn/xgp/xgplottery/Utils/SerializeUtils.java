package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.*;
import cn.xgp.xgplottery.XgpLottery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class SerializeUtils {

    public static int saveTaskId;

    public static void save(){
        if(!XgpLottery.lotteryList.isEmpty()){
            saveLotteryData();
        }
        saveData();
    }

    public static void load(){
        createLotteryFolder();

        loadLotteryData();
        loadData();
        saveTaskId = Bukkit.getScheduler().runTaskTimer(XgpLottery.instance, () -> {
            if(ConfigSetting.autoSaveMsg)
                XgpLottery.log("正在自动保存数据");
            save();
        }, ConfigSetting.autoSaveTime*3, ConfigSetting.autoSaveTime).getTaskId();
    }

    public static void createLotteryFolder(){
        File folder =new File(XgpLottery.instance.getDataFolder(), "Lottery");
        if(!folder.exists()){
            if(folder.mkdir()){
                XgpLottery.log("创建奖池文件夹成功");
            }
        }
    }

    public static void saveData(){
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            if(folder.mkdirs()){
                XgpLottery.log("创建数据文件夹成功");
            }
        }
        File file1 = new File(folder, "box.dat");
        try (FileWriter writer = new FileWriter(file1)) {
            writer.write(boxToJson(XgpLottery.lotteryBoxList));
        } catch (IOException e) {
            XgpLottery.log("储存文件时发生错误：" + e.getMessage());
        }
        File file2 = new File(folder, "current.dat");
        try (FileWriter writer = new FileWriter(file2)) {
            writer.write(timesToJson(XgpLottery.currentTime));
        } catch (IOException e) {
            XgpLottery.log("储存文件时发生错误：" + e.getMessage());
        }
        File file3 = new File(folder, "total.dat");
        try (FileWriter writer = new FileWriter(file3)) {
            writer.write(timesToJson(XgpLottery.totalTime));
        } catch (IOException e) {
            XgpLottery.log("储存文件时发生错误：" + e.getMessage());
        }
        File file4 = new File(folder,"all.dat");
        try (FileWriter writer = new FileWriter(file4)) {
            writer.write(timesToJson(XgpLottery.allTimes));
        } catch (IOException e) {
            XgpLottery.log("储存文件时发生错误：" + e.getMessage());
        }
    }

    public static void loadData(){
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            return;
        }
        File file1 = new File(folder, "box.dat");
        File file2 = new File(folder, "current.dat");
        File file3 = new File(folder, "total.dat");
        File file4 = new File(folder,"all.dat");
        try (BufferedReader reader = new BufferedReader(new FileReader(file1))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String jsonData = jsonBuilder.toString();
            XgpLottery.lotteryBoxList = boxFromJson(jsonData);
            if(XgpLottery.lotteryBoxList==null)
                XgpLottery.lotteryBoxList = new CopyOnWriteArrayList<>();
            XgpLottery.log("读取抽奖箱数据成功");
        }catch (Exception e) {
            XgpLottery.log("读取 JSON 文件时发生错误：" + e.getMessage());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file2))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String jsonData = jsonBuilder.toString();
            XgpLottery.currentTime = timesFromJson(jsonData);
            if(XgpLottery.currentTime==null)
                XgpLottery.currentTime = new CopyOnWriteArrayList<>();
            XgpLottery.log("读取抽奖次数数据成功");
        }catch (Exception e) {
            XgpLottery.log("读取 JSON 文件时发生错误：" + e.getMessage());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file3))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String jsonData = jsonBuilder.toString();
            XgpLottery.totalTime = timesFromJson(jsonData);
            if(XgpLottery.totalTime==null)
                XgpLottery.totalTime = new CopyOnWriteArrayList<>();
            XgpLottery.log("读取当前抽奖次数数据成功");
        }catch (Exception e) {
            XgpLottery.log("读取 JSON 文件时发生错误：" + e.getMessage());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file4))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String jsonData = jsonBuilder.toString();
            XgpLottery.allTimes = timesFromJson(jsonData);
            if(XgpLottery.allTimes==null)
                XgpLottery.allTimes = new CopyOnWriteArrayList<>();
            XgpLottery.log("读取总抽奖次数数据成功");
        }catch (Exception e) {
            XgpLottery.log("读取 JSON 文件时发生错误：" + e.getMessage());
        }

        for(LotteryBox lotteryBox :XgpLottery.lotteryBoxList){
            XgpLottery.locations.add(lotteryBox.getLocation());
        }
        BoxParticle.clearAllParticle();
    }

    public static void saveLotteryData(){
        List<LotteryNbtConverter> dataList = new ArrayList<>();
        for(Lottery lottery:XgpLottery.lotteryList.values()){
            dataList.add(new LotteryNbtConverter(lottery));
        }
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            if(folder.mkdirs()){
                XgpLottery.log("创建数据文件夹成功");
            }
        }
        File file = new File(folder, "lottery.dat");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(lotteryToJson(dataList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadLotteryData(){
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            return;
        }
        File file = new File(folder, "lottery.dat");
        if(!file.exists())
            return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String jsonData = jsonBuilder.toString();
            List<LotteryNbtConverter> dataList = lotteryFromJson(jsonData);
            for(LotteryNbtConverter lotteryNbtConverter:dataList){
                XgpLottery.lotteryList.put(lotteryNbtConverter.getName(),lotteryNbtConverter.toLottery());
            }
            XgpLottery.log("读取奖池数据成功");
        }catch (Exception e) {
            XgpLottery.log("读取 JSON 文件时发生错误：" + e.getMessage());
        }
    }




    public static String timesToJson(List<LotteryTimes> lotteryTimesList){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(lotteryTimesList);
    }
    public static List<LotteryTimes> timesFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<LotteryTimes>>() {}.getType());
    }
    public static String boxToJson(List<LotteryBox> lotteryBoxList){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .setPrettyPrinting()
                .create();
        return gson.toJson(lotteryBoxList);
    }
    public static List<LotteryBox> boxFromJson(String json){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .create();
        return gson.fromJson(json, new TypeToken<List<LotteryBox>>() {}.getType());
    }

    public static String lotteryToJson(List<LotteryNbtConverter> list){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(list);
    }
    public static List<LotteryNbtConverter> lotteryFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json,new TypeToken<List<LotteryNbtConverter>>() {}.getType());
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




