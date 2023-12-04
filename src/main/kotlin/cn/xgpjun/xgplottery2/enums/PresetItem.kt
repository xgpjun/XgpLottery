package cn.xgpjun.xgplottery2.enums

import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.MessageL
import cn.xgpjun.xgplottery2.manager.NMSManager
import cn.xgpjun.xgplottery2.utils.MyItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class PresetItem {
    WHITE_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[0]
        }
                            },
    ORANGE_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[1]
        }
    },
    MAGENTA_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[2]
        }
    },
    LIGHT_BLUE_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[3]
        }
    },
    YELLOW_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[4]
        }
    },
    LIME_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[5]
        }
    },
    PINK_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[6]
        }
    },
    GRAY_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[7]
        }
    },
    LIGHT_GRAY_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[8]
        }
    },
    CYAN_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[9]
        }
    },
    PURPLE_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[10]
        }
    },
    BLUE_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[11]
        }
    },
    BROWN_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[12]
        }
    },
    GREEN_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[13]
        }
    },
    RED_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[14]
        }
    },
    BLACK_STAINED_GLASS_PANE{
        override fun getItem(): ItemStack {
            return glasses[15]
        }
    },
    SIGN{
        override fun getItem(): ItemStack {
            return ItemStack(if (NMSManager.versionToInt>13){
                Material.LEGACY_SIGN
            }else{
                Material.valueOf("SIGN")
            })
        }
    },
    BORDER_GLASS{
        override fun getItem(): ItemStack {
            return MyItemBuilder(GRAY_STAINED_GLASS_PANE.getItem())
                .setDisplayName(Message.ItemBorderName.get().color())
                .setLore(MessageL.ItemBorderLore.get().color())
                .getItem()
        }
    },
    BORDER_GLASS2{
        override fun getItem(): ItemStack {
            return MyItemBuilder(ORANGE_STAINED_GLASS_PANE.getItem())
                .setDisplayName(Message.ItemBorder2Name.get().color())
                .setLore(MessageL.ItemBorder2Lore.get().color())
                .getItem()
        }
    },
    NEXT_PAGE{
        override fun getItem(): ItemStack {
            return MyItemBuilder(Material.PAPER)
                .setDisplayName(Message.ItemNextPageName.get().color())
                .getItem()
        }
    },
    PREVIOUS_PAGE{
        override fun getItem(): ItemStack {
            return MyItemBuilder(Material.PAPER)
                .setDisplayName(Message.ItemPreviousPageName.get().color())
                .getItem()

        }
    },
    ;

    abstract fun getItem(): ItemStack
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
    companion object{
        val glasses by lazy {
            val t = arrayOfNulls<ItemStack>(16)
            if (NMSManager.versionToInt < 13) {
                val select = Material.valueOf("STAINED_GLASS_PANE")
                for (i in 0..15) {
                    t[i] = ItemStack(
                        select, 1, i.toByte()
                            .toShort()
                    )
                }
            } else {
                t[0] = ItemStack(Material.WHITE_STAINED_GLASS_PANE)
                t[1] = ItemStack(Material.ORANGE_STAINED_GLASS_PANE)
                t[2] = ItemStack(Material.MAGENTA_STAINED_GLASS_PANE)
                t[3] = ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
                t[4] = ItemStack(Material.YELLOW_STAINED_GLASS_PANE)
                t[5] = ItemStack(Material.LIME_STAINED_GLASS_PANE)
                t[6] = ItemStack(Material.PINK_STAINED_GLASS_PANE)
                t[7] = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
                t[8] = ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
                t[9] = ItemStack(Material.CYAN_STAINED_GLASS_PANE)
                t[10] = ItemStack(Material.PURPLE_STAINED_GLASS_PANE)
                t[11] = ItemStack(Material.BLUE_STAINED_GLASS_PANE)
                t[12] = ItemStack(Material.BROWN_STAINED_GLASS_PANE)
                t[13] = ItemStack(Material.GREEN_STAINED_GLASS_PANE)
                t[14] = ItemStack(Material.RED_STAINED_GLASS_PANE)
                t[15] = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
            }
            t.requireNoNulls()
        }
    }
}