package top.ycmt.thetrackofshadow.pkg.chat

import taboolib.module.chat.ComponentText
import taboolib.module.chat.impl.DefaultComponent
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.CN_PREFIX_LEGACY_TEXT
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 发送玩家格式化消息
object SendMsg {
    // 发送自定义格式的消息
    fun org.bukkit.entity.Player.sendMsg(vararg msgList: String) {
        msgList.forEach {
            this.sendMessage(CN_PREFIX_LEGACY_TEXT + it)
        }
    }

    // 发送自定义格式的消息
    fun org.bukkit.entity.Player.sendSpigotMsg(vararg msgList: ComponentText) {
        msgList.forEach {
            val component = DefaultComponent()
                .append(CN_PREFIX_LEGACY_TEXT)
                .append(it)
            this.spigot().sendMessage(component.toSpigotObject())
        }
    }

    // 发送成功格式的消息
    fun org.bukkit.entity.Player.sendSuccMsg(vararg msgList: String) {
        msgList.forEach {
            this.sendMsg("<#deffd2,bee8ff>$it</#>".toGradientColor())
        }
    }

    // 发送失败格式的消息
    fun org.bukkit.entity.Player.sendFailMsg(vararg msgList: String) {
        msgList.forEach {
            this.sendMsg("<#ffcbcb,ff7093>$it</#>".toGradientColor())
        }
    }
}

