package top.ycmt.thetrackofshadow.game.flow.random

import org.bukkit.Sound
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.task.ThunderRandomTask
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 天雷圣裁随机事件
class ThunderRandom(private val game: Game) : RandomInterface {
    override fun exec() {
        // 开启天雷圣裁事件
        game.subTaskModule.submitTask(period = 10L, task = ThunderRandomTask(game, 20L))
        // 提示玩家
        game.playerModule.getOnlineUsers().forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "${RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#c2ffff,a1f4ff>天雷圣裁</#>§f已开始随机挑选玩家落雷, 持续<#ff9c9c,de4949>10秒</#>§f!".toGradientColor(),
                ""
            )
        }
    }

}