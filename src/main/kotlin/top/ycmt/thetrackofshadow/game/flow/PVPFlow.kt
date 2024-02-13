package top.ycmt.thetrackofshadow.game.flow

import org.bukkit.Sound
import taboolib.module.chat.impl.DefaultComponent
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
        // 输出提示通知玩家
        when (leftTick) {
            in 1L..5L, 10L -> {
                game.playerModule.getOnlineUsers().forEach {
                    it.sendMsg("<#c67070,ac3838>PVP将在${leftTick}秒<#c67070,ac3838>后开启!</#>".toGradientColor())
                    it.playSound(it, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                }
            }

            0L -> {
                // 清除禁止pvp状态
                game.cancelModule.removeGlobalCancelState(CancelState.CANCEL_PVP)
                // PVP开启前缀
                val prefixText = DefaultComponent()
                    .append("<#c67070,ac3838>PVP开启 > </#>".toGradientColor()).bold()
                    .toLegacyText()
                game.playerModule.getOnlineUsers().forEach {
                    it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                    it.sendMessage(
                        "",
                        "$prefixText§fPVP已开启, 准备好迎接挑战了吗?".toGradientColor(),
                        ""
                    )
                }
            }
        }
    }

}