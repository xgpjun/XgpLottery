package cn.xgp.xgplottery.common;

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
        File configFile = new File(XgpLottery.instance.getDataFolder(), "database.yml");
        FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(configFile);
        String url ="jdbc:mysql://" + dataConfig.getString("mysql.url") + "/" + dataConfig.getString("database")+"?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        config.setJdbcUrl(url);
        config.setDriverClassName(dataConfig.getString("driver"));
        config.setUsername(dataConfig.getString("username"));
        config.setPassword(dataConfig.getString("password"));
        config.setMaximumPoolSize(50);
        config.setMinimumIdle(5);

        dataSource = new HikariDataSource(config);
    }
}
