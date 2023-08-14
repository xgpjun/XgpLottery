package cn.xgp.xgplottery.common;

import cn.xgp.xgplottery.Utils.ConfigSetting;
import cn.xgp.xgplottery.XgpLottery;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DatabaseManager {
    @Getter
    private HikariDataSource dataSource;
    public DatabaseManager(){
        HikariConfig config = new HikariConfig();
        if(ConfigSetting.enableDatabase){
            File configFile = new File(XgpLottery.instance.getDataFolder(), "database.yml");
            FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(configFile);
            String url ="jdbc:mysql://" + dataConfig.getString("url") + "/" + dataConfig.getString("database")+"?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
            config.setJdbcUrl(url);
            config.setDriverClassName(dataConfig.getString("driver"));
            config.setUsername(dataConfig.getString("username"));
            config.setPassword(dataConfig.getString("password"));
        }else {
            String pluginFolder = XgpLottery.instance.getDataFolder().getAbsolutePath();
            String databasePath = pluginFolder + File.separator + "data.db";
            if(!ConfigSetting.sqlite.equals(""))
                databasePath=ConfigSetting.sqlite;
            config.setJdbcUrl("jdbc:sqlite:" + databasePath);
        }


        config.setMaximumPoolSize(500);
        config.setMinimumIdle(5);
        config.setIdleTimeout(60000);
        config.setConnectionTimeout(30000);
        config.setValidationTimeout(3000);
        config.setMaxLifetime(60000);
        dataSource = new HikariDataSource(config);
    }
}
