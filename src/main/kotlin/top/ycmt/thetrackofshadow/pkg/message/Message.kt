package top.ycmt.thetrackofshadow.pkg.message

import taboolib.module.chat.toGradientColor
import top.ycmt.thetrackofshadow.constant.MessageConst

// 发送失败格式的消息
fun org.bukkit.entity.Player.sendMsg(vararg msgList: String) {
    msgList.forEach {
        this.sendMessage(MessageConst.CNPrefixMessage + it)
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