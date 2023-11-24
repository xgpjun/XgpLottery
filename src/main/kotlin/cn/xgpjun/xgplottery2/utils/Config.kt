package cn.xgpjun.xgplottery2.utils

import cn.xgpjun.xgplottery2.XgpLottery

object Config {
    var version = "2.0.0"
    var checkFullInventory = false
    fun loadConfig(){
        XgpLottery.instance.saveDefaultConfig()
        XgpLottery.instance.config.let {
            version = it.getString("version","2.0.0").toString()
            checkFullInventory = it.getBoolean("checkFullInventory",false)
        }
    }
}