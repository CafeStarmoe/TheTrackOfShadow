package top.ycmt.thetrackofshadow.pkg.sendmsg

import taboolib.module.chat.toGradientColor
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.CN_PREFIX_LEGACY_TEXT

// 发送失败格式的消息
fun org.bukkit.entity.Player.sendMsg(vararg msgList: String) {
    msgList.forEach {
        this.sendMessage(CN_PREFIX_LEGACY_TEXT + it)
    }
}

// 发送成功格式的消息
fun org.bukkit.entity.Player.sendSuccMsg(vararg msgList: String) {
    msgList.forEach {
        this.sendMsg(it.toGradientColor(listOf(0xdeffd2, 0xbee8ff)))
    }
}

// 发送失败格式的消息
fun org.bukkit.entity.Player.sendFailMsg(vararg msgList: String) {
    msgList.forEach {
        this.sendMsg(it.toGradientColor(listOf(0xffcbcb, 0xff7093)))
    }
}