package cn.xgpjun.xgplottery2.lottery.anim.multiple.impl

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import cn.xgpjun.xgplottery2.enums.PresetItem
import cn.xgpjun.xgplottery2.enums.Sounds
import cn.xgpjun.xgplottery2.lottery.anim.multiple.MultipleAnim
import cn.xgpjun.xgplottery2.lottery.anim.single.impl.SelectAnim
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.SchedulerManager
import cn.xgpjun.xgplottery2.manager.unRegister
import cn.xgpjun.xgplottery2.utils.MyItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import java.util.*
import java.util.stream.IntStream

class SelectMultipleAnim :MultipleAnim(), Listener {
    private val gui : Inventory = Bukkit.createInventory(this,54,Message.SelectMultipleAnimTitle.get().color())
    override val name: String
        get() = "SelectMultiple"
    override val i18nName: String
        get() = Message.SelectMultipleAnimName.get().color()
    var animTask : Task? = null
    var animIsStop = false
    private var l : Lottery? = null
    private var uuid:UUID? = null

    private var chosenCount = 0
    private val chosenSlot = ArrayList<Int>()

    override fun draw(player: Player, lottery: Lottery, crateLocation: Location?) {
        l = lottery
        uuid = player.uniqueId
        SchedulerManager.getScheduler().runTaskAsynchronously{
            for (i in 0..53) {
                gui.setItem(i, SelectAnim.selectGlass)
            }
            SchedulerManager.getScheduler().runTask { player.openInventory(gui) }

        }
        animTask = SchedulerManager.getScheduler().runTaskTimerAsynchronously(0L,5L){
            object : Runnable {
                var counter = 0
                override fun run() {
                    counter++
                    randomGlass()
                    val pitch = Math.pow(2.0, counter / 18.0).toFloat()
                    player.playSound(player.location, Sounds.PLING.get(), 1.0f, pitch)
                    if (counter == 17) {
                        player.playSound(player.location, Sounds.LEVEL_UP.get(), 1.0f, 1.0f)
                        animTask?.cancel()
                        animIsStop = true
                    }
                }
            }
        }
        XgpLottery.instance.let { Bukkit.getPluginManager().registerEvents(this, it) }
    }

    @EventHandler
    fun click(e: InventoryClickEvent){
        SchedulerManager.getScheduler().runTaskAsynchronously{
            val slot = e.rawSlot
            if (e.whoClicked.uniqueId == uuid && e.inventory.holder == this && animIsStop && slot >= 0 && slot <= 53) {
                if (chosenCount >= 10 || chosenSlot.contains(slot)) {
                    return@runTaskAsynchronously
                }
                val player = e.whoClicked as Player
                gui.setItem(slot, awards[chosenCount].toDisplayItem())
                chosenCount++
                SchedulerManager.getScheduler().runTask{
                    player.playSound(player.location, Sounds.LEVEL_UP.get(), 1.0f, 1.0f)
                }
                chosenSlot.add(slot)
                if (chosenCount == 10) {
                    SchedulerManager.getScheduler().runTaskLaterAsynchronously(40L){
                        IntStream.range(0, 54).forEach { i: Int ->
                            if (!chosenSlot.contains(i)) {
                                gui.setItem(i,l?.getRandomAward()?.toDisplayItem())
                            }
                        }
                    }
                }
            }
        }
    }

    override fun finish() {
        animTask?.cancel()
        this.unRegister()
    }

    fun randomGlass() {
        IntStream.range(0, 54).forEach { i: Int ->
            val index: Int = Random().nextInt(16)
            gui.setItem(i, MyItemBuilder(PresetItem.glasses[index])
                .setDisplayName("").getItem())
        }
    }
    override fun getInventory() = gui
}