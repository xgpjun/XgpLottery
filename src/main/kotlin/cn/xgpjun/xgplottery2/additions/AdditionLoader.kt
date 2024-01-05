package cn.xgpjun.xgplottery2.additions

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.log
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.*
import java.util.jar.JarFile

object AdditionLoader {
    fun loadAddition(){
        val folder = File(XgpLottery.instance.dataFolder, "Additions")
        if (!folder.exists()){
            folder.mkdirs()
        }
        val jarFiles = folder.listFiles { _: File?, name: String ->
            name.lowercase(
                Locale.getDefault()
            ).endsWith(".jar")
        }
        if (jarFiles != null) {
            for (jarFile in jarFiles) {
                val jarUrl = jarFile.toURI().toURL()
                URLClassLoader(
                    arrayOf<URL>(jarUrl),
                    XgpLottery::class.java.classLoader
                ).use { classLoader ->
                    JarFile(jarFile).use { jar ->
                        val entries = jar.entries()
                        while (entries.hasMoreElements()) {
                            val entry = entries.nextElement()
                            if (entry.name.endsWith(".class")) {
                                val className = entry.name.replace("/", ".").replace(".class", "")
                                val targetClass = classLoader.loadClass(className)
                                if (XLAddition::class.java.isAssignableFrom(targetClass)) {
                                    val addition: XLAddition =
                                        targetClass.newInstance() as XLAddition
                                    "&fregister $1${addition.getName()} &a${addition.getVersion()} &fby &7${addition.getAuthor()} ...".log()
                                    addition.register()
                                    if (addition is Listener) {
                                        Bukkit.getPluginManager().registerEvents(addition, XgpLottery.instance)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}