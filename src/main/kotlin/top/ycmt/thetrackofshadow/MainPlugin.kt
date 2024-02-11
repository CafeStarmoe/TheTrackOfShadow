package top.ycmt.thetrackofshadow

import com.comphenix.protocol.ProtocolLibrary
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.pluginVersion
import top.ycmt.thetrackofshadow.config.Config
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.PLUGIN_DISABLE_LEGACY_TEXT
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.PLUGIN_LOAD_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.event.EntityEquipmentPacket
import top.ycmt.thetrackofshadow.pkg.logger.Logger

object MainPlugin : Plugin() {

    // 插件加载时
    override fun onLoad() {
        // 显示载入信息
        Logger.log("", PLUGIN_LOAD_LEGACY_TEXT, "")
    }

    // 插件启用时
    override fun onEnable() {
        // 加载配置表
        Config.load()
        // 监听数据包事件
        initProtocolEvent()
        // 创建游戏
        Config.getGameDataMap().forEach { (_, gameData) ->
            GameManager.createGame(gameData)
        }
        // 显示启用信息
        Logger.info("插件就绪，版本：$pluginVersion")
    }

    // 监听数据包事件
    private fun initProtocolEvent() {
        val manager = ProtocolLibrary.getProtocolManager()
        // 实体装备数据包事件
        manager.addPacketListener(EntityEquipmentPacket)
    }

    // 插件卸载时
    override fun onDisable() {
        // 显示卸载信息
        Logger.log("", PLUGIN_DISABLE_LEGACY_TEXT, "")
        // 停止所有游戏
        GameManager.stopGames()
    }

}