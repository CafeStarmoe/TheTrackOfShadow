package top.ycmt.thetrackofshadow

import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.pluginVersion
import top.ycmt.thetrackofshadow.conf.GameSetting
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.pkg.gradientcolor.ColorSetting
import top.ycmt.thetrackofshadow.pkg.gradientcolor.GradientColor
import top.ycmt.thetrackofshadow.pkg.logger.logger

class MainPlugin : Plugin() {

    // 插件加载时
    override fun onLoad() {
        // 显示载入信息
        val loadMessage = GradientColor(
            ColorSetting("正在加载", listOf("#d5d5ff", "#9e9eee"), isBold = true),
            ColorSetting(" "),
            ColorSetting("TheTrackOfShadow", listOf("#ffd89d", "#76d3c3", "#8a95f5"), isBold = true),
            ColorSetting(" "),
            ColorSetting("Beta", listOf("#ffd6d6", "#e14d4d"), isBold = true),
            ColorSetting("...", listOf("#f4f4f4", "#808080"), isBold = true),
            ColorSetting(" "),
            ColorSetting(Bukkit.getVersion(), listOf("#adf0e0", "#8fbdf4"), isBold = true),
        ).generate()
        logger.log("", loadMessage, "")
    }

    // 插件启用时
    override fun onEnable() {
        // 显示启用信息
        logger.info("插件就绪，版本：$pluginVersion")
        // 运行测试游戏
        val gameSetting = GameSetting("test", 30u)
        GameManager.createGame(gameSetting)
    }

    // 插件卸载时
    override fun onDisable() {
        // 显示卸载信息
        val unloadMessage = GradientColor(
            ColorSetting("正在卸载", listOf("#fcc3f3", "#ee80b5"), isBold = true),
            ColorSetting(" "),
            ColorSetting("TheTrackOfShadow", listOf("#ffd89d", "#76d3c3", "#8a95f5"), isBold = true),
            ColorSetting(" "),
            ColorSetting("Beta", listOf("#ffd6d6", "#e14d4d"), isBold = true),
            ColorSetting("...", listOf("#f4f4f4", "#808080"), isBold = true),
            ColorSetting(" "),
            ColorSetting(Bukkit.getVersion(), listOf("#adf0e0", "#8fbdf4"), isBold = true),
        ).generate()
        logger.log("", unloadMessage, "")
    }

}