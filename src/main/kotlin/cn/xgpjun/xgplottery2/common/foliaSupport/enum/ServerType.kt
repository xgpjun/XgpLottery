package cn.xgpjun.xgplottery2.common.foliaSupport.enum

enum class ServerType(private val className: String) {
    FOLIA("io.papermc.paper.threadedregions.RegionizedServerInitEvent"),
    BUKKIT("org.bukkit.Bukkit");

//    private val className: String? = null
//    open fun ServerType(className: String?) {
//        this.className = className
//    }

    companion object{
        fun getServerType(): ServerType {
            return try {
                Class.forName(FOLIA.className)
                FOLIA
            } catch (e: ClassNotFoundException) {
                BUKKIT
            }
        }
    }
}