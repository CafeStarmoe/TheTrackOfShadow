package top.ycmt.thetrackofshadow.game.flow

import org.bukkit.Sound
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.state.CancelState
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendMsg
import java.util.concurrent.TimeUnit

// 开启PVP事件
class PVPFlow(private val game: Game) : FlowInterface {
    override val finishTick = TimeUnit.MINUTES.toSeconds(10) // 运行完毕的tick数
    override val eventMsg = "等待开启PVP" // 事件消息

    override fun exec(leftTick: Long) {
        // 根据时间改变颜色
        val leftTickColor = when (leftTick) {
            10L -> "c1c1ff,7373ff"
            in 4L..5L -> "f7c79c,ef987d"
            in 1L..3L -> "ff9c9c,de4949"
            else -> ""
        }

        // 输出提示通知玩家
        when (leftTick) {
            in 1L..5L, 10L -> {
                game.playerModule.getOnlineUsers().forEach {
                    it.sendMsg("<#d6a1ff,ba6df8>PVP将在</#><#$leftTickColor>${leftTick}秒</#><#d6a1ff,ba6df8>后开启!</#>".toGradientColor())
                    it.playSound(it, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                }
            }

            0L -> {
                // 清除禁止pvp状态
                game.cancelModule.removeGlobalCancelState(CancelState.CANCEL_PVP)
                game.playerModule.getOnlineUsers().forEach {
                    it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                    it.sendMessage(
                        "",
                        "${LegacyTextConst.PLAYER_PVP_PREFIX_LEGACY_TEXT}§fPVP已开启, 准备好迎接挑战了吗?".toGradientColor(),
                        ""
                    )
                }
            }
        }
    }

}