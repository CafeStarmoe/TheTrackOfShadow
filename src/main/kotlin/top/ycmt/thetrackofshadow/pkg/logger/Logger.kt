package top.ycmt.thetrackofshadow.pkg.logger

import taboolib.common.platform.function.console
import taboolib.common.platform.function.dev
import taboolib.common.platform.function.severe
import taboolib.common.platform.function.warning
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 格式打印输出
object Logger {

    // 调试日志
    fun debug(vararg msgList: String) {
        msgList.forEach {
            dev("$it ${getStackTrace()}")
        }
    }

    // 标准日志
    fun info(vararg msgList: String) {
        msgList.forEach {
            console().sendMessage("${LegacyTextConst.EN_PREFIX_LEGACY_TEXT}§f$it <#ccffff,88dbfe>${getStackTrace()}</#>".toGradientColor())
        }
    }

    // 警告日志
    fun warn(vararg msgList: String) {
        msgList.forEach {
            warning("$it ${getStackTrace()}")
        }
    }

    // 错误日志
    fun error(vararg msgList: String) {
        msgList.forEach {
            severe("$it ${getStackTrace()}")
        }
    }

    // 无格式标准日志
    fun log(vararg msgList: String) {
        msgList.forEach {
            console().sendMessage(it)
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
