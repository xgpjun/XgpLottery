package cn.xgpjun.xgplottery2

import cn.xgpjun.xgplottery2.lottery.pojo.Lottery



fun main() {
    val l :Lottery? = null
     l?.let { lottery->
//                        CrateManager.createCrate(sender,lottery)
//                        return true
    }?: run {
        // 在 null 情况下执行的代码
        // 你可以在这里添加你希望执行的备用操作
        println("Lottery not found or null")
    }
}