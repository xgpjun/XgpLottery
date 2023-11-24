package cn.xgpjun.xgplottery2.lottery.pojo

import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.UUID


class Crate(
    val lotteryName: String,
    val x:Double,
    val y:Double,
    val z:Double,
    val world:String,
    val uuid: UUID) {

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
}