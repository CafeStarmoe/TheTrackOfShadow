package top.ycmt.thetrackofshadow.game.event

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityRegainHealthEvent
import taboolib.common.platform.event.SubscribeEvent
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.PhaseState

// 实体自然回血事件
object EntityRegainHealth {
    @SubscribeEvent
    fun onRegainHealth(e: EntityRegainHealthEvent) {
        // 判断实体是否为玩家
        if (e.entity !is Player) {
            return
        }
        val player = e.entity as Player
        // 获取玩家所在的游戏
        val playerGamePair = GameManager.getPlayerGame(player) ?: return
        val game = playerGamePair.first
        val playerType = playerGamePair.second
        // 如果不是玩家则跳出
        if (playerType != GameManager.PlayerType.PLAYER) {
            return
        }
        // 确保游戏处于运行阶段
        if (game.phaseState != PhaseState.RUNNING_PHASE) {
            return
        }
        // 事件流程
        cancelRegainHealth(e) // 禁止自然回血
    }

    // 禁止自然回血 (游戏机制)
    private fun cancelRegainHealth(e: EntityRegainHealthEvent) {
        // 事件已取消则跳出
        if (e.isCancelled) {
            return
        }
        // 确保是因为和平模式或饥饿感满足而回血
        if (e.regainReason != EntityRegainHealthEvent.RegainReason.REGEN && e.regainReason != EntityRegainHealthEvent.RegainReason.SATIATED) {
            return
        }
        e.isCancelled = true
    }

}