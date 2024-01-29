package top.ycmt.thetrackofshadow

import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.common.platform.function.info
import top.ycmt.thetrackofshadow.pkg.gradientcolor.ColorSetting
import top.ycmt.thetrackofshadow.pkg.gradientcolor.GradientColor

class MainPlugin : Plugin() {

    // 插件启动时
    override fun onEnable() {
        info("随影迷踪启动！")

        val loadMessage = GradientColor(
            ColorSetting("正在载入", listOf("#d5d5ff", "#9e9eee"), isBold = true),
            ColorSetting(" "),
            ColorSetting("TheTrackOfShadow", listOf("#ffffe1", "#ffca60", "#ac8c4c"), isBold = true),
            ColorSetting(" "),
            ColorSetting("Beta", listOf("#ffd6d6", "#e14d4d"), isBold = true),
            ColorSetting("...", listOf("#f4f4f4", "#808080"), isBold = true),
            ColorSetting(" "),
            ColorSetting(Bukkit.getVersion(), listOf("#adf0e0", "#8fbdf4"), isBold = true),
        ).generate()

        console().sendMessage(loadMessage)
    }

    // 插件卸载时
    override fun onDisable() {
        info("随影迷踪卸载！")
    }

}