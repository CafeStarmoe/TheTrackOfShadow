package top.ycmt.thetrackofshadow.game.module

import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendMsg
import top.ycmt.thetrackofshadow.pkg.logger.Logger
import java.util.*

// 玩家积分管理模块
class ScoreModule(private val game: Game) {
    private val playerScores = HashMap<UUID, Int>() // 玩家积分uuid列表
    var scoreMultiple = 1.0 // 积分倍率
    var deductScore = true // 是否扣除积分

    // 初始化玩家积分
    fun initPlayersScore(players: List<Player>) {
        players.forEach {
            playerScores[it.uniqueId] = 0
        }
    }

    // 增加玩家积分
    fun addPlayerScore(player: Player, addScore: Int) {
        // 不能增加负数
        if (addScore < 0) {
            return
        }
        val playerScore = playerScores[player.uniqueId]
        // 确保玩家积分已初始化
        if (playerScore == null) {
            Logger.error("玩家积分未初始化, uuid: ${player.uniqueId}, name: ${player.name}")
            return
        }
        // 积分 计算积分倍率
        val score = (addScore * scoreMultiple).toInt()
        // 修改积分
        playerScores[player.uniqueId] = (playerScore + score).coerceAtLeast(0)
        player.sendMsg("<#deffd2,bee8ff>+${score}积分✫</#>".toGradientColor())
    }

    // 减少玩家积分
    fun cutPlayerScore(player: Player, cutScore: Int) {
        // 不能减少负数
        if (cutScore < 0) {
            return
        }
        val playerScore = playerScores[player.uniqueId]
        // 确保玩家积分已初始化
        if (playerScore == null) {
            Logger.error("玩家积分未初始化, uuid: ${player.uniqueId}, name: ${player.name}")
            return
        }
        // 修改积分
        playerScores[player.uniqueId] = (playerScore - cutScore).coerceAtLeast(0)
        // 提示玩家
        player.sendMsg("<#ffcbcb,ff7093>-${cutScore}积分✫</#>".toGradientColor())
    }

    // 获取玩家积分
    fun getPlayerScore(player: Player): Int {
        val playerScore = playerScores[player.uniqueId]
        // 确保玩家积分已初始化
        if (playerScore == null) {
            Logger.error("玩家积分未初始化, uuid: ${player.uniqueId}, name: ${player.name}")
            return 0
        }
        // 修改积分
        return playerScores[player.uniqueId] ?: 0
    }

    // 获取积分排行
    fun getScoreRank(): List<Pair<UUID, Int>> {
        val scoreRank = mutableListOf<Pair<UUID, Int>>()

        // 排序玩家积分
        playerScores.forEach {
            scoreRank.add(Pair(it.key, it.value))
        }
        scoreRank.sortByDescending { it.second }

        return scoreRank
    }

}