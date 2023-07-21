package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.Lottery.LotteryBox;
import cn.xgp.xgplottery.XgpLottery;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;


public class ChatUtils {


    public static void sendMessage(String string, ItemStack itemStack) {
        String nbt = NMSUtils.toNBTString(itemStack);
        TextComponent itemInfo = new TextComponent(ChatColor.AQUA + "[");
        itemInfo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(nbt)}));


        String name = getName(itemStack);
        itemInfo.addExtra(ChatColor.AQUA + name);
        itemInfo.addExtra(ChatColor.AQUA + "]");
        TextComponent message = new TextComponent(string);
        TextComponent amount = new TextComponent(ChatColor.GREEN + "x" + itemStack.getAmount());
        message.addExtra(itemInfo);
        message.addExtra(amount);
        try {
            //部分分支不存在这个方法。
            Bukkit.getServer().spigot().broadcast(message);
        } catch (NoSuchMethodError e) {
            Bukkit.getServer().broadcastMessage(message.getText() + ChatColor.AQUA + "[" + name + "]");
        }

        //For bc
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");

        out.writeUTF("ALL");//服务器
        out.writeUTF("XgpLottery");//频道
        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        try {
            String json = new Gson().toJson(new pack(NMSUtils.toNBTString(itemStack),string,getName(itemStack),itemStack.getAmount()));
            msgOut.writeUTF(json);
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


    public static String getName(ItemStack itemStack){
        if(Objects.requireNonNull(itemStack.getItemMeta()).hasDisplayName())
            return itemStack.getItemMeta().getDisplayName();

        return itemStack.getType().name();
    }


    public static void sendText(String json) {
        pack p = new Gson().fromJson(json, pack.class);

        String nbt = p.nbt, msg = p.msg, name = p.name;
        int count = p.count;

        TextComponent itemInfo = new TextComponent(ChatColor.AQUA + "[");
        itemInfo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(nbt)}));

        itemInfo.addExtra(ChatColor.AQUA + name);
        itemInfo.addExtra(ChatColor.AQUA + "]");
        TextComponent message = new TextComponent(msg);
        TextComponent amount = new TextComponent(ChatColor.GREEN + "x" + count);
        message.addExtra(itemInfo);
        message.addExtra(amount);
        try {
            //部分分支不存在这个方法。
            Bukkit.getServer().spigot().broadcast(message);
        } catch (NoSuchMethodError e) {
            Bukkit.getServer().broadcastMessage(message.getText() + ChatColor.AQUA + "[" + name + "]");
        }
    }

    //发送抽奖箱列表并传送
    public static void sendBoxList(Player player) {
        player.sendMessage(org.bukkit.ChatColor.BLUE + "--------❀ 抽奖箱列表 ❀--------");
        int index = 1;
        for (LotteryBox box : XgpLottery.lotteryBoxList) {
            String lotteryName = box.getLotteryName();
            Location location = box.getLocation();
            TextComponent teleport = new TextComponent(org.bukkit.ChatColor.GOLD + "     §l[点击传送]");
            teleport.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/xl box list " + (index - 1)));
            teleport.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(getLocationMsg(location))}));
            TextComponent information = new TextComponent(org.bukkit.ChatColor.BLUE + String.valueOf(index) + ": §b奖池名称: §a" + lotteryName);
            information.addExtra(teleport);

            try {
                //部分分支不存在这个方法。
                player.spigot().sendMessage(information);
            } catch (NoSuchMethodError e) {
                information.addExtra(org.bukkit.ChatColor.RED + "当前版本不支持点击传送！ 箱子在: " + getLocationMsg(location));
                player.sendMessage(information.toString());
            }
            index++;
        }
    }

    private static String getLocationMsg(Location location) {
        return "§aWorld: §b" + Objects.requireNonNull(location.getWorld()).getName() + "§aX: §b" + location.getBlockX() + "§aY: §b" + location.getBlockY() + "§aZ: §b" + location.getBlockZ();
    }

    @AllArgsConstructor
    static class pack {
        String nbt;
        String msg;
        String name;
        int count;
    }
}
