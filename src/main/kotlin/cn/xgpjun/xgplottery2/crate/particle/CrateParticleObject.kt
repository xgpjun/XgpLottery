package cn.xgpjun.xgplottery2.crate.particle

import cn.xgpjun.xgplottery2.crate.Crate
import cn.xgpjun.xgplottery2.manager.ParticleManager

abstract class CrateParticleObject(val crate: Crate?) {
    abstract fun show()
    abstract fun clear()
    fun register(name:String){
        ParticleManager.particles[name] = this.javaClass
    }
}