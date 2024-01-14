package cn.xgpjun.xgplottery2.lottery.anim.single.impl

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import cn.xgpjun.xgplottery2.enums.PresetItem
import cn.xgpjun.xgplottery2.enums.Sounds
import cn.xgpjun.xgplottery2.lottery.anim.single.SingleAnim
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.*
import cn.xgpjun.xgplottery2.utils.MyItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class SelectAnim: SingleAnim(),Listener {
    private val inv = Bukkit.createInventory(this,6*9,Message.SelectAnimTitle.get().color())
    override val name: String
        get() = "Select"
    override val i18nName: String
        get() = Message.SelectAnimName.get().color()

    //动画数据
    var selected = false
    var lottery:Lottery? = null
    var uuid : UUID? =null
    var animTask:Task? = null
    companion object{
        var selectGlass: ItemStack = MyItemBuilder(PresetItem.ORANGE_STAINED_GLASS_PANE.getItem())
            .setDisplayName(Message.SelectAnimClick.get().color())
            .getItem()
    }
    //end
    override fun draw(player: Player, lottery: Lottery, crateLocation: Location?) {
        crateLocation.openChest()
        SchedulerManager.getScheduler().runTaskLater(100L){
            crateLocation.closeChest()
        }
        for (i in 0..53) {
            inv.setItem(i, selectGlass)
        }
        this.lottery = lottery
        uuid = player.uniqueId
        player.openInventory(inv)
        XgpLottery.instance.let { Bukkit.getPluginManager().registerEvents(this, it) }
    }

    @EventHandler
    fun click(e:InventoryClickEvent){
        if (!selected && e.whoClicked.uniqueId == uuid && e.inventory.holder == this &&  e.rawSlot >= 0 && e.rawSlot <= 53){
            selected = true
            inv.setItem(e.rawSlot,award?.item)
            val player = e.whoClicked as Player
            player.playSound(player.location, Sounds.LEVEL_UP.get(), 1.0f, 1.0f)
            animTask = SchedulerManager.getScheduler().runTaskLaterAsynchronously(40L){
                for (i in 0..53) {
                    if (e.rawSlot == i) continue
                    val itemStack = lottery!!.getRandomAward()!!.toDisplayItem()
                    inv.setItem(i, itemStack)
                }
            }
        }
    }

    override fun getInventory() = inv

    override fun finish() {
        this.unRegister()
        animTask?.cancel()
    }

}