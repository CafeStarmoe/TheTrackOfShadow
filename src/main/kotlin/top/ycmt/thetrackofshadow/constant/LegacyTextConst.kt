package top.ycmt.thetrackofshadow.constant

import taboolib.module.chat.impl.DefaultComponent
import taboolib.module.chat.toGradientColor

// 消息前缀常量
object LegacyTextConst {
    // 英文标志消息
    val EN_LOGO_LEGACY_TEXT = DefaultComponent()
        .append("TheTrackOfShadow".toGradientColor(listOf(0xffd89d, 0x76d3c3, 0x8a95f5))).bold()
        .toLegacyText()

    // 中文标志消息
    val CN_LOGO_LEGACY_TEXT = DefaultComponent()
        .append("随影寻踪".toGradientColor(listOf(0xffd89d, 0x76d3c3, 0x8a95f5))).bold()
        .toLegacyText()

    // 英文消息前缀
    val EN_PREFIX_LEGACY_TEXT = DefaultComponent()
        .append(EN_LOGO_LEGACY_TEXT)
        .append(">>".toGradientColor(listOf(0xffa3ca, 0xffb5ef))).bold()
        .append(" ")
        .toLegacyText()

    // 中文消息前缀
    val CN_PREFIX_LEGACY_TEXT = DefaultComponent()
        .append(CN_LOGO_LEGACY_TEXT)
        .append(">>".toGradientColor(listOf(0xffa3ca, 0xffb5ef))).bold()
        .append(" ")
        .toLegacyText()
}