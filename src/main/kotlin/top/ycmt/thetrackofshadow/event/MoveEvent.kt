package top.ycmt.thetrackofshadow.event

import org.bukkit.event.player.PlayerMoveEvent
import taboolib.common.platform.event.SubscribeEvent
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.CancelState

object MoveEvent {
    @SubscribeEvent(ignoreCancelled = true)
    fun onMove(e: PlayerMoveEvent) {
        val player = e.player
        val game = GameManager.getPlayerGame(player) ?: return

        // 禁止移动状态
        if (game.cancelModule.containsPlayerCancelState(player, CancelState.CANCEL_MOVE)) {
            // 移动的是位置就取消事件
            if (e.from.x != e.to?.x || e.from.y != e.to?.y || e.from.z != e.to?.z) {
                e.isCancelled = true
            }
        }
        // 禁止移动视角状态
        if (game.cancelModule.containsPlayerCancelState(player, CancelState.CANCEL_MOVE_ANGLE)) {
            // 移动的是视角就取消事件
            if (e.from.pitch != e.to?.pitch || e.from.yaw != e.to?.yaw) {
                e.isCancelled = true
            }
        }

    }
}