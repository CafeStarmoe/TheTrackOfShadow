package top.ycmt.thetrackofshadow

import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.pluginVersion
import taboolib.common.util.Vector
import taboolib.module.chat.impl.DefaultComponent
import taboolib.module.chat.toGradientColor
import top.ycmt.thetrackofshadow.config.GameSetting
import top.ycmt.thetrackofshadow.constant.MessageConst
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.pkg.logger.logger

class MainPlugin : Plugin() {

    // 插件加载时
    override fun onLoad() {
        // 显示载入信息
        logger.log(
            "",
            DefaultComponent()
                .append("正在加载".toGradientColor(listOf(0xd5d5ff, 0x9e9eee))).bold()
                .append(" ")
                .append(MessageConst.ENLogoMessage)
                .append(" ")
                .append("Beta".toGradientColor(listOf(0xffd6d6, 0xe14d4d))).bold()
                .append("...".toGradientColor(listOf(0xf4f4f4, 0x808080))).bold()
                .append(" ")
                .append(Bukkit.getVersion().toGradientColor(listOf(0xadf0e0, 0x8fbdf4))).bold()
                .toLegacyText(),
            "",
        )
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
        logger.log(
            "",
            DefaultComponent()
                .append("正在卸载".toGradientColor(listOf(0xfcc3f3, 0xee80b5))).bold()
                .append(" ")
                .append(MessageConst.ENLogoMessage)
                .append(" ")
                .append("Beta".toGradientColor(listOf(0xffd6d6, 0xe14d4d))).bold()
                .append("...".toGradientColor(listOf(0xf4f4f4, 0x808080))).bold()
                .append(" ")
                .append(Bukkit.getVersion().toGradientColor(listOf(0xadf0e0, 0x8fbdf4))).bold()
                .toLegacyText(),
            "",
        )
    }

}