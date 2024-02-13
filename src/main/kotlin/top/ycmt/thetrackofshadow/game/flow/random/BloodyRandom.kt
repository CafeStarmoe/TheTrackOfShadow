package top.ycmt.thetrackofshadow.game.flow.random

import org.bukkit.Sound
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.task.BloodyRandomTask
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 血色之夜随机事件
class BloodyRandom(private val game: Game) : RandomInterface {
    override fun exec() {
        // 开启血色之夜事件
        game.subTaskModule.submitTask(period = 1 * 20L, task = BloodyRandomTask(game, 30L))
        // 提示玩家
        game.playerModule.getOnlineUsers().forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "${LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT}§4血色之夜§f已开始入侵现实, 持续<#ff9c9c,de4949>30秒</#>§f!".toGradientColor(),
                ""
            )
        }
    }

}