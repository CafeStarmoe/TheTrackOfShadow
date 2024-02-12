package top.ycmt.thetrackofshadow.game.task

import org.bukkit.Sound
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 反向重生随机事件任务
class ReverseRandomTask(private val game: Game, private var tick: Long) : TaskAbstract() {
    init {
        // 开启反向重生
        game.spawnModule.reverse = true
    }

    override fun run() {
        // 事件结束
        if (tick <= 0) {
            // 关闭反向重生
            game.spawnModule.reverse = false
            // 提示玩家
            game.playerModule.getOnlineUsers().forEach {
                it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                it.sendMessage(
                    "",
                    "${RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#ffa1a1,ff6e6e>反向重生</#>§f现已结束干扰<#ff9c9c,de4949>重生点</#>§f!".toGradientColor(),
                    ""
                )
            }
            this.cancel()
            return
        }
        tick--
    }

}