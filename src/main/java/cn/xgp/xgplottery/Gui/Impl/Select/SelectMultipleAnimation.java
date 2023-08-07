package cn.xgp.xgplottery.Gui.Impl.Select;

import cn.xgp.xgplottery.Gui.Impl.Manage.LotterySetting;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl.BoxMultipleAnimation;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl.DefaultMultipleAnimation;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl.MultipleSelectItemAnimation;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl.SimpleMultipleAnimation;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class SelectMultipleAnimation extends LotteryGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9, LangUtils.MultipleAnimationSelect);
    private final Lottery lottery;
    private LotterySetting lotterySetting;

    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        setBorder(inv);
        inv.setItem(10,new MyItem(Material.CHEST)
                .setDisplayName(ChatColor.AQUA+ new DefaultMultipleAnimation(null,null).toLore())
                .getItem());
        inv.setItem(11,new MyItem(Material.CHEST)
                .setDisplayName(ChatColor.AQUA+ new SimpleMultipleAnimation(null,null).toLore())
                .getItem());
        inv.setItem(12,new MyItem(Material.CHEST)
                .setDisplayName(ChatColor.AQUA+ new MultipleSelectItemAnimation(null,null).toLore())
                .getItem());
        inv.setItem(13,new MyItem(Material.CHEST)
                .setDisplayName(ChatColor.AQUA+ new BoxMultipleAnimation(null,null).toLore())
                .getItem());
        return this;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        switch (e.getRawSlot()){
            //上一层
            case 0: player.openInventory(lotterySetting.getInventory());break;
            //退出
            case 8: player.getOpenInventory().close();break;
            case 10: {
                lottery.setMultipleAnimation("DefaultMultipleAnimation");
                SerializeUtils.saveLotteryData();
                player.openInventory(lotterySetting.getInventory());
                break;
            }
            case 11: {
                lottery.setMultipleAnimation("SimpleMultipleAnimation");
                SerializeUtils.saveLotteryData();
                player.openInventory(lotterySetting.getInventory());
                break;
            }
            case 12:{
                lottery.setMultipleAnimation("MultipleSelectItemAnimation");
                SerializeUtils.saveLotteryData();
                player.openInventory(lotterySetting.getInventory());
                break;
            }
            case 13:{
                lottery.setMultipleAnimation("BoxMultipleAnimation");
                SerializeUtils.saveLotteryData();
                player.openInventory(lotterySetting.getInventory());
                break;
            }
            default:
        }
    }
}
