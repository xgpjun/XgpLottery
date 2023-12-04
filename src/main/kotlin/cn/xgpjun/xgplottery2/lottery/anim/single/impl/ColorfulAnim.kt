package cn.xgpjun.xgplottery2.lottery.anim.single.impl

import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import cn.xgpjun.xgplottery2.enums.PresetItem
import cn.xgpjun.xgplottery2.enums.Sounds
import cn.xgpjun.xgplottery2.lottery.anim.single.SingleAnim
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.MessageL
import cn.xgpjun.xgplottery2.manager.SchedulerManager
import cn.xgpjun.xgplottery2.send
import cn.xgpjun.xgplottery2.utils.MyItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.stream.IntStream

class ColorfulAnim:SingleAnim() {
    private val inv = Bukkit.createInventory(this,6*9,Message.ColorfulAnimTitle.get().color())
    override val name: String
        get() = "Colorful"
    override val i18nName: String
        get() = Message.ColorfulAnimName.get().color()



    //begin 动画相关数据
    var animTask:Task? = null

    private var batches: MutableList<IntArray> = ArrayList()

    //end
    override fun draw(player: Player, lottery: Lottery, crateLocation: Location?) {
        SchedulerManager.getScheduler().runTaskAsynchronously{
            if (award==null){
                Message.AwardNull.get().send(player)
                return@runTaskAsynchronously
            }
            loadInv()
            val guaranteed = award?.customTag?.get("guaranteedRewards")?.equals(true)?:false

            SchedulerManager.getScheduler().runTask{
                player.openInventory(inv)
            }

            animTask = SchedulerManager.getScheduler().runTaskTimerAsynchronously(20L,15L){
                object : Runnable{
                    val colorGlass: ItemStack =
                        MyItemBuilder((if (guaranteed) PresetItem.ORANGE_STAINED_GLASS_PANE.getItem() else PresetItem.PURPLE_STAINED_GLASS_PANE.getItem()))
                            .setDisplayName(Message.ItemBorderName.get().color())
                            .setLore(MessageL.ItemBorderLore.get().color())
                            .getItem()
                    val glass: ItemStack =
                        MyItemBuilder(PresetItem.LIGHT_BLUE_STAINED_GLASS_PANE.getItem())
                            .setDisplayName(Message.ItemBorderName.get().color())
                            .setLore(MessageL.ItemBorderLore.get().color())
                            .getItem()
                    var counter = 0
                    override fun run() {
                        if (counter <= 8) {
                            for (index in batches.get(counter)) {
                                if (counter < 4) {
                                    inv.setItem(index, glass)
                                } else {
                                    inv.setItem(index, colorGlass)
                                }
                            }
                        } else if (counter == 9) {
                            IntStream.range(0, 54).forEach { i: Int ->
                                inv.setItem(i, colorGlass)
                            }
                        }
                        val pitch = Math.pow(2.0, counter / 10.0).toFloat()
                        player.playSound(player.location, Sounds.PLING.get(), 0.5f, pitch)
                        counter++
                        if (counter == 11) {
                            IntStream.range(0, 54).forEach { i: Int ->
                                inv.setItem(i, null)
                            }
                            inv.setItem(22, award?.item)
                            player.playSound(player.location, Sounds.LEVEL_UP.get(), 1.0f, 1.0f)
                            animTask?.cancel()
                        }
                        if (inv.viewers.isEmpty()) {
                            animTask?.cancel()
                        }
                    }
                }
            }
        }
    }


    override fun finish() {
        animTask?.cancel()
    }
    override fun getInventory(): Inventory {
        return inv
    }
    private fun loadInv(){
        IntStream.range(0, 54).forEach { i: Int ->
            inv.setItem(
                i,
                MyItemBuilder(PresetItem.WHITE_STAINED_GLASS_PANE.getItem()).setDisplayName(Message.ColorfulAnimClick.get().color()).getItem()
            )
        }
        batches.add(intArrayOf(0))
        batches.add(intArrayOf(10))
        batches.add(intArrayOf(11))
        batches.add(intArrayOf(21))
        batches.add(intArrayOf(22))
        batches.add(intArrayOf(32, 33, 41, 42))
        batches.add(intArrayOf(43, 44, 52, 53))
        batches.add(intArrayOf(23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 50, 51))
        batches.add(intArrayOf(3, 4, 5, 6, 7, 8, 12, 13, 14, 15, 16, 17, 21, 22, 30, 31, 39, 40, 48, 49))
    }
}