package top.ycmt.thetrackofshadow.game.flow.random

import org.bukkit.Sound
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.task.LeyLineRandomTask
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 地脉喷涌随机事件
class LeyLineRandom(private val game: Game) : RandomInterface {
    override fun exec() {
        // 开启地脉喷涌事件
        game.subTaskModule.submitTask(period = 1 * 20L, task = LeyLineRandomTask(game, 30L))
        // 提示玩家
        game.playerModule.getOnlineUsers().forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "${LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#cfffcf,9ce3a7>地脉喷涌</#>§f已开始给予所有玩家馈赠, 持续<#ff9c9c,de4949>30秒</#>§f!".toGradientColor(),
                ""
            )
        }
    }

}