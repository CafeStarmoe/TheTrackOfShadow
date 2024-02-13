package top.ycmt.thetrackofshadow.game.flow.random

import org.bukkit.Sound
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.task.RottenRandomTask
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 开摆随机事件
class RottenRandom(private val game: Game) : RandomInterface {
    override fun exec() {
        // 开启地脉喷涌事件
        game.subTaskModule.submitTask(period = 1 * 20L, task = RottenRandomTask(game, 10L))
        // 提示玩家
        game.playerModule.getOnlineUsers().forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "${LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#beffeb,70e5bb>开摆</#>§f已随机挑选玩家至重生点且无法离开, 持续<#ff9c9c,de4949>10秒</#>§f!".toGradientColor(),
                ""
            )
        }
    }

}