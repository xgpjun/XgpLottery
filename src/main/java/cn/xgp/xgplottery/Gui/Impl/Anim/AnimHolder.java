package cn.xgp.xgplottery.Gui.Impl.Anim;

import cn.xgp.xgplottery.Gui.PlayerGui;
import cn.xgp.xgplottery.Utils.NMSUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class AnimHolder extends PlayerGui {
    /**
     * 数据值 0: 白色（White）
     * 数据值 1: 橙色（Orange）
     * 数据值 2: 品红色（Magenta）
     * 数据值 3: 淡蓝色（Light Blue）
     * 数据值 4: 黄色（Yellow）
     * 数据值 5: 黄绿色（Lime）
     * 数据值 6: 粉红色（Pink）
     * 数据值 7: 灰色（Gray）
     * 数据值 8: 淡灰色（Light Gray）
     * 数据值 9: 青色（Cyan）
     * 数据值 10: 紫色（Purple）
     * 数据值 11: 蓝色（Blue）
     * 数据值 12: 棕色（Brown）
     * 数据值 13: 绿色（Green）
     * 数据值 14: 红色（Red）
     * 数据值 15: 黑色（Black）
     */
    protected static ItemStack[] glasses = new ItemStack[16];

    static {
        if(NMSUtils.versionToInt<13){
            Material select = Material.valueOf("STAINED_GLASS_PANE");
            for(int i=0;i<16;i++){
                glasses[i] = new ItemStack(select,1,(byte)i);
            }
        }else {
            glasses[0] = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            glasses[1] = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
            glasses[2] = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
            glasses[3] = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
            glasses[4] = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
            glasses[5] = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            glasses[6] = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
            glasses[7] = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            glasses[8] = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
            glasses[9] = new ItemStack(Material.CYAN_STAINED_GLASS_PANE);
            glasses[10] = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
            glasses[11] = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
            glasses[12] = new ItemStack(Material.BROWN_STAINED_GLASS_PANE);
            glasses[13] = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            glasses[14] = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            glasses[15] = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        }
    }


}
