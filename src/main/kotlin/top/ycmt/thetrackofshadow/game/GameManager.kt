package top.ycmt.thetrackofshadow.game

import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.config.GameSetting
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendFailMsg
import top.ycmt.thetrackofshadow.pkg.logger.Logger

// 游戏管理器
object GameManager {
    // 游戏列表
    private val gameMap: MutableMap<String, Game> = mutableMapOf()

    // 创建游戏
    fun createGame(gameSetting: GameSetting): Game? {
        // 校验游戏是否已经创建
        if (gameMap.contains(gameSetting.gameName)) {
            Logger.error("游戏已存在, gameName: ${gameSetting.gameName}")
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
            Logger.error("游戏不存在, gameName: $gameName")
            return
        }
        // 停止游戏
        game.stopGame()
        // 删除记录
        gameMap.remove(gameName)
    }

    // 删除所有游戏
    fun removeGames() {
        gameMap.forEach {
            removeGame(it.value.setting.gameName)
        }
    }

    // 玩家加入游戏
    fun joinGame(gameName: String, player: Player) {
        // 校验游戏是否存在
        val game = gameMap[gameName]
        if (game == null) {
            player.sendFailMsg("游戏${gameName}不存在!")
            return
        }
        // 校验玩家是否已经加入了一个游戏
        val playerGamePair = getPlayerGame(player)
        if (playerGamePair != null) {
            player.sendFailMsg("你已经加入了一个游戏${playerGamePair.first.setting.gameName}!")
            return
        }
        // 游戏添加玩家
        game.playerModule.addPlayer(player)
    }

    // 玩家或观察者离开游戏
    fun quitGame(player: Player) {
        // 校验玩家是否已经加入了一个游戏
        val playerGamePair = getPlayerGame(player)
        if (playerGamePair == null) {
            player.sendFailMsg("你没有加入任何游戏!")
            return
        }
        val game = playerGamePair.first
        val playerType = playerGamePair.second
        when (playerType) {
            // 游戏删除玩家
            PlayerType.PLAYER -> game.playerModule.removePlayer(player)
            // 游戏删除观察者
            PlayerType.WATCHER -> game.playerModule.removeWatcher(player)
        }
    }

    // 玩家观看游戏
    fun watchGame(gameName: String, player: Player) {
        // 校验游戏是否存在
        val game = gameMap[gameName]
        if (game == null) {
            player.sendFailMsg("游戏${gameName}不存在!")
            return
        }
        // 校验玩家是否已经加入了一个游戏
        val playerGamePair = getPlayerGame(player)
        if (playerGamePair != null) {
            player.sendFailMsg("你已经加入了一个游戏${playerGamePair.first.setting.gameName}!")
            return
        }
        // 游戏添加观察者
        game.playerModule.addWatcher(player)
    }

    // 获取玩家所在的游戏
    fun getPlayerGame(player: Player): Pair<Game, PlayerType>? {
        for (game in gameMap.values) {
            // 判断是否为玩家
            if (game.playerModule.containsPlayer(player)) {
                return Pair(game, PlayerType.PLAYER)
            }
            // 判断是否为观察者
            if (game.playerModule.containsWatcher(player)) {
                return Pair(game, PlayerType.WATCHER)
            }
        }
        return null
    }

    // 获取游戏名称列表
    fun getGameNames(): List<String> {
        return gameMap.keys.toList()
    }

    // 游戏玩家类型
    enum class PlayerType {
        PLAYER, // 玩家
        WATCHER, // 观察者
    }

}