package top.ycmt.thetrackofshadow.pkg.logger

import taboolib.common.platform.function.console
import taboolib.common.platform.function.severe
import taboolib.common.platform.function.warning
import top.ycmt.thetrackofshadow.pkg.gradientcolor.ColorSetting
import top.ycmt.thetrackofshadow.pkg.gradientcolor.GradientColor

val logger: Logger = Logger()

// 格式打印输出
class Logger {

    // 标准日志
    fun info(vararg msgList: String) {
        for (s in msgList) {
            val formatMsg = GradientColor(
                ColorSetting("TheTrackOfShadow", listOf("#ffd89d", "#76d3c3", "#8a95f5"), isBold = true),
                ColorSetting(" "),
                ColorSetting("->", listOf("#ffa3ca", "#ffb5ef"), isBold = true),
                ColorSetting(" "),
                ColorSetting(s, listOf("#c1ecff", "#53b4eb"), isBold = true),
            ).generate()
            console().sendMessage(formatMsg)
        }
    }

    // 警告日志
    fun warn(vararg msgList: String) {
        for (s in msgList) {
            warning("$s ${getStackTrace()}")
        }
    }

    // 错误日志
    fun error(vararg msgList: String) {
        for (s in msgList) {
            severe("$s ${getStackTrace()}")
        }
    }

    // 无格式标准日志
    fun log(vararg msgList: String) {
        for (s in msgList) {
            console().sendMessage(s)
        }
    }

    // 获取详细信息
    private fun getStackTrace(): String {
        val stackTraces = Thread.currentThread().stackTrace
        val ste = stackTraces[3]
        val className = ste.className
        val methodName = ste.methodName
        val lineNumber = ste.lineNumber

        return "[$className:$lineNumber $methodName()]"
    }
}
