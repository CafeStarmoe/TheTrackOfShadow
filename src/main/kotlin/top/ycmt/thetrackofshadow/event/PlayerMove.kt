package top.ycmt.thetrackofshadow.event

import org.bukkit.event.player.PlayerMoveEvent
import taboolib.common.platform.event.SubscribeEvent
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.CancelState

// 玩家移动事件
object PlayerMove {
    @SubscribeEvent
    fun onPlayerMove(e: PlayerMoveEvent) {
        cancelMove(e) // 禁止玩家移动
    }

    // 禁止玩家移动
    private fun cancelMove(e: PlayerMoveEvent) {
        val player = e.player
        // 获取玩家所在的游戏
        val playerGamePair = GameManager.getPlayerGame(player) ?: return
        val game = playerGamePair.first
        val playerType = playerGamePair.second
        // 如果不是玩家则跳出
        if (playerType != GameManager.PlayerType.PLAYER) {
            return
        }

        // 禁止移动状态
        if (game.cancelModule.containsCancelState(player, CancelState.CANCEL_MOVE)) {
            // 移动的是位置就取消事件
            if (e.from.x != e.to?.x || e.from.y != e.to?.y || e.from.z != e.to?.z) {
                e.isCancelled = true
            }
        }
        // 禁止移动视角状态
        if (game.cancelModule.containsCancelState(player, CancelState.CANCEL_MOVE_ANGLE)) {
            // 移动的是视角就取消事件
            if (e.from.pitch != e.to?.pitch || e.from.yaw != e.to?.yaw) {
                e.isCancelled = true
            }
        }
    }
}