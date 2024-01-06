package cn.xgpjun.xgplottery2.manager

import cn.xgpjun.xgplottery2.crate.particle.CrateParticleObject
import cn.xgpjun.xgplottery2.crate.particle.DefaultParticle
import cn.xgpjun.xgplottery2.crate.particle.CubeParticle
import org.bukkit.Bukkit


object ParticleManager {
    var enable = false

    val particles = HashMap<String,Class<out CrateParticleObject>>()
    val list = arrayListOf<CrateParticleObject>()
    fun register(){
        enable = Bukkit.getPluginManager().isPluginEnabled("ParticleLib")
        if (!enable){
            return
        }
        clear()
        particles["DefaultParticle"] = DefaultParticle(null).javaClass
        particles["CubeParticle"] = CubeParticle(null).javaClass
        show()
    }

    fun show(){
        list.clear()
        CrateManager.cratesList.values.forEach{
            it.crateParticle.let { particle->
                list.add(particles[particle]?.getConstructor(it.javaClass)?.newInstance(it)?:DefaultParticle(it))
            }
        }
        list.forEach{
            it.show()
        }
    }
    fun clear(){
        list.forEach{
            it.clear()
        }
    }
}



