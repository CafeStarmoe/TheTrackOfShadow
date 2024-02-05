package top.ycmt.thetrackofshadow.game.event

import org.bukkit.Sound
import top.ycmt.thetrackofshadow.constant.TeleportAnimationConst.ANIMATION_COUNT
import top.ycmt.thetrackofshadow.constant.TeleportAnimationConst.ANIMATION_TICK
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.state.CancelState
import top.ycmt.thetrackofshadow.pkg.chat.sendMsg
import top.ycmt.thetrackofshadow.pkg.chat.toGradientColor

// 开始游戏事件
class StartEvent(private val game: Game) : EventInterface {
    override val finishTick = ANIMATION_TICK * (ANIMATION_COUNT + 1) + 6 // 运行完毕的tick数
    override val eventMsg = "等待游戏开局" // 事件消息

    override fun exec(leftTick: Long) {
        // 输出提示通知玩家
        when (leftTick) {
            6L -> {
                game.playerModule.getOnlinePlayers().forEach {
                    // 暂时允许飞行
                    it.allowFlight = true
                    it.isFlying = true
                    // 禁止移动
                    game.cancelModule.addPlayerCancelState(it, CancelState.CANCEL_MOVE)
                }
            }

            in 1L..5L -> {
                game.playerModule.getOnlinePlayers().forEach {
                    it.sendMsg("<#f5ead0,eee6a1>游戏将在</#><#f7c79c,ef987d>${leftTick}秒</#><#f5ead0,eee6a1>后开局!</#>".toGradientColor())
                    it.sendTitle(
                        "<#ff9c9c,de4949>$leftTick</#>".toGradientColor(),
                        "<#f5ead0,eee6a1>准备好战斗!</#>".toGradientColor(),
                        0,
                        15,
                        5
                    )
                    it.playSound(it, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                }
            }

            0L -> {
                game.playerModule.getOnlinePlayers().forEach {
                    // 允许移动
                    game.cancelModule.removePlayerCancelState(it, CancelState.CANCEL_MOVE)
                    // 不允许飞行
                    it.allowFlight = false
                    it.isFlying = false
                    // 提示玩家
                    it.sendMsg("<#f5ead0,eee6a1>游戏正式开始,</#> <#c1c1ff,7373ff>下蹲+副手键</#> <#f5ead0,eee6a1>打开菜单~</#>".toGradientColor())
                    it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                }
            }
        }
    }

}