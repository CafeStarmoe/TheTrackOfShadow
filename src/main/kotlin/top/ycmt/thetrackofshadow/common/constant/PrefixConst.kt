package top.ycmt.thetrackofshadow.common.constant

import taboolib.module.chat.impl.DefaultComponent
import taboolib.module.chat.toGradientColor

// 消息前缀常量
object PrefixConst {
    // 消息前缀
    val PrefixMessage = DefaultComponent()
        .append("TheTrackOfShadow".toGradientColor(listOf(0xffd89d, 0x76d3c3, 0x8a95f5))).bold()
        .append("->".toGradientColor(listOf(0xffa3ca, 0xffb5ef))).bold()
        .append(" ")
        .toLegacyText()
}