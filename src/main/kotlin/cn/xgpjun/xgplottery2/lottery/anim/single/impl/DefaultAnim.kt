package cn.xgpjun.xgplottery2.lottery.anim.single.impl

import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import cn.xgpjun.xgplottery2.enums.PresetItem
import cn.xgpjun.xgplottery2.enums.Sounds
import cn.xgpjun.xgplottery2.lottery.anim.single.SingleAnim
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.NMSManager
import cn.xgpjun.xgplottery2.manager.SchedulerManager
import cn.xgpjun.xgplottery2.send
import cn.xgpjun.xgplottery2.utils.MyItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.block.Lidded
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.math.pow


class DefaultAnim:SingleAnim() {
    private val inv = Bukkit.createInventory(this,3*9,Message.DefaultSingleAnimTitle.get().color())
    override val name: String
        get() = "Default"
    override val i18nName: String
        get() = Message.DefaultSingleAnimName.get().color()

    override fun getInventory(): Inventory {
        return inv
    }
    var animTask: Task? = null

    override fun draw(player: Player, lottery: Lottery, crateLocation: Location?) {
        crateLocation?.block?.let {
            //1.16+
            try {
                if(NMSManager.versionToInt>=16&&it.state is Lidded){
                    (it.state as Lidded).open()
                    SchedulerManager.getScheduler(crateLocation).runTaskLater(100L){
                        (it.state as Lidded).close()
                        val firework: Firework = crateLocation.world.spawn(crateLocation, Firework::class.java)
                        val fireworkMeta = firework.fireworkMeta
                        val builder = FireworkEffect.builder()
                        builder.withColor(Color.AQUA)
                        builder.with(FireworkEffect.Type.BALL)
                        fireworkMeta.addEffect(builder.build())
                        fireworkMeta.power = 1 // 设置烟花弹的强度
                        firework.fireworkMeta = fireworkMeta
                    }
                }
            }catch (_:Exception){
            }

        }
        SchedulerManager.getScheduler().runTaskAsynchronously{
            val award = award
            if (award==null){
                Message.AwardNull.get().send(player)
                return@runTaskAsynchronously
            }
            loadInv()
            val showItemList = ArrayList<ItemStack>()

            for (i in 0..40) {
                val itemStack = lottery.getRandomAward()!!.toDisplayItem()
                showItemList.add(itemStack)
            }
            //get award
            showItemList[20] = award.toDisplayItem()

            SchedulerManager.getScheduler().runTask{
                player.openInventory(inv)
            }

            var j = 0
            var stop = false
            animTask = SchedulerManager.getScheduler().runTaskTimerAsynchronously(0L,5L){
                for (i in 9..17) {
                    val myItem = MyItemBuilder(showItemList[i - 9 + j])
                    if (i == 13) inventory.setItem(i, myItem.addEnchant().getItem()) else inventory.setItem(
                        i,
                        myItem.getItem()
                    )
                }
                j++
                if (j >= showItemList.size - 9) {
                    j = 0
                }
                if (j == 17) { // 中奖物品的位置是第 17   21-4
                    stop = true
                }
                val pitch = 2.0.pow(j / 18.0).toFloat()
                player.playSound(player.location, Sounds.PLING.get(), 1.0f, pitch)
                if (stop) {
                    animTask?.cancel()
                    player.playSound(player.location, Sounds.LEVEL_UP.get(), 1.0f, 1.0f)
                }
            }
        }
    }

    override fun finish() {
        animTask?.cancel()
    }

    private fun loadInv(){
        var i = 0
        while (i < 27) {
            inv.setItem(i, PresetItem.BORDER_GLASS.getItem())
            if (i == 8) i = 17
            i++
        }
        inv.setItem(4, PresetItem.BORDER_GLASS2.getItem())
        inv.setItem(22, PresetItem.BORDER_GLASS2.getItem())

    }
}