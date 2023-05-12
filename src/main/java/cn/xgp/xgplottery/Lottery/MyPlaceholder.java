package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.Utils.ConfigSetting;
import cn.xgp.xgplottery.XgpLottery;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class MyPlaceholder extends PlaceholderExpansion {

    private final JavaPlugin plugin; // The instance is created in the constructor and won't be modified, so it can be final

    public MyPlaceholder(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public String getIdentifier() {
        return "XgpLottery";
    }

    @Override
    public String getAuthor() {
        return "1badsobig";
    }

    @Override
    public String getVersion() {
        return ConfigSetting.version;
    }
    @Override
    public boolean canRegister() {
        return true; // 可以注册占位符
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        String[] args = params.split("_");
        if(args.length==2){
            String playerName = args[0];
            String lotteryName = args[1];
            UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
            return Integer.toString(LotteryTimes.getTimes(uuid,lotteryName));
        }
        return null; // Placeholder is unknown by the Expansion
    }


}
