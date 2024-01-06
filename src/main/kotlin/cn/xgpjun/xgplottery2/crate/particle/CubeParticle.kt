package cn.xgpjun.xgplottery2.crate.particle

import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import cn.xgpjun.xgplottery2.crate.Crate
import cn.xgpjun.xgplottery2.manager.SchedulerManager
import org.bukkit.Color
import top.zoyn.particlelib.pobject.Cube
import top.zoyn.particlelib.utils.matrix.Matrixs


class CubeParticle(crate: Crate?):CrateParticleObject(crate){

    var task:Task?=null
    private var cube1:Cube?=null
    private var cube2:Cube?=null
    override fun show() {
        if (crate?.getLocation()==null){
            return
        }
        cube1 = Cube(crate.getLocation()!!.add(0.1,1.0,0.1), crate.getLocation()!!.add(0.9,1.8,0.9))
        cube1!!.setColor(Color.AQUA)
            .addMatrix(Matrixs.rotateAroundXAxis(45.0))
            .addMatrix(Matrixs.rotateAroundZAxis(45.0))
            .alwaysShowAsync()

        cube2 = Cube(crate.getLocation(), crate.getLocation()!!.add(1.0,0.5,1.0))
        cube2!!.color = Color.WHITE
        cube2!!.alwaysShowAsync()
        task = SchedulerManager.getScheduler().runTaskTimerAsynchronously(0L,5L){
            cube1!!.addMatrix(Matrixs.rotateAroundYAxis(5.0))
        }
    }

    override fun clear() {
        task?.cancel()
        cube1?.turnOffTask()
        cube2?.turnOffTask()
    }
}