package cn.xgp.xgplottery.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;

import org.bukkit.Bukkit;

import org.bukkit.inventory.ItemStack;



import java.util.Objects;


public class ChatUtils {

    public static void sendMessage(String string, ItemStack itemStack) {


        String nbt = nmsUtils.toNBTString(itemStack);
        TextComponent itemInfo = new TextComponent(ChatColor.AQUA+"[");
        itemInfo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(nbt)}));
        String name = getName(itemStack);

        itemInfo.addExtra(ChatColor.AQUA+ name);
        itemInfo.addExtra(ChatColor.AQUA+"]");
        TextComponent message = new TextComponent(string);
        TextComponent amount = new TextComponent(ChatColor.GREEN+"x"+itemStack.getAmount());
        message.addExtra(itemInfo);
        message.addExtra(amount);
        Bukkit.getServer().spigot().broadcast(message);
    }


    public static String getName(ItemStack itemStack){
        if(Objects.requireNonNull(itemStack.getItemMeta()).hasDisplayName())
            return itemStack.getItemMeta().getDisplayName();

        return itemStack.getType().name();
    }
}
