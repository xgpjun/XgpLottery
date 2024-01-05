package cn.xgpjun.xgplottery2.crate.particle

import cn.xgpjun.xgplottery2.crate.Crate

abstract class CrateParticleObject(val crate: Crate?) {
    abstract fun show()
    abstract fun clear()
}