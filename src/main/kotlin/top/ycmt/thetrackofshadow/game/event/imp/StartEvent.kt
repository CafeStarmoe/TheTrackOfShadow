package top.ycmt.thetrackofshadow.game.event.imp

import org.bukkit.Sound
import taboolib.module.chat.toGradientColor
import top.ycmt.thetrackofshadow.constant.TeleportAnimationConst.ANIMATION_COUNT
import top.ycmt.thetrackofshadow.constant.TeleportAnimationConst.ANIMATION_TICK
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.event.EventAbstract
import top.ycmt.thetrackofshadow.game.state.CancelState
import top.ycmt.thetrackofshadow.pkg.sendmsg.sendMsg

// 开始游戏事件
class StartEvent(override var game: Game) : EventAbstract() {
    override val finishTick: Long =
        ANIMATION_TICK * (ANIMATION_COUNT + 1) + 6 // 运行完毕的tick数
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
                    it.sendMsg(
                        "游戏将在".toGradientColor(listOf(0xf5ead0, 0xeee6a1)) + "${leftTick}秒".toGradientColor(
                            listOf(
                                0xf7c79c,
                                0xef987d
                            )
                        ) + "后开局!".toGradientColor(listOf(0xf5ead0, 0xeee6a1))
                    )
                    it.sendTitle(
                        "$leftTick".toGradientColor(listOf(0xff9c9c, 0xde4949)),
                        "准备好战斗!".toGradientColor(listOf(0xf5ead0, 0xeee6a1)),
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
                    it.sendMsg(
                        "游戏正式开始, ".toGradientColor(listOf(0xf5ead0, 0xeee6a1)) + "下蹲+副手键".toGradientColor(
                            listOf(
                                0xc1c1ff,
                                0x7373ff
                            )
                        ) + " 打开菜单~".toGradientColor(listOf(0xf5ead0, 0xeee6a1))
                    )
                    it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                }
            }
        }
    }

}