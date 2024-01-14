package cn.xgpjun.xgplottery2.lottery.anim.single.impl

import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import cn.xgpjun.xgplottery2.enums.PresetItem
import cn.xgpjun.xgplottery2.enums.Sounds
import cn.xgpjun.xgplottery2.lottery.anim.single.SingleAnim
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.SchedulerManager
import cn.xgpjun.xgplottery2.manager.closeChest
import cn.xgpjun.xgplottery2.manager.openChest
import cn.xgpjun.xgplottery2.utils.MyItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.stream.IntStream

class MarqueeAnim:SingleAnim() {

    private val inv: Inventory = Bukkit.createInventory(this, 6 * 9, Message.MarqueeAnimTitle.get().color())

    //动画
    private var temp: ItemStack? = null
    var animTask:Task?=null
    //end
    override val name: String
        get() = "Marquee"
    override val i18nName: String
        get() = Message.MarqueeAnimName.get().color()

    override fun draw(player: Player, lottery: Lottery, crateLocation: Location?) {
        crateLocation.openChest()
        SchedulerManager.getScheduler().runTaskLater(100L){
            crateLocation.closeChest()
        }
        player.openInventory(inv)
        val awardSlot: Int = Random().nextInt(20)

        IntStream.range(0, 54).forEach { i: Int ->
            val next = Random().nextInt(lottery.awards.size)
            val itemStack: ItemStack = lottery.awards.values.toList()[next].toDisplayItem()
            inv.setItem(i, itemStack)
        }
        inv.setItem(awardSlot, award?.toDisplayItem())
        val totalStep = 54 + awardSlot

        var counter = 0
        var step = 0
        animTask = SchedulerManager.getScheduler().runTaskTimerAsynchronously(0L,5L){
            counter++
            val pitch = Math.pow(2.0, (step.toFloat() / totalStep).toDouble()).toFloat()
            if (counter < 54 || counter < 162 && counter % 2 == 0) {
                player.playSound(player.location, Sounds.PLING.get(), 0.2f, pitch)
                nexStep(step % 54)
                step++
            }
            if (step == totalStep) {
                player.playSound(player.location, Sounds.LEVEL_UP.get(), 1.0f, 1.0f)
                animTask?.cancel()
            }
        }
    }

    override fun finish() {
        animTask?.cancel()
    }
    override fun getInventory(): Inventory {
        return inv
    }

    fun nexStep(index: Int) {
        if (temp != null) {
            if (index == 0) {
                inv.setItem(53, temp)
            } else {
                inv.setItem(index - 1, temp)
            }
        }
        temp = inv.getItem(index)
        if (temp != null) {
            inv.setItem(index, MyItemBuilder(PresetItem.LIME_STAINED_GLASS_PANE.getItem()).setDisplayName(Message.MarqueeAnimSelected.get().color()).getItem())
        }
    }
}