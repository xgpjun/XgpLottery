package cn.xgp.xgplottery.Utils;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

//for legacy version
public class VersionAdapterUtils {

    public static boolean ifMainHand(PlayerInteractEvent e){
        return EquipmentSlot.HAND.equals(e.getHand());
    }
    public static void setItemInMainHand(Player player,ItemStack itemStack){
        player.getInventory().setItemInMainHand(itemStack);
    }

    public static ItemStack getItemInMainHand(Player player){
        return player.getInventory().getItemInMainHand();
    }
    public static int getPlayerEmptySlot(Player player){
        ItemStack[] items = player.getInventory().getContents();
        int emptySlots = 0;
        for (ItemStack i : items) {
            if (i == null) {
                emptySlots++;
            }
        }
        ItemStack[] equipment = player.getEquipment().getArmorContents();
        for (ItemStack i : equipment) {
            if (i == null) {
                emptySlots--;
            }
        }
        if(player.getInventory().getItemInOffHand().getItemMeta() == null)
            emptySlots--;
        return emptySlots;

    }
}
