package top.ycmt.thetrackofshadow.game.flow.random

import org.bukkit.Sound
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.task.ReverseRandomTask
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 反向重生随机事件
class ReverseRandom(private val game: Game) : RandomInterface {
    override fun exec() {
        // 开启反向重生事件
        game.subTaskModule.submitTask(period = 1 * 20L, task = ReverseRandomTask(game, 10L))
        // 提示玩家
        game.playerModule.getOnlineUsers().forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "${RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#ffa1a1,ff6e6e>反向重生</#>§f已开始干扰重生点, 持续<#ff9c9c,de4949>10秒</#>§f!".toGradientColor(),
                ""
            )
        }
    }

}