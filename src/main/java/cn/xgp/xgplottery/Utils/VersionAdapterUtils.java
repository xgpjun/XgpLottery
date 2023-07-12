package cn.xgp.xgplottery.Utils;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

//for legacy version
public class VersionAdapterUtils {

    public static boolean ifMainHand(PlayerInteractEvent e){
        if(NMSUtils.versionToInt<9)
            return true;
        else
            return EquipmentSlot.HAND.equals(e.getHand());
    }
    public static void setItemInMainHand(Player player,ItemStack itemStack){
        if(NMSUtils.versionToInt<9){
            player.setItemInHand(itemStack);
        }else {
            player.getInventory().setItemInMainHand(itemStack);
        }
    }

    public static ItemStack getItemInMainHand(Player player){
        if(NMSUtils.versionToInt<9)
            return player.getItemInHand();
        else
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
        if(NMSUtils.versionToInt<9){
            return emptySlots;
        }
        else{
            if(player.getInventory().getItemInOffHand()==null||player.getInventory().getItemInOffHand().getItemMeta()==null)
                emptySlots--;
            return emptySlots;
        }
    }
}
