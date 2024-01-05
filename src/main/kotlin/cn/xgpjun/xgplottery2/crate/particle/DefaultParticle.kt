package cn.xgpjun.xgplottery2.crate.particle

import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import cn.xgpjun.xgplottery2.crate.Crate
import cn.xgpjun.xgplottery2.manager.SchedulerManager
import org.bukkit.Color
import org.bukkit.Location
import top.zoyn.particlelib.pobject.Circle
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class DefaultParticle(crate: Crate?):CrateParticleObject(crate){
    var task : Task? =null

    override fun show(){
        val c = Circle2(crate?.getLocation()!!.clone().add(0.5,0.0,0.5))
        c.setPeriod(2L)
        c.color = Color.AQUA
        c.radius = 2.0.pow(0.5)/2
        task = SchedulerManager.getScheduler().runTaskTimerAsynchronously(0L,4L){
            for (i in 0..10) {
                c.playNextPoint()
            }
        }
    }
    override fun clear(){
        task?.cancel()
    }
}

class Circle2(location: Location): Circle(location){
    private var currentAngle = 0.0
    override fun playNextPoint() {
        currentAngle += step
        val radians = Math.toRadians(currentAngle)
        val x = radius * cos(radians)
        val z = radius * sin(radians)
        val y = (currentAngle/angle)*2
        spawnParticle(origin.clone().add(x, y, z))
        spawnParticle(origin.clone().add(-x, y, -z))

        // 进行重置
        if (currentAngle > angle) {
            currentAngle = 0.0
        }
    }
}