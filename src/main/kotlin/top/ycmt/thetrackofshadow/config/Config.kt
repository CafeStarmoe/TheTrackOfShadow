package top.ycmt.thetrackofshadow.config

import app.softwork.serialization.csv.CSVFormat
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import taboolib.common.io.runningResourcesInJar
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import top.ycmt.thetrackofshadow.constant.ConfigConst
import top.ycmt.thetrackofshadow.pkg.logger.Logger
import java.io.File

// 配置表读取
object Config {
    private lateinit var gameDataMap: Map<String, GameData> // 游戏配置表
    private lateinit var itemDataMap: Map<Int, ItemData> // 物品配置表
    private lateinit var weaponDataMap: Map<Int, WeaponData> // 武器配置表
    private lateinit var equipDataMap: Map<Int, EquipData> // 装备配置表
    private lateinit var propDataMap: Map<Int, PropData> // 道具配置表
    private lateinit var potionDataMap: Map<Int, PotionData> // 药水配置表
    private lateinit var gemDataMap: Map<Int, GemData> // 宝石配置表

    @Config("setting.toml")
    private lateinit var pluginSetting: Configuration // 插件配置文件

    // 加载配置表
    fun load() {
        // 创建默认配置表
        releaseDefaultConfig()
        // 读取配置表
        loadGameData() // 游戏配置表
        loadItemData() // 物品配置表
        loadWeaponData() // 武器配置表
        loadEquipData() // 装备配置表
        loadPropData() // 道具配置表
        loadPotionData() // 药水配置表
        loadGemData() // 宝石配置表
    }

    // 加载游戏配置表
    private fun loadGameData() {
        val dataMap = mutableMapOf<String, GameData>()
        val dataList = readTable(GameData.serializer(), "${ConfigConst.CONFIG_PATH_PREFIX}/GameData.csv")
        dataList?.forEach {
            dataMap[it.gameName] = it
        }
        gameDataMap = dataMap
        Logger.info("游戏配置表读取完毕, 共${gameDataMap.size}条数据")
    }

    // 加载物品配置表
    private fun loadItemData() {
        val dataMap = mutableMapOf<Int, ItemData>()
        val dataList = readTable(ItemData.serializer(), "${ConfigConst.CONFIG_PATH_PREFIX}/ItemData.csv")
        dataList?.forEach {
            dataMap[it.itemId] = it
        }
        itemDataMap = dataMap
        Logger.info("物品配置表读取完毕, 共${itemDataMap.size}条数据")
    }

    // 加载武器配置表
    private fun loadWeaponData() {
        val dataMap = mutableMapOf<Int, WeaponData>()
        val dataList = readTable(WeaponData.serializer(), "${ConfigConst.CONFIG_PATH_PREFIX}/WeaponData.csv")
        dataList?.forEach {
            dataMap[it.itemId] = it
        }
        weaponDataMap = dataMap
        Logger.info("武器配置表读取完毕, 共${weaponDataMap.size}条数据")
    }

    // 加载装备配置表
    private fun loadEquipData() {
        val dataMap = mutableMapOf<Int, EquipData>()
        val dataList = readTable(EquipData.serializer(), "${ConfigConst.CONFIG_PATH_PREFIX}/EquipData.csv")
        dataList?.forEach {
            dataMap[it.itemId] = it
        }
        equipDataMap = dataMap
        Logger.info("装备配置表读取完毕, 共${equipDataMap.size}条数据")
    }

    // 加载道具配置表
    private fun loadPropData() {
        val dataMap = mutableMapOf<Int, PropData>()
        val dataList = readTable(PropData.serializer(), "${ConfigConst.CONFIG_PATH_PREFIX}/PropData.csv")
        dataList?.forEach {
            dataMap[it.itemId] = it
        }
        propDataMap = dataMap
        Logger.info("道具配置表读取完毕, 共${propDataMap.size}条数据")
    }

    // 加载药水配置表
    private fun loadPotionData() {
        val dataMap = mutableMapOf<Int, PotionData>()
        val dataList = readTable(PotionData.serializer(), "${ConfigConst.CONFIG_PATH_PREFIX}/PotionData.csv")
        dataList?.forEach {
            dataMap[it.itemId] = it
        }
        potionDataMap = dataMap
        Logger.info("药水配置表读取完毕, 共${potionDataMap.size}条数据")
    }

    // 加载宝石配置表
    private fun loadGemData() {
        val dataMap = mutableMapOf<Int, GemData>()
        val dataList = readTable(GemData.serializer(), "${ConfigConst.CONFIG_PATH_PREFIX}/GemData.csv")
        dataList?.forEach {
            dataMap[it.itemId] = it
        }
        gemDataMap = dataMap
        Logger.info("宝石配置表读取完毕, 共${gemDataMap.size}条数据")
    }

    // 释放默认配置表文件
    private fun releaseDefaultConfig() {
        runningResourcesInJar.keys.filter {
            it.startsWith("${ConfigConst.CONFIG_PATH_PREFIX}/")
        }.forEach {
            releaseResourceFile(it)
        }
    }

    // 读取csv表
    @OptIn(ExperimentalSerializationApi::class)
    private fun <T> readTable(deserializer: KSerializer<T>, tablePath: String): List<T>? {
        val filePath = "${getDataFolder().path}/${tablePath}"
        val dataFile = File(filePath)
        // 文件不存在则跳出
        if (!dataFile.exists()) {
            Logger.error("配置表文件不存在, filePath: $filePath")
            return null
        }
        // 读取数据的每一行
        var fileData = dataFile.readText()
        // 替换crlf为lf
        fileData = fileData.replace("\r\n", "\n")
        // 反序列化csv数据
        val dataList = CSVFormat.decodeFromString(ListSerializer(deserializer), fileData)
        // 然后反序列化出来的元素
        return dataList
    }

    // 获取插件配置文件
    fun getPluginSetting(): Configuration {
        return pluginSetting
    }

    // 获取游戏配置表
    fun getGameDataMap(): Map<String, GameData> {
        return gameDataMap
    }

    // 获取物品配置表
    fun getItemDataMap(): Map<Int, ItemData> {
        return itemDataMap
    }

    // 通过物品id获取物品配置表
    fun getItemDataByItemId(itemId: Int): ItemData? {
        return itemDataMap[itemId]
    }

    // 获取武器配置表
    fun getWeaponDataMap(): Map<Int, WeaponData> {
        return weaponDataMap
    }

    // 通过物品id获取武器配置表
    fun getWeaponDataByItemId(itemId: Int): WeaponData? {
        return weaponDataMap[itemId]
    }

    // 获取装备配置表
    fun getEquipDataMap(): Map<Int, EquipData> {
        return equipDataMap
    }

    // 通过物品id获取装备配置表
    fun getEquipDataByItemId(itemId: Int): EquipData? {
        return equipDataMap[itemId]
    }

    // 获取道具配置表
    fun getPropDataMap(): Map<Int, PropData> {
        return propDataMap
    }

    // 通过物品id获取道具配置表
    fun getPropDataByItemId(itemId: Int): PropData? {
        return propDataMap[itemId]
    }

    // 获取药水配置表
    fun getPotionDataMap(): Map<Int, PotionData> {
        return potionDataMap
    }

    // 通过物品id获取药水配置表
    fun getPotionDataByItemId(itemId: Int): PotionData? {
        return potionDataMap[itemId]
    }

    // 获取宝石配置表
    fun getGemDataMap(): Map<Int, GemData> {
        return gemDataMap
    }

    // 通过物品id获取宝石配置表
    fun getGemDataByItemId(itemId: Int): GemData? {
        return gemDataMap[itemId]
    }

}