package cn.xgpjun.xgplottery2.lottery.anim.multiple.impl

import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import cn.xgpjun.xgplottery2.enums.PresetItem
import cn.xgpjun.xgplottery2.enums.Sounds
import cn.xgpjun.xgplottery2.lottery.anim.multiple.MultipleAnim
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
import java.util.*
import java.util.stream.IntStream

class SimpleMultipleAnim:MultipleAnim() {
    private val gui : Inventory = Bukkit.createInventory(this,54,Message.SimpleMultipleAnimTitle.get())
    override val name: String
        get() = "SimpleMultiple"
    override val i18nName: String
        get() = Message.SimpleMultipleAnimName.get().color()

    private var index = 0

    var animTask :Task? = null
    var animTask1 :Task? = null

    override fun draw(player: Player, lottery: Lottery, crateLocation: Location?) {
        SchedulerManager.getScheduler().runTaskAsynchronously{
            IntStream.range(0, 27).forEach { i: Int ->
                gui.setItem(i, MyItemBuilder(PresetItem.GRAY_STAINED_GLASS_PANE.getItem())
                    .setDisplayName(Message.ItemBorderName.get().color())
                    .setLore(MessageL.ItemBorderLore.get().color())
                    .getItem()
                )
            }
            IntStream.range(27, 36).forEach { i: Int ->
                gui.setItem(i, MyItemBuilder(PresetItem.ORANGE_STAINED_GLASS_PANE.getItem())
                    .setDisplayName(Message.ItemBorderName.get().color())
                    .setLore(MessageL.ItemBorderLore.get().color())
                    .getItem()
                )
            }
            IntStream.range(45, 54).forEach { i: Int ->
                gui.setItem(i, MyItemBuilder(PresetItem.ORANGE_STAINED_GLASS_PANE.getItem())
                    .setDisplayName(Message.ItemBorderName.get().color())
                    .setLore(MessageL.ItemBorderLore.get().color())
                    .getItem()
                )
            }
            gui.setItem(13,null)
            SchedulerManager.getScheduler().runTask{player.openInventory(gui)}
            var counter = 0
            animTask1 = SchedulerManager.getScheduler().runTaskTimerAsynchronously(30L,20L){
                setNextItem(awards[counter].toDisplayItem())
                val pitch = Math.pow(2.0, counter / 10.0).toFloat()
                player.playSound(player.location, Sounds.PLING.get(), 0.2f, pitch)
                counter++
                if (counter == 10) {
                    player.playSound(player.location, Sounds.LEVEL_UP.get(), 1.0f, 1.0f)
                    animTask1?.cancel()
                }
            }
        }
    }

    override fun finish() {
        animTask?.cancel()
        animTask1?.cancel()
    }
    override fun getInventory() = gui

    fun setNextItem(item: ItemStack?) {
        if (index < 9)
            gui.setItem(36 + index, item)
        gui.setItem(13, item)
        index++
        if (index == 10) {
            animTask = SchedulerManager.getScheduler().runTaskTimerAsynchronously(0L,5L){
                object : Runnable {
                    var counter = 0
                    override fun run() {
                        counter++
                        val glass: ItemStack = MyItemBuilder(PresetItem.glasses[Random().nextInt(16)]).setDisplayName("").getItem()
                        IntStream.range(0, 27).forEach { i: Int ->
                            if (i != 13) {
                                gui.setItem(i, glass)
                            }
                        }
                        if (counter == 20) {
                            animTask?.cancel()
                        }
                    }
                }
            }
        }
    }
}