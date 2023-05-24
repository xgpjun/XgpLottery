package cn.xgp.xgplottery.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class ChatUtils {

    public static void sendMessage(String string, ItemStack itemStack) {

        TextComponent itemInfo = new TextComponent(ChatColor.AQUA+"[奖品~]!");
        itemInfo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(nmsUtils.toNBTString(itemStack))}));

        TextComponent message = new TextComponent(string);
        TextComponent amount = new TextComponent(ChatColor.GREEN+"x"+itemStack.getAmount());
        message.addExtra(itemInfo);
        message.addExtra(amount);
        Bukkit.getServer().spigot().broadcast(message);

    }
}
