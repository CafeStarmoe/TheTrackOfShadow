package top.ycmt.thetrackofshadow.game.event

import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.PhaseState

// 玩家离开事件
object PlayerQuit {
    @SubscribeEvent
    fun onQuit(e: PlayerQuitEvent) {
        val player = e.player
        // 获取玩家所在的游戏
        val playerGamePair = GameManager.getPlayerGame(player) ?: return
        val game = playerGamePair.first
        val playerType = playerGamePair.second
        // 事件流程
        disconnectGame(game, playerType, player) // 游戏断开连接
    }

    // 游戏断开连接
    private fun disconnectGame(game: Game, playerType: GameManager.PlayerType, player: Player) {
        // 确保游戏处于运行阶段
        if (game.phaseState != PhaseState.RUNNING_PHASE) {
            // 踢出玩家
            GameManager.quitGame(player)
            return
        }
        when (playerType) {
            // 玩家重连
            GameManager.PlayerType.PLAYER -> game.reconnectModule.disconnectPlayer(player)
            // 观察者重连
            GameManager.PlayerType.WATCHER -> game.reconnectModule.disconnectWatcher(player)
        }
    }

}