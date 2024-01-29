package top.ycmt.thetrackofshadow.game

import taboolib.common.platform.function.warning

// 游戏管理器
object GameManager {
    // 游戏集合
    private val gameMap: MutableMap<UInt, Game> = mutableMapOf()

    // 游戏id计数器
    private var gameCounter: UInt = 0u

    // 创建游戏
    fun createGame(gameName: String): Game {
        val game = Game(gameCounter++, gameName)
        gameMap[game.gameId] = game
        return game
    }

    // 删除游戏
    fun removeGame(gameId: UInt) {
        val game = gameMap[gameId]
        if (game == null) {
            warning("游戏不存在, gameId: $gameId")
            return
        }
        gameMap.remove(gameId)
    }
}