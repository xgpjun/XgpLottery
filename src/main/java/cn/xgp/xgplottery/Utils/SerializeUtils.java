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
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
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
        XgpLottery.log(LangUtils.LoadData);
        loadLotteryData();
        loadData();
        saveTaskId = Bukkit.getScheduler().runTaskTimer(XgpLottery.instance, SerializeUtils::save, ConfigSetting.autoSaveTime*3, ConfigSetting.autoSaveTime).getTaskId();
    }

    public static void saveData(){
        saveBoxData();
        if(!SqlUtils.enable){
            saveDataByFile();
        }
    }
    public static void loadData(){
        loadBoxData();
        if(!SqlUtils.enable){
            loadDataByFile();
        }
    }

    private static void loadBoxData() {
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            return;
        }
        File file = new File(folder, "box.json");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String jsonData = jsonBuilder.toString();
            XgpLottery.lotteryBoxList = boxFromJson(jsonData);
            if(XgpLottery.lotteryBoxList==null)
                XgpLottery.lotteryBoxList = new CopyOnWriteArrayList<>();

        }catch (Exception e) {
            XgpLottery.warning(LangUtils.LoadFileError + e.getMessage());
        }

        for(LotteryBox lotteryBox :XgpLottery.lotteryBoxList){
            XgpLottery.locations.add(lotteryBox.getLocation());
        }
        BoxParticle.clearAllParticle();
    }

    private static void saveBoxData(){
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            folder.mkdirs();
        }
        File file1 = new File(folder, "box.json");
        try (FileWriter writer = new FileWriter(file1)) {
            writer.write(boxToJson(XgpLottery.lotteryBoxList));
        } catch (IOException e) {
            XgpLottery.warning(LangUtils.SaveFileError + e.getMessage());
        }
    }

    private static void saveDataByFile(){
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            folder.mkdirs();
        }
        File file2 = new File(folder, "current.json");
        try (FileWriter writer = new FileWriter(file2)) {
            writer.write(timesToJson(XgpLottery.currentTime));
        } catch (IOException e) {
            XgpLottery.warning(LangUtils.SaveFileError + e.getMessage());
        }
        File file3 = new File(folder, "total.json");
        try (FileWriter writer = new FileWriter(file3)) {
            writer.write(timesToJson(XgpLottery.totalTime));
        } catch (IOException e) {
            XgpLottery.warning(LangUtils.SaveFileError + e.getMessage());
        }
        File file4 = new File(folder,"all.json");
        try (FileWriter writer = new FileWriter(file4)) {
            writer.write(timesToJson(XgpLottery.allTimes));
        } catch (IOException e) {
            XgpLottery.warning(LangUtils.SaveFileError + e.getMessage());
        }
    }

    public static void loadDataByFile(){
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            return;
        }
        File file2 = new File(folder, "current.json");
        File file3 = new File(folder, "total.json");
        File file4 = new File(folder,"all.json");


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
        }catch (Exception e) {
            XgpLottery.warning(LangUtils.LoadFileError + e.getMessage());
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

        }catch (Exception e) {
            XgpLottery.warning(LangUtils.LoadFileError + e.getMessage());
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

        }catch (Exception e) {
            XgpLottery.warning(LangUtils.LoadFileError + e.getMessage());
        }

    }

    public static void saveLotteryData(){
        if(!SqlUtils.enable){
            saveLotteryDataByFile();
        }else {
            SqlUtils.saveLottery();
        }
    }
    public static void loadLotteryData(){
        if(!SqlUtils.enable){
            loadLotteryDataByFile();
        }else {
            SqlUtils.loadLottery();
        }
    }


    private static void saveLotteryDataByFile(){
        List<LotteryNbtConverter> dataList = new ArrayList<>();
        for(Lottery lottery:XgpLottery.lotteryList.values()){
            dataList.add(new LotteryNbtConverter(lottery));
        }
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            folder.mkdirs();
        }

        File file = new File(folder, "lottery.json");
        try (OutputStream outputStream = new FileOutputStream(file);
             Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            writer.write(lotteryToJson(dataList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadLotteryDataByFile(){
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            return;
        }
        File file = new File(folder, "lottery.json");
        if(!file.exists())
            return;
        try (InputStream inputStream = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            XgpLottery.warning(LangUtils.LoadFileError + e.getMessage());
        }
    }

    public static void saveLotteryRecord(LotteryRecord lotteryRecord){
        List<String> itemToNbt = new ArrayList<>();
        for (ItemStack item : lotteryRecord.getRecord()) {
            String str = nmsUtils.toNBTString(item);
            itemToNbt.add(str);
        }
        String json = new Gson().toJson(itemToNbt);

        if(!SqlUtils.enable) {
            File folder = new File(XgpLottery.instance.getDataFolder(), "Record");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(folder, lotteryRecord.getUuid() +"_"+lotteryRecord.getLotteryName() + ".json");

            try (OutputStream outputStream = new FileOutputStream(file);
                 Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                writer.write(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            SqlUtils.updateRecord(lotteryRecord.getUuid().toString(),lotteryRecord.getLotteryName(),json);
        }

    }

    public static List<ItemStack> loadLotteryRecord(UUID uuid,String lotteryName){
        String json = null;
        if(!SqlUtils.enable) {
            File folder = new File(XgpLottery.instance.getDataFolder(), "Record");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(folder, uuid +"_"+ lotteryName + ".json");

            try (InputStream inputStream = new FileInputStream(file);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                json = jsonBuilder.toString();

            } catch (IOException ignore) {
            }
        }else {
            json = SqlUtils.getRecord(uuid.toString(),lotteryName);

        }
        if(json == null)
            return null;

        List<String> itemToNbt = new Gson().fromJson(json, new TypeToken<List<String>>() {}.getType());
        if (itemToNbt == null)
            return null;

        List<ItemStack> items = new ArrayList<>();
        for (String nbt : itemToNbt) {
            ItemStack itemStack = nmsUtils.toItem(nbt);
            items.add(itemStack);
        }
        return items;

    }


    public static String timesToJson(List<LotteryTimes> lotteryTimesList){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
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




