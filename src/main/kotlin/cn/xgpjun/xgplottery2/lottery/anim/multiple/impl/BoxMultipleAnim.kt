package cn.xgpjun.xgplottery2.lottery.anim.multiple.impl

import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import cn.xgpjun.xgplottery2.enums.PresetItem
import cn.xgpjun.xgplottery2.enums.Sounds
import cn.xgpjun.xgplottery2.lottery.anim.multiple.MultipleAnim
import cn.xgpjun.xgplottery2.lottery.pojo.Award
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.MessageL
import cn.xgpjun.xgplottery2.manager.SchedulerManager
import cn.xgpjun.xgplottery2.utils.MyItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.math.pow

class BoxMultipleAnim:MultipleAnim() {
    val gui: Inventory = Bukkit.createInventory(this, 6 * 9, Message.BoxMultipleAnimTitle.get().color())

    private var showItemList = ArrayList<ItemStack>()
    private val borderSlot = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 25, 26, 27, 28, 34, 35, 36, 37, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53)
    private val awardSlot = intArrayOf(29, 30, 31, 32, 33, 38, 39, 40, 41, 42)
    var animTask: Task? = null
    override val name: String
        get() = "BoxMultiple"
    override val i18nName: String
        get() = Message.BoxMultipleAnimName.get().color()

    override fun draw(player: Player, lottery: Lottery, crateLocation: Location?) {
        SchedulerManager.getScheduler().runTaskAsynchronously{
            for (i in 0..45) {
                val itemStack = lottery.getRandomAward()!!.toDisplayItem()
                showItemList.add(itemStack)
            }

            for (i in awardSlot) {
                gui.setItem(i, MyItemBuilder(PresetItem.WHITE_STAINED_GLASS_PANE.getItem())
                        .setDisplayName(Message.ItemBorder2Name.get().color())
                        .setLore(MessageL.ItemBorder2Lore.get().color())
                        .addEnchant()
                        .getItem())
            }
            for (i in borderSlot) {
                gui.setItem(i, MyItemBuilder(PresetItem.GRAY_STAINED_GLASS_PANE.getItem())
                    .setDisplayName(Message.ItemBorderName.get().color())
                    .setLore(MessageL.ItemBorderLore.get().color())
                    .addEnchant()
                    .getItem())
            }
            gui.setItem(4, MyItemBuilder(PresetItem.ORANGE_STAINED_GLASS_PANE.getItem())
                .setDisplayName("")
                .getItem())
            gui.setItem(22, MyItemBuilder(PresetItem.ORANGE_STAINED_GLASS_PANE.getItem())
                .setDisplayName("")
                .getItem())
            SchedulerManager.getScheduler().runTask{ player.openInventory(gui) }

            var counter = 0
            var index = 0
            var stop = false
            animTask = SchedulerManager.getScheduler().runTaskTimerAsynchronously(0L,5L){
                counter++
                val pitch = 2.0.pow(counter / 30.0).toFloat()
                for (i in 10..16) {
                    val myItem = MyItemBuilder(showItemList[i - 9 + counter])
                    if (i == 13) gui.setItem(i, myItem.addEnchant().getItem()) else gui.setItem(i, myItem.getItem())
                }
                player.playSound(player.location, Sounds.PLING.get(), 1.0f, pitch)
                if (counter % 3 == 0) {
                    setAward(index, awards[index])
                    player.playSound(player.location, Sounds.LEVEL_UP.get(), 0.2f, 1.0f)
                    index++
                    if (index == 10) {
                        stop =(true)
                    }
                }
                if (stop) {
                    player.playSound(player.location, Sounds.LEVEL_UP.get(), 1.0f, 1.0f)
                }
            }
        }
    }

    override fun finish() {
        animTask?.cancel()
    }
    fun setAward(index: Int, award: Award) {
        gui.setItem(awardSlot[index], award.toDisplayItem())
    }

    override fun getInventory() = gui
}