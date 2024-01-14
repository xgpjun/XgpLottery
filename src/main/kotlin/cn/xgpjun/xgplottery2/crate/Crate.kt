package cn.xgpjun.xgplottery2.crate

import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import cn.xgpjun.xgplottery2.manager.LotteryManager
import cn.xgpjun.xgplottery2.manager.SchedulerManager
import cn.xgpjun.xgplottery2.manager.getTag
import cn.xgpjun.xgplottery2.manager.setTag
import cn.xgpjun.xgplottery2.utils.Config
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.metadata.MetadataValue
import java.util.UUID


class Crate(
    val lotteryName: String,
    val x:Double,
    val y:Double,
    val z:Double,
    val world:String,
    val uuid: UUID,
    var crateParticle:String) {

    var task :Task? = null
    var dropItems :Entity? = null

    fun equal(location: Location):Boolean{
        return (location.world?.name == this.world&&
                location.x==this.x&&
                location.y==this.y&&
                location.z==this.z)
    }

    fun getLocation():Location?{
        return Bukkit.getWorld(world)?.let {
            Location(it,x,y,z)
        }
    }

    fun displayItems(){
        if (Config.displayItem){
            val lottery = LotteryManager.getLottery(lotteryName)
            task = SchedulerManager.getScheduler(getLocation()!!).runTaskTimerAsynchronously(20L,100L){
                val displayItem = lottery?.getRandomAward()?.item?.clone()?.setTag("XLDrop","drop")
                displayItem?.let {
                    val item = getLocation()?.world?.dropItem(getLocation()!!,displayItem)

                }
            }
        }
    }

    fun cancelDisplay(){
        task?.cancel()
        dropItems?.remove()
    }
}