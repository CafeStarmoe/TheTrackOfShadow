package top.ycmt.thetrackofshadow.game

import top.ycmt.thetrackofshadow.conf.GameSetting
import top.ycmt.thetrackofshadow.pkg.logger.logger

// 游戏管理器
object GameManager {
    // 游戏集合
    private val gameMap: MutableMap<UInt, Game> = mutableMapOf()

    // 游戏id计数器
    private var gameCounter: UInt = 0u

    // 创建游戏
    fun createGame(gameSetting: GameSetting): Game {
        gameCounter += 1u
        val game = Game(gameCounter, gameSetting)
        gameMap[game.gameId] = game
        return game
    }

    // 删除游戏
    fun removeGame(gameId: UInt) {
        val game = gameMap[gameId]
        if (game == null) {
            logger.error("游戏不存在， gameId: $gameId")
            return
        }
        gameMap.remove(gameId)
    }
}