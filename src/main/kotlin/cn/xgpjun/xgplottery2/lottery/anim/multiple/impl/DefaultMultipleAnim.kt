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
import java.util.*
import kotlin.math.pow
import kotlin.random.Random


class DefaultMultipleAnim:MultipleAnim() {
    private val inv: Inventory = Bukkit.createInventory(this, 6 * 9, Message.DefaultMultipleAnimTitle.get().color())
    override val name: String
        get() = "DefaultMultiple"
    override val i18nName: String
        get() = Message.DefaultMultipleAnimName.get().color()

    //动画

    var borderSlot = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 25, 26, 27, 28, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53)
    var awardSlot = intArrayOf(20, 21, 22, 23, 24, 29, 30, 31, 32, 33)
    var animTask: Task? = null
    var stop = false
    //end

    override fun draw(player: Player, lottery: Lottery, crateLocation: Location?) {
        for (i in awardSlot) {
            inv.setItem(i, MyItemBuilder(PresetItem.WHITE_STAINED_GLASS_PANE.getItem())
                    .setDisplayName(Message.ItemBorder2Name.get().color())
                    .setLore(MessageL.ItemBorder2Lore.get().color())
                    .addEnchant()
                    .getItem())
        }
        for (i in borderSlot) {
            inv.setItem(i, MyItemBuilder(PresetItem.GRAY_STAINED_GLASS_PANE.getItem())
                .setDisplayName(Message.ItemBorderName.get().color())
                .setLore(MessageL.ItemBorderLore.get().color())
                .getItem())
        }
        player.openInventory(inv)

        var counter = 0
        var index = 0
        animTask = SchedulerManager.getScheduler().runTaskTimerAsynchronously(0L,5L){
            counter++
            val pitch = 2.0.pow(counter / 30.0).toFloat()
            //3->出现一个奖品 30 结束
            if (counter <= 21 || counter <= 27 && counter % 2 == 0 || counter == 30) {
                borderChange()
                player.playSound(player.location, Sounds.PLING.get(), 1.0f, pitch)
            }

            if (counter % 3 == 0) {
                setAward(index, awards[index])
                player.playSound(player.location, Sounds.LEVEL_UP.get(), 0.2f, 1.0f)
                index++
                if (index == 10) {
                    stop = true
                }
            }
            if (stop) {
                animTask?.cancel()
                player.playSound(player.location, Sounds.LEVEL_UP.get(), 1.0f, 1.0f)
            }
        }
    }

    override fun getInventory() = inv

    fun borderChange() {
        for (i in borderSlot) {
            val index: Int = Random.Default.nextInt(0,16)
            inv.setItem(i, MyItemBuilder(PresetItem.glasses[index])
                .setDisplayName(Message.ItemBorderName.get().color())
                .setLore(MessageL.ItemBorderLore.get().color())
                .getItem())
        }
    }
    fun setAward(index: Int, award: Award) {
        inv.setItem(awardSlot[index], award.toDisplayItem())
    }
}