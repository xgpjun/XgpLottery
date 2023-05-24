package cn.xgp.xgplottery.Utils;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

//for legacy version
public class VersionAdapterUtils {

    public static boolean ifMainHand(PlayerInteractEvent e){
        if(nmsUtils.versionToInt<9)
            return true;
        else
            return e.getHand()== EquipmentSlot.HAND;
    }
    public static void setItemInMainHand(Player player,ItemStack itemStack){
        if(nmsUtils.versionToInt<9){
            player.setItemInHand(itemStack);
        }else {
            player.getInventory().setItemInMainHand(itemStack);
        }


    }

    public static ItemStack getItemInMainHand(Player player){
        if(nmsUtils.versionToInt<9)
            return player.getItemInHand();
        else
            return player.getInventory().getItemInMainHand();
    }
}
