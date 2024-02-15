package top.ycmt.thetrackofshadow.game.module

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.state.PhaseState
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendFailMsg
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendMsg
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendSuccMsg
import top.ycmt.thetrackofshadow.pkg.scoreboard.ScoreBoard
import java.util.*

// 玩家管理模块
class PlayerModule(private val game: Game) {
    private val players = mutableListOf<UUID>() // 玩家uuid列表 记录uuid
    private val watchers = mutableListOf<UUID>() // 观察者uuid列表 记录uuid
    private var onlinePlayersCache = listOf<Player>() // 在线玩家对象列表缓存
    private var alivePlayersCache = listOf<Player>() // 存活且在线玩家对象列表缓存
    private var onlineWatchersCache = listOf<Player>() // 在线观察者对象列表缓存

    // 玩家加入游戏
    private fun playerJoin(player: Player) {
        // 初始化玩家属性
        initPlayer(player)
        // 传送至等待大厅
        player.teleport(game.setting.getLobbyLocation())
        // 刷新玩家列表
        refreshPlayers()
        // 进入提示
        getOnlineUsers().forEach {
            it.sendMsg("<#dcffcc,9adbb1>${player.name}</#><#f5ead0,eee6a1>加入了游戏(</#><#dcffcc,9adbb1>${game.playerModule.getOnlineUsers().size}</#>§f/<#dcffcc,9adbb1>${game.setting.maxPlayerCount}</#><#f5ead0,eee6a1>)</#>".toGradientColor())
        }
    }

    // 玩家退出游戏
    private fun playerQuit(player: Player) {
        // 初始化玩家属性
        initPlayer(player)
        // 清空玩家的计分板
        ScoreBoard.removeScore(player)
        // 传送至离开游戏的位置
        player.teleport(game.setting.getQuitLocation())
        // 刷新玩家列表
        refreshPlayers()
        // 给其他玩家看的退出提示
        getOnlineUsers().forEach {
            it.sendMsg("<#dcffcc,9adbb1>${player.name}</#><#f5ead0,eee6a1>已退出!</#>".toGradientColor())
        }
    }

    // 观察者退出游戏
    private fun watcherQuit(player: Player) {
        // 初始化玩家属性
        initPlayer(player)
        // 清空玩家的计分板
        ScoreBoard.removeScore(player)
        // 传送至离开游戏的位置
        player.teleport(game.setting.getQuitLocation())
    }

    // 观察者加入游戏
    private fun watcherJoin(player: Player) {
        // 初始化玩家属性
        initPlayer(player, gameMode = GameMode.SPECTATOR)
        // 传送至游戏地图重生点
        player.teleport(game.setting.getRespawnLocation())
    }

    // 添加玩家
    fun addPlayer(player: Player) {
        // 校验玩家是否已经加入游戏
        if (players.contains(player.uniqueId)) {
            player.sendFailMsg("你已经加入了此游戏${game.setting.gameName}!")
            return
        }
        // 校验是否已满人
        if (players.size >= game.setting.maxPlayerCount) {
            player.sendFailMsg("游戏${game.setting.gameName}已满人!")
            return
        }
        // 校验游戏阶段是否处于大厅等待阶段
        if (game.phaseState != PhaseState.LOBBY_PHASE) {
            player.sendFailMsg("游戏${game.setting.gameName}已开始, 但你能观战这局游戏!")
            return
        }
        // 玩家列表添加玩家
        players.add(player.uniqueId)
        // 处理玩家加入游戏
        playerJoin(player)
        player.sendSuccMsg("你加入了游戏${game.setting.gameName}")
    }

    // 删除玩家
    fun removePlayer(player: Player) {
        // 校验玩家是否已经加入游戏
        if (!players.contains(player.uniqueId)) {
            player.sendFailMsg("你没有加入此游戏${game.setting.gameName}!")
            return
        }
        // 玩家列表删除玩家
        players.remove(player.uniqueId)
        // 处理玩家离开游戏
        playerQuit(player)
        // 提示玩家
        player.sendSuccMsg("你离开了游戏${game.setting.gameName}")
    }

    // 添加观察者
    fun addWatcher(player: Player) {
        // 校验观察者是否已经加入游戏
        if (watchers.contains(player.uniqueId)) {
            player.sendFailMsg("你已经在观看此游戏${game.setting.gameName}!")
            return
        }
        // 校验游戏阶段是否处于游戏运行阶段
        if (game.phaseState != PhaseState.RUNNING_PHASE) {
            player.sendFailMsg("游戏${game.setting.gameName}未开始, 请耐心等待游戏开始!")
            return
        }
        // 观察者列表添加观察者
        watchers.add(player.uniqueId)
        // 处理观察者加入
        watcherJoin(player)
        // 提示观察者
        player.sendSuccMsg("你已观察者的身份加入了游戏")
    }

    // 删除观察者
    fun removeWatcher(player: Player) {
        // 校验观察者是否已经加入游戏
        if (!watchers.contains(player.uniqueId)) {
            player.sendFailMsg("你没有在观看此游戏${game.setting.gameName}!")
            return
        }
        // 观察者列表删除观察者
        watchers.remove(player.uniqueId)
        // 处理观察者离开游戏
        watcherQuit(player)
        // 提示观察者
        player.sendSuccMsg("你离开了游戏${game.setting.gameName}")
    }

    // 玩家转换观察者
    fun playerToWatcher(player: Player) {
        // 确保玩家加入游戏且不是观察者
        if (!players.contains(player.uniqueId) || watchers.contains(player.uniqueId)) {
            return
        }
        players.remove(player.uniqueId)
        // 清空玩家的计分板
        ScoreBoard.removeScore(player)
        // 加入观察者
        addWatcher(player)
    }

    // 是否存在玩家
    fun containsPlayer(player: Player): Boolean {
        return players.contains(player.uniqueId)
    }

    // 是否存在观察者
    fun containsWatcher(player: Player): Boolean {
        return watchers.contains(player.uniqueId)
    }

    // 刷新玩家对象列表
    fun refreshPlayers() {
        onlinePlayersCache = players.mapNotNull { Bukkit.getPlayer(it) }
        alivePlayersCache = onlinePlayersCache.filter { !game.respawnModule.containsRespawnPlayer(it) }
        onlineWatchersCache = watchers.mapNotNull { Bukkit.getPlayer(it) }
    }

    // 获取在线玩家列表
    fun getOnlinePlayers(): List<Player> {
        return onlinePlayersCache
    }

    // 获取在线玩家列表
    fun getOnlineWatchers(): List<Player> {
        return onlineWatchersCache
    }

    // 获取在线玩家以及观察者列表
    fun getOnlineUsers(): List<Player> {
        return onlinePlayersCache + onlineWatchersCache
    }

    // 获取存活玩家列表
    fun getAlivePlayers(): List<Player> {
        return alivePlayersCache
    }

    // 初始化玩家
    fun initPlayer(player: Player, gameMode: GameMode = GameMode.ADVENTURE) {
        // 设置冒险模式
        player.gameMode = gameMode
        // 清除玩家的药水效果
        player.activePotionEffects.forEach {
            player.removePotionEffect(it.type)
        }

        // 设置玩家的基础信息
        val playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
        player.health = playerMaxHealth
        player.foodLevel = 20
        player.level = 0
        player.exp = 0f

        player.inventory.clear() // 清空玩家的背包
    }
}
