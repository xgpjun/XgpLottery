package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.*;
import cn.xgp.xgplottery.XgpLottery;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SerializeUtils {

    public static int saveTaskId;

    public static void save(){
        if(!XgpLottery.lotteryList.isEmpty()){
            saveLotteryData();
        }
        saveData();
    }

    public static void load() {
        XgpLottery.log(LangUtils.LoadData);
        loadLotteryData();
        loadData();
        if (ConfigSetting.autoSaveTime >= 0)
            saveTaskId = Bukkit.getScheduler().runTaskTimer(XgpLottery.instance, SerializeUtils::save, ConfigSetting.autoSaveTime * 3, ConfigSetting.autoSaveTime).getTaskId();
    }

    public static void saveData(){
        if(!SqlUtils.enable){
            saveDataByFile();
            saveRewardByFile();
        }
    }
    public static void loadData(){
        loadBoxData();
        if(!SqlUtils.enable){
            loadDataByFile();
            loadRewardByFile();
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


    public static void saveRewardData(){
        if(!SqlUtils.enable){
            saveRewardByFile();
        }else {
            SqlUtils.saveReward();
        }
    }
    public static void loadRewardData(){
        if(!SqlUtils.enable){
            loadRewardByFile();
        }else {
            SqlUtils.loadReward();
        }
    }


    public static void saveRewardByFile(){
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            folder.mkdirs();
        }
        File file = new File(folder, "RewardGifts.json");
        try (OutputStream outputStream = Files.newOutputStream(file.toPath());
             Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            String json = rewardToJson(XgpLottery.rewards);
            writer.write(json);
        } catch (IOException e) {
            XgpLottery.warning(LangUtils.SaveFileError + e.getMessage());
        }
    }

    public static void loadRewardByFile(){
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            return;
        }
        File file = new File(folder, "RewardGifts.json");
        if(file.exists()){
            try (InputStream inputStream = Files.newInputStream(file.toPath());
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                String jsonData = jsonBuilder.toString();
                XgpLottery.rewards = rewardFromJson(jsonData);
            }  catch (Exception e) {
                XgpLottery.warning(LangUtils.LoadFileError + e.getMessage());
            }
        }else {
            XgpLottery.rewards = new ArrayList<>();
        }
    }


    private static void loadBoxData() {
        if(XgpLottery.lotteryBoxList!=null&&!XgpLottery.lotteryBoxList.isEmpty()){
            return;
        }
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            return;
        }
        File file = new File(folder, "box.json");
        if(file.exists()){
            try (InputStream inputStream = new FileInputStream(file)){
                BufferedReader reader;
                if(ConfigSetting.versionToInt<120){
                    reader = new BufferedReader(new FileReader(file));
                }else {
                    reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                }

                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                reader.close();
                String jsonData = jsonBuilder.toString();
                XgpLottery.lotteryBoxList = boxFromJson(jsonData);
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                XgpLottery.warning(LangUtils.LoadFileError + e.getMessage());
            }
        }else {
            XgpLottery.lotteryBoxList = new CopyOnWriteArrayList<>();
        }
        for(LotteryBox lotteryBox :XgpLottery.lotteryBoxList){
            XgpLottery.locations.add(lotteryBox.getLocation());
        }
    }

    public static void saveBoxData() {
        if (XgpLottery.lotteryBoxList.isEmpty()) {
            return;
        }

        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file1 = new File(folder, "box.json");
        try (OutputStream outputStream = Files.newOutputStream(file1.toPath());
             Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            writer.write(boxToJson(XgpLottery.lotteryBoxList));
        } catch (IOException e) {
            XgpLottery.warning(LangUtils.SaveFileError + e.getMessage());
        }
    }

    public static void saveDataByFile(){
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            folder.mkdirs();
        }
        File file2 = new File(folder, "current.json");
        saveTimesByFile(file2,XgpLottery.currentTime);
        File file3 = new File(folder, "total.json");
        saveTimesByFile(file3,XgpLottery.totalTime);
        File file4 = new File(folder,"all.json");
        saveTimesByFile(file4,XgpLottery.allTimes);
        File file5 = new File(folder,"reward.json");
        saveTimesByFile(file5,XgpLottery.rewardsTimes);
    }



    public static void loadDataByFile(){
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            return;
        }
        File file2 = new File(folder, "current.json");
        File file3 = new File(folder, "total.json");
        File file4 = new File(folder,"all.json");
        File file5 = new File(folder,"reward.json");
        XgpLottery.currentTime = loadTimesByFile(file2);
        XgpLottery.totalTime = loadTimesByFile(file3);
        XgpLottery.allTimes = loadTimesByFile(file4);
        XgpLottery.rewardsTimes = loadTimesByFile(file5);
    }

    private static void saveTimesByFile(File file,List<LotteryTimes> list){
        try (OutputStream outputStream = Files.newOutputStream(file.toPath());
             Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            writer.write(timesToJson(list));
        } catch (IOException e) {
            XgpLottery.warning(LangUtils.SaveFileError + e.getMessage());
        }
    }
    private static List<LotteryTimes> loadTimesByFile(File file){
        if(file.exists()){
            try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                BufferedReader reader;
                if(ConfigSetting.versionToInt<120){
                    reader = new BufferedReader(new FileReader(file));
                }else {
                    reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                }
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                reader.close();
                String jsonData = jsonBuilder.toString();
                return timesFromJson(jsonData);
            }  catch (Exception e) {
                XgpLottery.warning(LangUtils.LoadFileError + e.getMessage());
            }
        }
        return new CopyOnWriteArrayList<>();

    }


    private static void saveLotteryDataByFile(){
        File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
        if(!folder.exists()){
            folder.mkdirs();
        }

        File file = new File(folder, "lottery.json");
        try (OutputStream outputStream = Files.newOutputStream(file.toPath());
             Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            writer.write(lotteryToJson(new ArrayList<>(XgpLottery.lotteryList.values())));
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
        if(!file.exists()){
            XgpLottery.lotteryList = new ConcurrentHashMap<>();
            return;
        }

        try (InputStream inputStream = Files.newInputStream(file.toPath());
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            //TODO 1.20+会更改，此处为了不丢失以前的版本的数据
            String jsonData = jsonBuilder.toString();
            if(ConfigSetting.versionToInt<120){
                List<LotteryNbtConverter> dataList = lotteryFromJson(jsonData);
                for(LotteryNbtConverter lotteryNbtConverter:dataList){
                    XgpLottery.lotteryList.put(lotteryNbtConverter.getName(),lotteryNbtConverter.toLottery());
                }
            } else {
                XgpLottery.lotteryList = lotteryFromJson_new(jsonData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            XgpLottery.warning(LangUtils.LoadFileError + e.getMessage());
        }
    }

    public static void saveLotteryRecord(LotteryRecord lotteryRecord){
        List<String> itemToNbt = new ArrayList<>();
        for (ItemStack item : lotteryRecord.getRecord()) {
            String str = NMSUtils.toNBTString(item);
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
                 Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
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
            ItemStack itemStack = NMSUtils.toItem(nbt);
            if(itemStack.getItemMeta() ==null)
                itemStack = MyItem.getMissingItem();
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
        List<LotteryTimes> list = gson.fromJson(json, new TypeToken<List<LotteryTimes>>() {}.getType());
        return new CopyOnWriteArrayList<>(list);
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

    public static String lotteryToJson(List<Lottery> list){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ItemStack.class, new ItemAdapter())
                .registerTypeAdapter(Award.class, new AwardAdapter())
                .create();
        return gson.toJson(list);
    }

    public static List<LotteryNbtConverter> lotteryFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json,new TypeToken<List<LotteryNbtConverter>>() {}.getType());
    }
    public static Map<String,Lottery> lotteryFromJson_new(String json){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ItemStack.class, new ItemAdapter())
                .registerTypeAdapter(Award.class, new AwardAdapter())
                .create();
        List<Lottery> list = gson.fromJson(json,new TypeToken<List<Lottery>>() {}.getType());
        Map<String,Lottery> map = new ConcurrentHashMap<>();
        for (Lottery lottery: list){
            map.put(lottery.getName(),lottery);
        }
        return map;
    }


    public static String rewardToJson(List<CumulativeRewards> rewards){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ItemStack.class, new ItemAdapter())
                .registerTypeAdapter(Award.class, new AwardAdapter())
                .create();
        return gson.toJson(rewards);
    }

    public static List<CumulativeRewards> rewardFromJson(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ItemStack.class, new ItemAdapter())
                .registerTypeAdapter(Award.class, new AwardAdapter())
                .create();
        List<CumulativeRewards> list = gson.fromJson(json, new TypeToken<List<CumulativeRewards>>() {
        }.getType());
        return new CopyOnWriteArrayList<>(list);
    }

    public static void bcSaveSignal() {
        //For bc
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");

        out.writeUTF("ALL");//服务器
        out.writeUTF("XgpLotterySave");//频道
        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        try {
            String msg = "NeedToReload";
            msgOut.writeUTF(msg);
            out.writeShort(msgBytes.toByteArray().length);
            out.write(msgBytes.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (player != null) {
            player.sendPluginMessage(XgpLottery.instance, "BungeeCord", out.toByteArray());
        }
    }


    static class LocationAdapter extends TypeAdapter<Location> {
        @Override
        public void write(JsonWriter json, Location location) throws IOException {
            if (location == null || location.getWorld() == null) {
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

    static class ItemAdapter extends TypeAdapter<ItemStack>{

        @Override
        public void write(JsonWriter json, ItemStack item) throws IOException {
            if(item == null){
                json.nullValue();
                return;
            }
            json.beginObject();
            json.name("item").value(NMSUtils.toNBTString(item));
            json.endObject();

        }

        @Override
        public ItemStack read(JsonReader json) throws IOException {
            if (json.peek() == null) {
                return null;
            }
            json.beginObject();

            ItemStack item = null;
            while (json.hasNext()) {
                String name = json.nextName();
                if ("item".equals(name)) {
                    item = NMSUtils.toItem(json.nextString());
                } else {
                    json.skipValue();
                }
            }
            json.endObject();
            if(item==null){
                item = new ItemStack(Material.STONE);
            }
            return item;
        }
    }
    static class AwardAdapter extends TypeAdapter<Award>{

        @Override
        public void write(JsonWriter json, Award award) throws IOException {
            if(award == null){
                json.nullValue();
                return;
            }
            json.beginObject();
            json.name("item").value(NMSUtils.toNBTString(award.getItem()));
            json.name("weight").value(award.getWeight());
            json.name("commands").value(new Gson().toJson(award.getCommands()));
            json.name("giveItem").value(award.isGiveItem());
            json.name("executeCommands").value(award.isExecuteCommands());
            json.name("broadCast").value(award.isBroadCast());
            json.name("displayName").value(award.getDisplayName());
            json.name("isShowRaw").value(award.isShowRaw());
            json.endObject();

        }

        @Override
        public Award read(JsonReader json) throws IOException {
            if (json.peek() == null) {
                return null;
            }
            json.beginObject();
            ItemStack item =null;
            int weight = 1;
            List<String> commands = new ArrayList<>();
            boolean giveItem = true, executeCommands = false, broadCast = false, isShowRaw = false;
            String displayName = null;

            while (json.hasNext()) {
                String name = json.nextName();
                switch (name) {
                    case "item":
                        ItemStack itemStack = NMSUtils.toItem(json.nextString());
                        if(itemStack.getItemMeta()==null)
                            itemStack = MyItem.getMissingItem();
                        item = itemStack;
                        break;
                    case "weight":
                        weight = json.nextInt();
                        break;
                    case "commands":
                        commands = new Gson().fromJson(json.nextString(),new TypeToken<List<String>>(){}.getType());
                        break;
                    case "giveItem":
                        giveItem = json.nextBoolean();
                        break;
                    case "executeCommands":
                        executeCommands = json.nextBoolean();
                        break;
                    case "broadCast":
                        broadCast = json.nextBoolean();
                        break;
                    case "displayName":
                        displayName = json.nextString();
                        break;
                    case "isShowRaw":
                        isShowRaw = json.nextBoolean();
                        break;
                    default:
                        json.skipValue();
                        break;
                }
            }
            json.endObject();
            if(item==null){
                item = new ItemStack(Material.STONE);
            }
            return new Award(item, weight, commands, giveItem, executeCommands, broadCast, displayName, isShowRaw);
        }
    }

}



