package cn.xgp.xgplottery;

import cn.xgp.xgplottery.Command.GuiCommand;
import cn.xgp.xgplottery.Listener.GuiListener;
import cn.xgp.xgplottery.Utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class XgpLottery extends JavaPlugin {
    public static JavaPlugin instance;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        saveResource("lang\\zh_CN.yml",false);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance=this;
        FileConfiguration config = getConfig();
        LangUtils.LoadLangFile(config.getString("lang"));
        instance.getLogger().info(LangUtils.EnableMessage);
        Objects.requireNonNull(Bukkit.getPluginCommand("xgplottery")).setExecutor(new GuiCommand());
        Bukkit.getPluginManager().registerEvents(new GuiListener(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance.getLogger().info(LangUtils.DisableMessage);
        saveConfig();
    }
}
