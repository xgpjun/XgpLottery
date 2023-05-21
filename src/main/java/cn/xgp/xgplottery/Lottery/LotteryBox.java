package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.XgpLottery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class LotteryBox{
    @Getter
    private final String lotteryName;
    @Getter
    private final Location location;

    public LotteryBox(String lotteryName, Location location) {
        this.lotteryName = lotteryName;
        this.location = location;
    }

    public static Lottery getLotteryByLocation(Location location){
        LotteryBox result =null;
        for(LotteryBox lotteryBox: XgpLottery.lotteryBoxList){
            if(lotteryBox.getLocation().equals(location))
                result = lotteryBox;
        }
        if(result!=null){
            return XgpLottery.lotteryList.get(result.getLotteryName());
        }
        return null;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("lotteryName", lotteryName);

        map.put("world", Objects.requireNonNull(location.getWorld()).getName());
        map.put("x",location.getX());
        map.put("y", location.getY());
        map.put("z", location.getZ());
        map.put("yaw", location.getYaw());
        map.put("pitch", location.getPitch());
        return map;
    }

    public static LotteryBox deserialize(Map<String, Object> map) {
        String lotteryName = (String) map.get("lotteryName");
        String worldName = (String) map.get("world");
        World world = Bukkit.getWorld(worldName);
        double x = (double) map.get("x");
        double y = (double) map.get("y");
        double z = (double) map.get("z");
        double yaw = (double) map.get("yaw");
        double pitch = (double) map.get("pitch");

        float yawFloat = (float) yaw;
        float pitchFloat = (float) pitch;
        Location location = new Location(world, x, y, z, yawFloat, pitchFloat);

        return new LotteryBox(lotteryName, location);
    }

}

