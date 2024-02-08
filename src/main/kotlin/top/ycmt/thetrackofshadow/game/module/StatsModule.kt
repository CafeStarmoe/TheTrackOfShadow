package top.ycmt.thetrackofshadow.game.module

import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.logger.Logger
import java.util.*

// 玩家统计信息管理模块
class StatsModule(private val game: Game) {
    private val playerStatsInfos: MutableMap<UUID, StatsInfo> = mutableMapOf() // 玩家统计信息uuid列表

    // 初始化玩家统计信息
    fun initPlayersStats(players: List<Player>) {
        players.forEach {
            playerStatsInfos[it.uniqueId] = StatsInfo()
        }
    }

    // 获取玩家统计信息
    fun getPlayerStats(player: Player): StatsInfo? {
        val statsInfo = playerStatsInfos[player.uniqueId]
        // 确保玩家统计信息已初始化
        if (statsInfo == null) {
            Logger.error("玩家统计信息未初始化, uuid: ${player.uniqueId}, name: ${player.name}")
            return null
        }
        return statsInfo
    }

    // 玩家统计信息
    data class StatsInfo(
        var deathCount: Int = 0, // 死亡次数
        var killPlayerCount: Int = 0, // 击杀玩家数
        var findChestCount: Int = 0, // 找到宝箱数
    )
}
