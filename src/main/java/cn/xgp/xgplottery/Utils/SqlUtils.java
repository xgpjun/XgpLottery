package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryNbtConverter;
import cn.xgp.xgplottery.Lottery.LotteryTimes;
import cn.xgp.xgplottery.XgpLottery;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SqlUtils {

    public static boolean enable;
    public static String url;
    public static String driver;
    public static String database;
    public static String username;
    public static String password;
    private static Connection connection;

    public static void getConnection() {
        File LangConfigFile = new File(XgpLottery.instance.getDataFolder(),"database.yml");
        FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(LangConfigFile);
        enable = ConfigSetting.enableDatabase;
        url = dataConfig.getString("url");
        driver = dataConfig.getString("driver");
        database = dataConfig.getString("database");
        username = dataConfig.getString("username");
        password = dataConfig.getString("password");

        String jdbcUrl = "jdbc:mysql://" + url + "/" + database+"?useSSL=false";

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        createTables();
    }

    @SuppressWarnings("SqlResolve")
    public static int getOneTimes(String timesType,String uuid,String lotteryName){
        int times = 0;
        try {
            String sql = "SELECT `times` FROM `"+timesType+"` WHERE `uuid` = ? AND `lotteryName` = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);
            statement.setString(2, lotteryName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    times = resultSet.getInt("times");
                }
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return times;
    }

    @SuppressWarnings("SqlResolve")
    public static void addTimes(String uuid, String lotteryName, String timesType){
        try {
            // 先查询是否存在记录
            String selectQuery = "SELECT `times` FROM `" + timesType + "` WHERE `uuid` = ? AND `lotteryName` = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, uuid);
            selectStatement.setString(2, lotteryName);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // 如果存在记录，则更新times值
                int times = resultSet.getInt("times") + 1;
                String updateQuery = "UPDATE `" + timesType + "` SET `times` = ? WHERE `uuid` = ? AND `lotteryName` = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, times);
                updateStatement.setString(2, uuid);
                updateStatement.setString(3, lotteryName);
                updateStatement.executeUpdate();
                updateStatement.close();
            } else {
                // 如果不存在记录，则插入新的数据
                String insertQuery = "INSERT INTO `" + timesType + "` (`uuid`, `lotteryName`, `times`) VALUES (?, ?, 1)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, uuid);
                insertStatement.setString(2, lotteryName);
                insertStatement.executeUpdate();
                insertStatement.close();
            }

            resultSet.close();
            selectStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearCurrentTimes(LotteryTimes lotteryTimes){
        String updateQuery = "UPDATE `current` SET `times` = ? WHERE `uuid` = ? AND `lotteryName` = ?";
        try {
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setInt(1, 0);
            updateStatement.setString(2, String.valueOf(lotteryTimes.getUuid()));
            updateStatement.setString(3, lotteryTimes.getLotteryName());
            updateStatement.executeUpdate();
            updateStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("SqlResolve")
    public static List<LotteryTimes> getAllTimes(String timesType) {
        List<LotteryTimes> lotteryTimesList = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `uuid`, `times` ,`lotteryName` FROM `"+timesType+"`");

            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                String lotteryName = resultSet.getString("lotteryName");


                int times = resultSet.getInt("times");
                LotteryTimes lotteryTimes = new LotteryTimes(lotteryName ,UUID.fromString(uuid), times);
                lotteryTimesList.add(lotteryTimes);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance,()->{
        });
        return lotteryTimesList;
    }

    @SuppressWarnings("SqlResolve")
    public static void deleteTimes(String lotteryName,String timesType) {
        Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance,()->{
            try {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM `"+timesType+"` WHERE `lotteryName` = ?");
                statement.setString(1, lotteryName);
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void updateRecord(String uuid,String lotteryName,String items){
        try {
            // 先查询是否存在记录
            String selectQuery = "SELECT `items` FROM `record` WHERE `uuid` = ? AND `lotteryName` = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, uuid);
            selectStatement.setString(2, lotteryName);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // 如果存在记录，则更新times值
                String updateQuery = "UPDATE `record` SET `items` = ? WHERE `uuid` = ? AND `lotteryName` = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, items);
                updateStatement.setString(2, uuid);
                updateStatement.setString(3, lotteryName);
                updateStatement.executeUpdate();
                updateStatement.close();
            } else {
                // 如果不存在记录，则插入新的数据
                String insertQuery = "INSERT INTO `record` (`uuid`, `lotteryName`, `items`) VALUES (?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, uuid);
                insertStatement.setString(2, lotteryName);
                insertStatement.setString(3,items);
                insertStatement.executeUpdate();
                insertStatement.close();
            }
            resultSet.close();
            selectStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getRecord(String uuid,String lotteryName){
        String selectQuery = "SELECT `items` FROM `record` WHERE `uuid` = ? AND `lotteryName` = ?";

        try(PreparedStatement statement = connection.prepareStatement(selectQuery)) {
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
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void convertToDatabase(){
        SerializeUtils.loadLotteryDataByFile();
        SerializeUtils.loadDataByFile();

        SqlUtils.enable=true;
        XgpLottery.instance.getConfig().set("enableDatabase",true);
        XgpLottery.instance.saveConfig();

        //all
        getConnection();
        try {
            String query = "INSERT INTO `all` (uuid, lotteryName, times) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

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
            PreparedStatement statement = connection.prepareStatement(query);

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

        try {
            String query = "INSERT INTO `total` (uuid, lotteryName, times) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

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
        try {
            String query = "INSERT INTO `lottery` (lotteryData) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);

            List<LotteryNbtConverter> dataList = new ArrayList<>();
            for(Lottery lottery:XgpLottery.lotteryList.values()){
                dataList.add(new LotteryNbtConverter(lottery));
            }
            statement.setString(1,SerializeUtils.lotteryToJson(dataList));
            statement.execute();

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SerializeUtils.loadLotteryData();
        SerializeUtils.loadData();

        File folder = new File(XgpLottery.instance.getDataFolder(), "Record");
        File[] files = folder.listFiles();

        String insertQuery = "INSERT INTO `record` (`uuid`, `lotteryName`, `items`) VALUES (?, ?, ?)";

        if (files != null) {
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
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

    private static String readFile(File file) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        try (InputStream inputStream = new FileInputStream(file);
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
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String lotteryData = resultSet.getString("lotteryData");
                List<LotteryNbtConverter> dataList = SerializeUtils.lotteryFromJson(lotteryData);
                for(LotteryNbtConverter lotteryNbtConverter:dataList){
                    XgpLottery.lotteryList.put(lotteryNbtConverter.getName(),lotteryNbtConverter.toLottery());
                }
                XgpLottery.log(LangUtils.LoadLotteryData);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void saveLottery(){
        List<LotteryNbtConverter> dataList = new ArrayList<>();
        for(Lottery lottery:XgpLottery.lotteryList.values()){
            dataList.add(new LotteryNbtConverter(lottery));
        }
        String json = SerializeUtils.lotteryToJson(dataList);
        try {
            // 先查询是否存在记录
            String selectQuery = "SELECT `lotteryData` FROM lottery";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String UpdateQuery = "UPDATE lottery SET lotteryData = ?";
                PreparedStatement statement = connection.prepareStatement(UpdateQuery);
                statement.setString(1,json);
                statement.executeUpdate();
                statement.close();

            } else {
                // 如果不存在记录，则插入新的数据
                String insertQuery = "INSERT INTO `lottery` (`lotteryData`) VALUES (?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
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
        try (Statement statement = connection.createStatement()) {
            // 创建表的 SQL 语句
            String sql1 = "CREATE TABLE IF NOT EXISTS `all` (uuid TEXT NULL, times INT NULL, lotteryName TEXT NULL)";
            String sql2 = "CREATE TABLE IF NOT EXISTS `current` (uuid TEXT NULL, lotteryName TEXT NULL, times INT NULL)";
            String sql3 = "CREATE TABLE IF NOT EXISTS `lottery` (lotteryData LONGTEXT NULL)";
            String sql4 = "CREATE TABLE IF NOT EXISTS `record` (uuid TEXT NULL, lotteryName TEXT NULL, items LONGTEXT NULL)";
            String sql5 = "CREATE TABLE IF NOT EXISTS `total` (uuid TEXT NULL, lotteryName TEXT NULL, times INT NULL)";

            statement.executeUpdate(sql1);
            statement.executeUpdate(sql2);
            statement.executeUpdate(sql3);
            statement.executeUpdate(sql4);
            statement.executeUpdate(sql5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
