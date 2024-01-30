package top.ycmt.thetrackofshadow.pkg.message

import taboolib.module.chat.impl.DefaultComponent
import taboolib.module.chat.toGradientColor

// 发送成功格式的消息
fun org.bukkit.entity.Player.sendSuccessMessage(vararg msgList: String) {
    for (s in msgList) {
        val successComponent = DefaultComponent()
            .append("TheTrackOfShadow".toGradientColor(listOf(0xffd89d, 0x76d3c3, 0x8a95f5))).bold()
            .append("->".toGradientColor(listOf(0xffa3ca, 0xffb5ef))).bold()
            .append(" ")
            .append(s.toGradientColor(listOf(0xdeffd2, 0xbee8ff)))
        // 发送消息
        this.sendMessage(successComponent.toLegacyText())
    }
}

// 发送失败格式的消息
fun org.bukkit.entity.Player.sendFailedMessage(vararg msgList: String) {
    for (s in msgList) {
        val failedComponent = DefaultComponent()
            .append("TheTrackOfShadow".toGradientColor(listOf(0xffd89d, 0x76d3c3, 0x8a95f5))).bold()
            .append("->".toGradientColor(listOf(0xffa3ca, 0xffb5ef))).bold()
            .append(" ")
            .append(s.toGradientColor(listOf(0xffcbcb, 0xff7093)))
        // 发送消息
        this.sendMessage(failedComponent.toLegacyText())
    }
}