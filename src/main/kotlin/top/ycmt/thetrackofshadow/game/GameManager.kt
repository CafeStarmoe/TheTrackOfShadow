package top.ycmt.thetrackofshadow.game

import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.config.GameSetting
import top.ycmt.thetrackofshadow.pkg.logger.logger
import top.ycmt.thetrackofshadow.pkg.message.sendFailedMessage

// 游戏管理器
object GameManager {
    // 游戏列表
    val gameMap: MutableMap<String, Game> = mutableMapOf()

    // 创建游戏
    fun createGame(gameSetting: GameSetting): Game? {
        // 校验游戏是否已经创建
        if (gameMap.contains(gameSetting.gameName)) {
            logger.error("游戏已经创建, gameName: ${gameSetting.gameName}")
            return null
        }
        val game = Game(gameSetting)
        // 记录创建的游戏
        gameMap[gameSetting.gameName] = game
        return game
    }

    // 删除游戏
    fun removeGame(gameName: String) {
        // 校验游戏是否存在
        val game = gameMap[gameName]
        if (game == null) {
            logger.error("游戏不存在, gameName: $gameName")
            return
        }
        // 删除记录
        gameMap.remove(gameName)
        // TODO 停止游戏
    }

    // 玩家加入游戏
    fun joinGame(gameName: String, player: Player) {
        // 校验游戏是否存在
        val game = gameMap[gameName]
        if (game == null) {
            logger.error("游戏不存在, gameName: $gameName")
            player.sendFailedMessage("游戏${gameName}不存在。")
            return
        }
        // TODO 校验玩家是否已经加入了一个游戏
        // 游戏添加玩家
        game.playerModule.addPlayer(player)
    }
}