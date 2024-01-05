package cn.xgpjun.xgplottery2.manager

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.data.PlayerData
import cn.xgpjun.xgplottery2.log
import cn.xgpjun.xgplottery2.stop
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.io.File
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object DatabaseManager{
    lateinit var dataSource: HikariDataSource

    val onlinePlayerData = ConcurrentHashMap<UUID,PlayerData>()


    fun register(){
        try {
            val config = XgpLottery.instance.config
            val dataSourceConfig = HikariConfig()
            val isMysql = config.getBoolean("database.mysql.enable")
            if (isMysql){
                dataSourceConfig.driverClassName = config.getString("database.mysql.driver")
                val url =
                    "jdbc:mysql://" + config.getString("database.mysql.url") + "/" + config.getString("database.mysql.database") + "?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai"
                dataSourceConfig.jdbcUrl = url
                dataSourceConfig.username = config.getString("database.mysql.username")
                dataSourceConfig.password = config.getString("database.mysql.password")
            }else{
                val path = config.getString("database.sqlite.path","")!!
                if (path != ""){
                    dataSourceConfig.jdbcUrl = "jdbc:sqlite:$path"
                }else{
                    val dataPath = XgpLottery.instance.dataFolder.absolutePath + File.separator + "data.db"
                    dataSourceConfig.jdbcUrl = "jdbc:sqlite:$dataPath"
                }
            }
            dataSourceConfig.maximumPoolSize = 50
            dataSourceConfig.minimumIdle = 5
            dataSource = HikariDataSource(dataSourceConfig)
            createTable()
        }catch (e:Exception){
            e.printStackTrace()
            stop("An error occurred while initializing the database:${e.message}")
        }
    }

    private fun createTable(){
        val sql = "create table if not exists xlplayerdata\n" +
                "(\n" +
                "\tuuid varchar(40) not null,\n" +
                "\ttotalDrawCount int null,\n" +
                "\tindividualPoolDrawCount longtext null,\n" +
                "\tkeyCount longtext null,\n" +
                "\tcustomData longtext null\n" +
                ");"
        try {
            dataSource.connection.use { connection ->
                connection.createStatement().use { statement ->
                   statement.execute(sql)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stop("An error occurred while creating table:${e.message}")
        }
    }

    fun getPlayerData(uuid: UUID): PlayerData {
        return onlinePlayerData[uuid] ?: loadPlayerData(uuid)
    }
    fun PlayerData.save(){
        dataSource.connection.use { connection ->
            connection.prepareStatement("UPDATE xlplayerdata " +
                    "SET totalDrawCount = ?," +
                    "individualPoolDrawCount = ?," +
                    "keyCount = ?," +
                    "customData = ?" +
                    "\tWHERE uuid = ?").use {
                        it.setInt(1,this.totalDrawCount)
                        it.setString(2, this.individualPoolDrawCountString())
                        it.setString(3, this.keyCountString())
                        it.setString(4, this.customDataString())
                        it.setString(5,"${this.uuid}")
                        it.executeUpdate()
            }

        }
    }

    private fun loadPlayerData(uuid: UUID):PlayerData{
        dataSource.connection.use { connection ->
            connection.prepareStatement("SELECT * FROM xlplayerdata WHERE uuid = ?").use {
                it.setString(1,uuid.toString())
                val result = it.executeQuery()
                if (result.next()){
                    val individualPoolDrawCount = try {
                        Gson().fromJson(result.getString("individualPoolDrawCount"),ConcurrentHashMap<String,Int>().javaClass)
                    }catch (e :JsonSyntaxException){
                        "&cDeserialization error occurred while reading column: $uuid-individualPoolDrawCount, data was reset".log()
                        ConcurrentHashMap<String,Int>()
                    }?:ConcurrentHashMap<String,Int>()
                    val keyCount = try {
                        Gson().fromJson(result.getString("keyCount"),ConcurrentHashMap<String,Int>().javaClass)
                    }catch (e :JsonSyntaxException){
                        "&cDeserialization error occurred while reading column: $uuid-keyCount, data was reset".log()
                        ConcurrentHashMap<String,Int>()
                    }?:ConcurrentHashMap<String,Int>()
                    val customData = try {
                        Gson().fromJson(result.getString("customData"),ConcurrentHashMap<String,Any>().javaClass)
                    }catch (e :JsonSyntaxException){
                        "&cDeserialization error occurred while reading column: $uuid-customData, data was reset".log()
                        ConcurrentHashMap<String,Any>()
                    }?:ConcurrentHashMap<String,Any>()
                    val count = result.getInt("totalDrawCount")
                    return PlayerData(uuid, count,individualPoolDrawCount, keyCount, customData)
                }else{
                    connection.prepareStatement("INSERT INTO xlplayerdata (uuid) VALUES (?)").use { statement ->
                        statement.setString(1,uuid.toString())
                        statement.executeUpdate()
                    }
                    return PlayerData(uuid,0,ConcurrentHashMap(),ConcurrentHashMap(),ConcurrentHashMap())
                }
            }
        }
    }

}