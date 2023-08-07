package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.LotteryTimes;
import cn.xgp.xgplottery.XgpLottery;
import cn.xgp.xgplottery.common.DatabaseManager;
import org.bukkit.Bukkit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SqlUtils {

    public static boolean enable;
    public static DatabaseManager databaseManager;
    public static void init(){
        databaseManager = new DatabaseManager();
        enable = ConfigSetting.enableDatabase;
        createTables();
    }


    public static Connection getConnection() throws SQLException {
        return databaseManager.getDataSource().getConnection();
    }

    @SuppressWarnings("SqlResolve")
    public static int getOneTimes(String timesType,String uuid,String lotteryName){
        int times = 0;
        String sql = "SELECT `times` FROM `"+timesType+"` WHERE `uuid` = ? AND `lotteryName` = ?";
        try(PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, uuid);
            statement.setString(2, lotteryName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    times = resultSet.getInt("times");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return times;
    }

    @SuppressWarnings("SqlResolve")
    public static void addTimes(String uuid, String lotteryName, String timesType){
        String selectQuery = "SELECT `times` FROM `" + timesType + "` WHERE `uuid` = ? AND `lotteryName` = ?";

        try(PreparedStatement selectStatement = getConnection().prepareStatement(selectQuery)) {
            // 先查询是否存在记录
            selectStatement.setString(1, uuid);
            selectStatement.setString(2, lotteryName);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                // 如果存在记录，则更新times值
                int times = resultSet.getInt("times") + 1;
                String updateQuery = "UPDATE `" + timesType + "` SET `times` = ? WHERE `uuid` = ? AND `lotteryName` = ?";
                try(PreparedStatement updateStatement = getConnection().prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, times);
                    updateStatement.setString(2, uuid);
                    updateStatement.setString(3, lotteryName);
                    updateStatement.executeUpdate();
                }
            } else {
                // 如果不存在记录，则插入新的数据
                String insertQuery = "INSERT INTO `" + timesType + "` (`uuid`, `lotteryName`, `times`) VALUES (?, ?, 1)";
                try(PreparedStatement insertStatement = getConnection().prepareStatement(insertQuery)) {
                    insertStatement.setString(1, uuid);
                    insertStatement.setString(2, lotteryName);
                    insertStatement.executeUpdate();
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearCurrentTimes(LotteryTimes lotteryTimes){
        String updateQuery = "UPDATE `current` SET `times` = ? WHERE `uuid` = ? AND `lotteryName` = ?";
        try(PreparedStatement updateStatement = getConnection().prepareStatement(updateQuery)) {
            updateStatement.setInt(1, 0);
            updateStatement.setString(2, String.valueOf(lotteryTimes.getUuid()));
            updateStatement.setString(3, lotteryTimes.getLotteryName());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("SqlResolve")
    public static List<LotteryTimes> getAllTimes(String timesType) {
        List<LotteryTimes> lotteryTimesList = new ArrayList<>();


        try(Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `uuid`, `times` ,`lotteryName` FROM `"+timesType+"`")) {
            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                String lotteryName = resultSet.getString("lotteryName");
                int times = resultSet.getInt("times");
                LotteryTimes lotteryTimes = new LotteryTimes(lotteryName ,UUID.fromString(uuid), times);
                lotteryTimesList.add(lotteryTimes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lotteryTimesList;
    }

    @SuppressWarnings("SqlResolve")
    public static void deleteTimes(String lotteryName,String timesType) {
        Bukkit.getAsyncScheduler().runNow(XgpLottery.instance,task ->{
            try(PreparedStatement statement = getConnection().prepareStatement("DELETE FROM `"+timesType+"` WHERE `lotteryName` = ?")) {
                statement.setString(1, lotteryName);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void updateRecord(String uuid,String lotteryName,String items){
        String selectQuery = "SELECT `items` FROM `record` WHERE `uuid` = ? AND `lotteryName` = ?";
        try(PreparedStatement selectStatement = getConnection().prepareStatement(selectQuery)) {
            // 先查询是否存在记录
            selectStatement.setString(1, uuid);
            selectStatement.setString(2, lotteryName);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // 如果存在记录，则更新times值
                String updateQuery = "UPDATE `record` SET `items` = ? WHERE `uuid` = ? AND `lotteryName` = ?";
                PreparedStatement updateStatement = getConnection().prepareStatement(updateQuery);
                updateStatement.setString(1, items);
                updateStatement.setString(2, uuid);
                updateStatement.setString(3, lotteryName);
                updateStatement.executeUpdate();
                updateStatement.close();
            } else {
                // 如果不存在记录，则插入新的数据
                String insertQuery = "INSERT INTO `record` (`uuid`, `lotteryName`, `items`) VALUES (?, ?, ?)";
                PreparedStatement insertStatement = getConnection().prepareStatement(insertQuery);
                insertStatement.setString(1, uuid);
                insertStatement.setString(2, lotteryName);
                insertStatement.setString(3,items);
                insertStatement.executeUpdate();
                insertStatement.close();
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getRecord(String uuid,String lotteryName){
        String selectQuery = "SELECT `items` FROM `record` WHERE `uuid` = ? AND `lotteryName` = ?";

        try(PreparedStatement statement = getConnection().prepareStatement(selectQuery)) {
            statement.setString(1,uuid);
            statement.setString(2,lotteryName);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()){
                    return resultSet.getString("items");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void closeConnection() {
        if(databaseManager!=null)
            databaseManager.getDataSource().close();
    }


    public static void convertToDatabase(){
        SerializeUtils.loadLotteryDataByFile();
        SerializeUtils.loadDataByFile();
        SerializeUtils.loadRewardByFile();

        SqlUtils.enable=true;
        XgpLottery.instance.getConfig().set("enableDatabase",true);
        XgpLottery.instance.saveConfig();

        //all
        init();
        try {
            String query = "INSERT INTO `all` (uuid, lotteryName, times) VALUES (?, ?, ?)";
            PreparedStatement statement = getConnection().prepareStatement(query);

            for (LotteryTimes lotteryTimes : XgpLottery.allTimes) {
                statement.setString(1, lotteryTimes.getUuid().toString());
                statement.setString(2, "all");
                statement.setInt(3, lotteryTimes.getTimes());
                statement.addBatch(); // 添加到批处理中
            }

            statement.executeBatch(); // 执行批处理

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //current
        try {
            String query = "INSERT INTO `current` (uuid, lotteryName, times) VALUES (?, ?, ?)";
            PreparedStatement statement = getConnection().prepareStatement(query);

            for (LotteryTimes lotteryTimes : XgpLottery.currentTime) {
                statement.setString(1, lotteryTimes.getUuid().toString());
                statement.setString(2, lotteryTimes.getLotteryName());
                statement.setInt(3, lotteryTimes.getTimes());
                statement.addBatch(); // 添加到批处理中
            }

            statement.executeBatch(); // 执行批处理
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //total
        try {
            String query = "INSERT INTO `total` (uuid, lotteryName, times) VALUES (?, ?, ?)";
            PreparedStatement statement = getConnection().prepareStatement(query);

            for (LotteryTimes lotteryTimes : XgpLottery.totalTime) {
                statement.setString(1, lotteryTimes.getUuid().toString());
                statement.setString(2, lotteryTimes.getLotteryName());
                statement.setInt(3, lotteryTimes.getTimes());
                statement.addBatch(); // 添加到批处理中
            }

            statement.executeBatch(); // 执行批处理

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //lottery
        try {
            String query = "INSERT INTO `lottery` (lotteryData) VALUES (?)";
            PreparedStatement statement = getConnection().prepareStatement(query);;
            statement.setString(1,SerializeUtils.lotteryToJson(new ArrayList<>(XgpLottery.lotteryList.values())));
            statement.execute();

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //rewards
        try {
            String query = "INSERT INTO `rewards` (`rewardData`) VALUES (?)";
            PreparedStatement statement = getConnection().prepareStatement(query);;
            statement.setString(1,SerializeUtils.rewardToJson(XgpLottery.rewards));
            statement.execute();

            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        //reward

        try {
            String query = "INSERT INTO `reward` (uuid, lotteryName, times) VALUES (?, ?, ?)";
            PreparedStatement statement = getConnection().prepareStatement(query);

            for (LotteryTimes lotteryTimes : XgpLottery.rewardsTimes) {
                statement.setString(1, lotteryTimes.getUuid().toString());
                statement.setString(2, lotteryTimes.getLotteryName());
                statement.setInt(3, lotteryTimes.getTimes());
                statement.addBatch(); // 添加到批处理中
            }
            statement.executeBatch(); // 执行批处理

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SerializeUtils.loadLotteryData();
        SerializeUtils.loadData();
        SerializeUtils.loadRewardData();

        File folder = new File(XgpLottery.instance.getDataFolder(), "Record");
        File[] files = folder.listFiles();

        String insertQuery = "INSERT INTO `record` (`uuid`, `lotteryName`, `items`) VALUES (?, ?, ?)";

        if (files != null) {
            try (PreparedStatement insertStatement = getConnection().prepareStatement(insertQuery)) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".json")) {
                        String fileName = file.getName();
                        String uuid = fileName.split("_")[0];
                        String lotteryName = fileName.split("_")[1].split("\\.")[0];

                        String items = readFile(file);
                        insertStatement.setString(1, uuid);
                        insertStatement.setString(2, lotteryName);
                        insertStatement.setString(3, items);
                        insertStatement.addBatch(); // 添加到批处理中
                    }
                }
                insertStatement.executeBatch(); // 执行批处理
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void convertToFile(){
        init();
        //all

        String allQuery = "SELECT `uuid`, `times`, `lotteryName` FROM `all`";
        try(Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(allQuery)) {
            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                int times = resultSet.getInt("times");
                String lotteryName = resultSet.getString("lotteryName");
                XgpLottery.allTimes.add(new LotteryTimes(lotteryName,UUID.fromString(uuid),times));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //current

        String currentQuery = "SELECT `uuid`, `times`, `lotteryName` FROM `current`";
        try(Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(currentQuery)) {
            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                int times = resultSet.getInt("times");
                String lotteryName = resultSet.getString("lotteryName");
                XgpLottery.currentTime.add(new LotteryTimes(lotteryName,UUID.fromString(uuid),times));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //total
        String totalQuery = "SELECT `uuid`, `times`, `lotteryName` FROM `total`";
        try(Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(totalQuery)) {
            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                int times = resultSet.getInt("times");
                String lotteryName = resultSet.getString("lotteryName");
                XgpLottery.totalTime.add(new LotteryTimes(lotteryName,UUID.fromString(uuid),times));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SerializeUtils.saveDataByFile();

        //rewards

        try {
            String query = "SELECT rewardData FROM rewards";
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String json = resultSet.getString("rewardData");

                File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
                if(!folder.exists()){
                    folder.mkdirs();
                }
                File file = new File(folder, "RewardGifts.json");
                try (OutputStream outputStream = Files.newOutputStream(file.toPath());
                     Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                    writer.write(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //reward
        String rewardQuery = "SELECT `uuid`, `times`, `lotteryName` FROM `reward`";
        try(Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(rewardQuery)) {
            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                int times = resultSet.getInt("times");
                String lotteryName = resultSet.getString("lotteryName");
                XgpLottery.rewardsTimes.add(new LotteryTimes(lotteryName,UUID.fromString(uuid),times));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //lottery
        String lotteryQuery = "SELECT `lotteryData` FROM `lottery`";
        try(Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(lotteryQuery)) {
            if(resultSet.next()) {
                String json = resultSet.getString("lotteryData");
                File folder = new File(XgpLottery.instance.getDataFolder(), "Data");
                if(!folder.exists()){
                    folder.mkdirs();
                }
                File file = new File(folder, "lottery.json");
                try (OutputStream outputStream = Files.newOutputStream(file.toPath());
                     Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                    writer.write(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //record

        String recordQuery = "SELECT `uuid`, `items`, `lotteryName` FROM `record`";
        try(Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(recordQuery)) {
            File folder = new File(XgpLottery.instance.getDataFolder(), "Record");
            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                String items = resultSet.getString("items");
                String lotteryName = resultSet.getString("lotteryName");
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File file = new File(folder, uuid +"_"+lotteryName + ".json");
                try (OutputStream outputStream = new FileOutputStream(file);
                     Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                    writer.write(items);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static String readFile(File file) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        try (InputStream inputStream = Files.newInputStream(file.toPath());
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }
        return jsonBuilder.toString();
    }

    public static void loadLottery(){
        String query = "SELECT lotteryData FROM lottery";

        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String lotteryData = resultSet.getString("lotteryData");
                XgpLottery.lotteryList = SerializeUtils.lotteryFromJson_new(lotteryData);
                XgpLottery.log(LangUtils.LoadLotteryData);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void saveLottery(){

        String json = SerializeUtils.lotteryToJson(new ArrayList<>(XgpLottery.lotteryList.values()));
        try {
            // 先查询是否存在记录
            String selectQuery = "SELECT `lotteryData` FROM lottery";
            PreparedStatement selectStatement = getConnection().prepareStatement(selectQuery);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String UpdateQuery = "UPDATE lottery SET lotteryData = ?";
                PreparedStatement statement = getConnection().prepareStatement(UpdateQuery);
                statement.setString(1,json);
                statement.executeUpdate();
                statement.close();

            } else {
                // 如果不存在记录，则插入新的数据
                String insertQuery = "INSERT INTO `lottery` (`lotteryData`) VALUES (?)";
                PreparedStatement insertStatement = getConnection().prepareStatement(insertQuery);
                insertStatement.setString(1, json);
                insertStatement.executeUpdate();
                insertStatement.close();
            }
            resultSet.close();
            selectStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadReward(){
        String query = "SELECT rewardData FROM rewards";
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String rewardData = resultSet.getString("rewardData");
                XgpLottery.rewards = SerializeUtils.rewardFromJson(rewardData);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void saveReward(){
        String json = SerializeUtils.rewardToJson(XgpLottery.rewards);
        try {
            // 先查询是否存在记录
            String selectQuery = "SELECT `rewardData` FROM rewards";
            PreparedStatement selectStatement = getConnection().prepareStatement(selectQuery);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String UpdateQuery = "UPDATE rewards SET rewardData = ?";
                PreparedStatement statement = getConnection().prepareStatement(UpdateQuery);
                statement.setString(1,json);
                statement.executeUpdate();
                statement.close();

            } else {
                // 如果不存在记录，则插入新的数据
                String insertQuery = "INSERT INTO `rewards` (`rewardData`) VALUES (?)";
                PreparedStatement insertStatement = getConnection().prepareStatement(insertQuery);
                insertStatement.setString(1, json);
                insertStatement.executeUpdate();
                insertStatement.close();
            }
            resultSet.close();
            selectStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables() {
        try (Statement statement = getConnection().createStatement()) {
            // 创建表的 SQL 语句
            String sql1 = "CREATE TABLE IF NOT EXISTS `all` (uuid TEXT NULL, times INT NULL, lotteryName TEXT NULL)";
            String sql2 = "CREATE TABLE IF NOT EXISTS `current` (uuid TEXT NULL, lotteryName TEXT NULL, times INT NULL)";
            String sql3 = "CREATE TABLE IF NOT EXISTS `lottery` (lotteryData LONGTEXT NULL)";
            String sql4 = "CREATE TABLE IF NOT EXISTS `record` (uuid TEXT NULL, lotteryName TEXT NULL, items LONGTEXT NULL)";
            String sql5 = "CREATE TABLE IF NOT EXISTS `total` (uuid TEXT NULL, lotteryName TEXT NULL, times INT NULL)";
            String sql6 = "CREATE TABLE IF NOT EXISTS `reward` (uuid TEXT NULL, lotteryName TEXT NULL, times INT NULL)";
            String sql7 = "create table if not exists `rewards` (rewardData text null)";

            statement.executeUpdate(sql1);
            statement.executeUpdate(sql2);
            statement.executeUpdate(sql3);
            statement.executeUpdate(sql4);
            statement.executeUpdate(sql5);
            statement.executeUpdate(sql6);
            statement.executeUpdate(sql7);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
