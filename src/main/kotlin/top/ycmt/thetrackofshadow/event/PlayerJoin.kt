package top.ycmt.thetrackofshadow.event

import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.PhaseState

// 玩家加入事件
object PlayerJoin {
    @SubscribeEvent
    fun onJoin(e: PlayerJoinEvent) {
        reconnectGame(e) // 游戏重新连接
    }

    // 游戏重新连接
    private fun reconnectGame(e: PlayerJoinEvent) {
        val player = e.player
        // 获取玩家所在的游戏
        val playerGamePair = GameManager.getPlayerGame(player) ?: return
        val game = playerGamePair.first
        val playerType = playerGamePair.second
        // 确保游戏处于运行阶段
        if (game.phaseState != PhaseState.RUNNING_PHASE) {
            // 踢出玩家
            GameManager.quitGame(player)
            return
        }
        when (playerType) {
            // 玩家重连
            GameManager.PlayerType.PLAYER -> game.reconnectModule.reconnectPlayer(player)
            // 观察者重连
            GameManager.PlayerType.WATCHER -> game.reconnectModule.reconnectWatcher(player)
        }
    }

}