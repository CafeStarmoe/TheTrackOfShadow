package top.ycmt.thetrackofshadow.constant

import taboolib.module.chat.impl.DefaultComponent
import taboolib.module.chat.toGradientColor

// 消息前缀常量
object MessageConst {
    // 英文标志消息
    val ENLogoMessage = DefaultComponent()
        .append("TheTrackOfShadow".toGradientColor(listOf(0xffd89d, 0x76d3c3, 0x8a95f5))).bold()
        .toLegacyText()

    // 中文标志消息
    val CNLogoMessage = DefaultComponent()
        .append("随影寻踪".toGradientColor(listOf(0xffd89d, 0x76d3c3, 0x8a95f5))).bold()
        .toLegacyText()

    // 英文消息前缀
    val ENPrefixMessage = DefaultComponent()
        .append(ENLogoMessage)
        .append(">>".toGradientColor(listOf(0xffa3ca, 0xffb5ef))).bold()
        .append(" ")
        .toLegacyText()

    // 中文消息前缀
    val CNPrefixMessage = DefaultComponent()
        .append(CNLogoMessage)
        .append(">>".toGradientColor(listOf(0xffa3ca, 0xffb5ef))).bold()
        .append(" ")
        .toLegacyText()
}