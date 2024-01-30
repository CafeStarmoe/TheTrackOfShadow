package top.ycmt.thetrackofshadow.pkg.logger

import taboolib.common.platform.function.console
import taboolib.common.platform.function.severe
import taboolib.common.platform.function.warning
import taboolib.module.chat.impl.DefaultComponent
import taboolib.module.chat.toGradientColor

val logger: Logger = Logger()

// 格式打印输出
class Logger {

    // 标准日志
    fun info(vararg msgList: String) {
        for (s in msgList) {
            val formatMsg = DefaultComponent()
                .append("TheTrackOfShadow".toGradientColor(listOf(0xffd89d, 0x76d3c3, 0x8a95f5))).bold()
                .append("->".toGradientColor(listOf(0xffa3ca, 0xffb5ef))).bold()
                .append(" ")
                .append("§f$s §3${getStackTrace()}")
                .toLegacyText()
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
