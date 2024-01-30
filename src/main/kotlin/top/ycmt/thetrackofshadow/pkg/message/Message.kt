package top.ycmt.thetrackofshadow.pkg.message

import taboolib.module.chat.toGradientColor
import top.ycmt.thetrackofshadow.common.constant.PrefixConst

// 发送成功格式的消息
fun org.bukkit.entity.Player.sendSuccessMessage(vararg msgList: String) {
    msgList.forEach {
        this.sendMessage(PrefixConst.PrefixMessage + it.toGradientColor(listOf(0xdeffd2, 0xbee8ff)))
    }
}

// 发送失败格式的消息
fun org.bukkit.entity.Player.sendFailedMessage(vararg msgList: String) {
    msgList.forEach {
        this.sendMessage(PrefixConst.PrefixMessage + it.toGradientColor(listOf(0xffcbcb, 0xff7093)))
    }
}