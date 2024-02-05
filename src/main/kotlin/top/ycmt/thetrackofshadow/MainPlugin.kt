package top.ycmt.thetrackofshadow

import taboolib.common.platform.Plugin
import taboolib.common.platform.function.pluginVersion
import taboolib.common.util.Vector
import top.ycmt.thetrackofshadow.config.GameSetting
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.PLUGIN_DISABLE_LEGACY_TEXT
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.PLUGIN_LOAD_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.pkg.logger.logger

class MainPlugin : Plugin() {

    // 插件加载时
    override fun onLoad() {
        // 显示载入信息
        logger.log("", PLUGIN_LOAD_LEGACY_TEXT, "")
    }

    // 插件启用时
    override fun onEnable() {
        // 显示启用信息
        logger.info("插件就绪，版本：$pluginVersion")
        // 运行测试游戏
        for (i in 1 until 5 + 1) {
            val gameSetting = GameSetting(
                "test$i",
                "testMap",
                2,
                10,
                5,
                "world",
                Vector(-191, 40, -191),
                Vector(-639, 80, 191)
            )
            GameManager.createGame(gameSetting)
        }
    }

    // 插件卸载时
    override fun onDisable() {
        // 显示卸载信息
        logger.log("", PLUGIN_DISABLE_LEGACY_TEXT, "")
    }

}