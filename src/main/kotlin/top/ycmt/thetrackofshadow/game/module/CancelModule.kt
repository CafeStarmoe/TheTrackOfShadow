package top.ycmt.thetrackofshadow.game.module

import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.state.CancelState
import top.ycmt.thetrackofshadow.pkg.logger.Logger
import java.util.*

// 玩家禁止状态管理模块
class CancelModule(private val game: Game) {
    // 玩家禁止状态集合
    private val playerCancelStates: MutableMap<UUID, MutableList<CancelState>> = mutableMapOf()

    // 获取玩家禁止状态列表
    private fun getPlayerStates(player: Player): MutableList<CancelState>? {
        // 列表不存在则创建列表
        if (!playerCancelStates.contains(player.uniqueId)) {
            playerCancelStates[player.uniqueId] = mutableListOf()
        }
        return playerCancelStates[player.uniqueId]
    }

    // 添加玩家禁止状态
    fun addPlayerCancelState(player: Player, vararg cancelStates: CancelState) {
        // 玩家禁止状态列表
        val stateList = getPlayerStates(player)
        if (stateList == null) {
            Logger.error("玩家禁止状态列表为空, uuid: ${player.uniqueId}, name: ${player.name}")
            return
        }
        for (state in cancelStates) {
            // 添加禁止状态
            stateList.add(state)
        }
    }

    // 删除玩家禁止状态
    fun removePlayerCancelState(player: Player, vararg cancelStates: CancelState) {
        // 玩家禁止状态列表
        val stateList = getPlayerStates(player)
        if (stateList == null) {
            Logger.error("玩家禁止状态列表为空, uuid: ${player.uniqueId}, name: ${player.name}")
            return
        }
        for (state in cancelStates) {
            // 添加禁止状态
            stateList.remove(state)
        }
    }

    // 玩家是否拥有该禁止状态
    fun containsPlayerCancelState(player: Player, cancelState: CancelState): Boolean {
        // 玩家禁止状态列表
        val stateList = getPlayerStates(player)
        if (stateList == null) {
            Logger.error("玩家禁止状态列表为空, uuid: ${player.uniqueId}, name: ${player.name}")
            return false
        }
        return stateList.contains(cancelState)
    }

}