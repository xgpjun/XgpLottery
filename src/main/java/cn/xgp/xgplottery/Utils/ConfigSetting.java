package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.XgpLottery;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.configuration.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ConfigSetting {
    public static String msg;
    static String latestVersion;
    //检查为旧版本自动赋为true
    public static boolean msgToAdmin;
    public static String version;

    public static boolean enableDatabase;
    public static boolean showProbability;
    public static boolean enableParticle;
    public static boolean giveLottery;
    public static boolean giveKey;
    public static boolean shop;
    public static long autoSaveTime;
    public static boolean autoSaveMsg;
    public static long autoUpdateTopTime;
    public static boolean broadcast;
    public static int recordAmount;
    private final static String pluginVersion = XgpLottery.instance.getDescription().getVersion();

    public static int versionToInt;
    public static int pluginVersionToInt = getVersionToInt(pluginVersion);


    public static void loadConfig(Configuration config){
        version = config.getString("version");
        enableDatabase = config.getBoolean("enableDatabase",false); //1.1.0新增
        showProbability = config.getBoolean("showProbability",false);
        enableParticle = config.getBoolean("enableParticle",true);
        giveLottery = config.getBoolean("giveLottery",true);
        giveKey = config.getBoolean("giveKey",false);
        shop = config.getBoolean("shop",true);
        autoSaveTime = config.getLong("autoSaveTime",120L)*20;
        autoUpdateTopTime = config.getLong("autoUpdateTopTime",120L)*20;
        autoSaveMsg = config.getBoolean("autoSaveMsg",true);
        broadcast = config.getBoolean("broadcast",true);
        recordAmount = config.getInt("recordAmount",500); //1.1.0新增
        LangUtils.loadLangFile(config.getString("lang","zh_CN.yml"));

        versionToInt= getVersionToInt(version);

    }

    public static void updateConfig(){

        if(versionToInt<110){
            version = "1.1.0";
            enableDatabase = false;
            XgpLottery.instance.getConfig().set("version",version);
            XgpLottery.instance.getConfig().set("enableDatabase",false);
            XgpLottery.instance.getConfig().set("lang", "zh_CN.yml");
            XgpLottery.instance.saveConfig();
            XgpLottery.instance.saveResource("lang\\zh_CN.yml", true);
        }
        if (versionToInt < 120) {
            BackupUtils.backup();
            version = "1.2.0";
            XgpLottery.instance.getConfig().set("version", version);
            XgpLottery.instance.saveConfig();
        }
        if (versionToInt < 125) {
            version = pluginVersion;
            XgpLottery.instance.getConfig().set("version", version);
            XgpLottery.instance.saveConfig();
        }
        msg = getXgpWebsite("msg");

        try {
            XgpLottery.log(LangUtils.GetVersion);
            latestVersion = getLatestVersion();
        } catch (IOException e) {
            XgpLottery.warning("Failed to get GitHub-Api.");
            latestVersion = getXgpWebsite("version");
        } finally {
            checkOutdated(latestVersion);
        }

    }

    private static void checkOutdated(String latestVersion) {
        XgpLottery.log(LangUtils.LatestVersion+latestVersion);
        if(pluginVersionToInt==getVersionToInt(latestVersion)){
            XgpLottery.log(LangUtils.LatestVersionMsg);
        }
        if(pluginVersionToInt<getVersionToInt(latestVersion)){
            XgpLottery.warning(LangUtils.OutdatedMsg);
            msgToAdmin = true;
            XgpLottery.warning(LangUtils.LatestVersionUrlMsg+"https://www.mcbbs.net/thread-1445345-1-1.html");

        }
    }

    static String getLatestVersion() throws IOException {
        String GITHUB_API_URL = "https://api.github.com/repos/xgpjun/XgpLottery/releases/latest";
        URL url = new URL(GITHUB_API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Accept", "application/vnd.github.v3+json");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getStr(connection,"tag_name");
        } else {
            throw new IOException("Failed to retrieve latest version. Response code: " + responseCode);
        }

    }

    static String getXgpWebsite(String key){
        String apiUrl = "https://xgpjun.cn/api/xl/latest";
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return getStr(connection,key);
            }
        } catch (IOException e) {
            XgpLottery.warning("fail to connect xgp website!");
            e.printStackTrace();
            msg = "null";
        }
        return "0.0.0";
    }

    private static String getStr(HttpURLConnection connection,String key) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        JsonParser jsonParser = new JsonParser();
        JsonObject json = jsonParser.parse(response.toString()).getAsJsonObject();
        return json.get(key).toString().replace("\"","");
    }

    static int getVersionToInt(String version){
        return Integer.parseInt(version.split("\\.")[0])*100+Integer.parseInt(version.split("\\.")[1])*10+Integer.parseInt(version.split("\\.")[2]);
    }

}
