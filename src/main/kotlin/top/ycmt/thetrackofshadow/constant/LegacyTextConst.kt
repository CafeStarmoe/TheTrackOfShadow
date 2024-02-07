package top.ycmt.thetrackofshadow.constant

import org.bukkit.Bukkit
import taboolib.module.chat.impl.DefaultComponent
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 消息前缀常量
object LegacyTextConst {
    // 英文标志消息
    val EN_LOGO_LEGACY_TEXT = DefaultComponent()
        .append("<#ffd89d,76d3c3,8a95f5>TheTrackOfShadow</#>".toGradientColor()).bold()
        .toLegacyText()

    // 中文标志消息
    val CN_LOGO_LEGACY_TEXT = DefaultComponent()
        .append("<#ffd89d,76d3c3,8a95f5>随影寻踪</#>".toGradientColor()).bold()
        .toLegacyText()

    // 英文消息前缀
    val EN_PREFIX_LEGACY_TEXT = DefaultComponent()
        .append(EN_LOGO_LEGACY_TEXT)
        .append("<#ffa3ca,ffb5ef>>></#> ".toGradientColor()).bold()
        .toLegacyText()

    // 中文消息前缀
    val CN_PREFIX_LEGACY_TEXT = DefaultComponent()
        .append(CN_LOGO_LEGACY_TEXT)
        .append("<#ffa3ca,ffb5ef>>></#> ".toGradientColor()).bold()
        .toLegacyText()

    // 插件载入信息
    val PLUGIN_LOAD_LEGACY_TEXT = DefaultComponent()
        .append("<#d5d5ff,9e9eee>正在加载</#> $EN_LOGO_LEGACY_TEXT <#ffd6d6,e14d4d>Beta</#><#f4f4f4,808080>...</#> <#adf0e0,8fbdf4>${Bukkit.getVersion()}</#>".toGradientColor())
        .bold()
        .toLegacyText()

    // 插件卸载信息
    val PLUGIN_DISABLE_LEGACY_TEXT = DefaultComponent()
        .append("<#fcc3f3,ee80b5>正在卸载</#> $EN_LOGO_LEGACY_TEXT <#ffd6d6,e14d4d>Beta</#><#f4f4f4,808080>...</#> <#adf0e0,8fbdf4>${Bukkit.getVersion()}</#>".toGradientColor())
        .bold()
        .toLegacyText()

    // 随机事件
    val RANDOM_EVENT_PREFIX_LEGACY_TEXT = DefaultComponent()
        .append("§f§l随机事件 > ").bold()
        .toLegacyText()
}