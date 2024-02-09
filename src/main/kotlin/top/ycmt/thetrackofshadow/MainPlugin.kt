package top.ycmt.thetrackofshadow

import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.pluginVersion
import taboolib.common.util.Vector
import top.ycmt.thetrackofshadow.config.GameSetting
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.PLUGIN_DISABLE_LEGACY_TEXT
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.PLUGIN_LOAD_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.pkg.logger.Logger

object MainPlugin {

    // 插件加载时
    @Awake(LifeCycle.LOAD)
    fun onLoad() {
        // 显示载入信息
        Logger.log("", PLUGIN_LOAD_LEGACY_TEXT, "")
    }

    // 插件启用时
    @Awake(LifeCycle.ENABLE)
    fun onEnable() {
        // 运行测试游戏
        for (i in 1 until 5 + 1) {
            val gameSetting = GameSetting(
                "test$i",
                "testMap",
                2,
                10,
                5,
                "world",
                Vector(-279.5, 182.0, -91.5),
                "world",
                Vector(-191, 40, -191),
                Vector(-639, 80, 191),
                Vector(-388.5, 51.0, -6.5),
                10.0,
                "world",
                Vector(-388.5, 182.0, -91.5),
            )
            GameManager.createGame(gameSetting)
        }
        // 显示启用信息
        Logger.info("插件就绪，版本：$pluginVersion")
    }

    // 插件卸载时
    @Awake(LifeCycle.DISABLE)
    fun onDisable() {
        // 显示卸载信息
        Logger.log("", PLUGIN_DISABLE_LEGACY_TEXT, "")
        // 停止所有游戏
        GameManager.removeGames()
    }

}