package top.ycmt.thetrackofshadow.game.event

import org.bukkit.entity.Player
import org.bukkit.event.entity.FoodLevelChangeEvent
import taboolib.common.platform.event.SubscribeEvent
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.PhaseState

// 玩家饱食度改变事件
object FoodLevelChange {
    @SubscribeEvent
    fun onFoodLevelChange(e: FoodLevelChangeEvent) {
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
        // 确保游戏处于大厅等待阶段
        if (game.phaseState != PhaseState.LOBBY_PHASE) {
            return
        }
        // 事件流程
        lobbyFoodLevelFixed(e)
    }

    // 大厅饥饿值固定
    private fun lobbyFoodLevelFixed(e: FoodLevelChangeEvent) {
        // 事件已取消则跳出
        if (e.isCancelled) {
            return
        }
        // 取消变动饱食度的事件
        e.isCancelled = true
    }

}