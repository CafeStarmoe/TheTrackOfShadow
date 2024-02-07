package top.ycmt.thetrackofshadow.game.event.random

import org.bukkit.Sound
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.task.AnvilRandomTask
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 铁砧雨随机事件
class AnvilRandom(private val game: Game) : RandomInterface {
    override fun exec() {
        // 开启铁砧雨事件
        game.subTaskModule.submitTask(period = 5L, task = AnvilRandomTask(game, 40))
        // 提示玩家
        game.playerModule.getOnlineUsers().forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "$RANDOM_EVENT_PREFIX_LEGACY_TEXT<#989898,777777>铁砧雨</#>§f已开始在所有玩家上空落铁, 持续<#ff9c9c,de4949>10秒</#>§f!".toGradientColor(),
                ""
            )
        }
    }
}