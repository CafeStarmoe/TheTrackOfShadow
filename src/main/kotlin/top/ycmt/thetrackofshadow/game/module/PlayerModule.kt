package top.ycmt.thetrackofshadow.game.module

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.sendFailMsg
import top.ycmt.thetrackofshadow.pkg.chat.sendSuccMsg
import top.ycmt.thetrackofshadow.pkg.logger.logger
import java.util.*

// 玩家管理模块
class PlayerModule(private val game: Game) {
    private val players: MutableList<UUID> = mutableListOf() // 玩家uuid列表 记录uuid
    private var onlinePlayersCache: List<Player> = listOf() // 在线玩家对象列表缓存

    // 添加玩家
    fun addPlayer(player: Player) {
        // 校验玩家是否已经加入游戏
        if (players.contains(player.uniqueId)) {
            logger.error("玩家列表已存在该玩家, uuid: ${player.uniqueId}, name: ${player.name}, gameName: ${game.setting.gameName}")
            player.sendFailMsg("你已经加入了游戏${game.setting.gameName}")
            return
        }
        // 校验是否已满人
        if (players.size >= game.setting.maxPlayerCount) {
            logger.error("玩家列表已满人, size: ${players.size}, gameName: ${game.setting.gameName}")
            player.sendFailMsg("游戏${game.setting.gameName}已满人")
            return
        }
        // 玩家列表添加玩家
        players.add(player.uniqueId)
        player.sendSuccMsg("你加入了游戏${game.setting.gameName}")
    }

    // 是否存在玩家
    fun containsPlayer(player: Player): Boolean {
        return players.contains(player.uniqueId)
    }

    // 刷新在线玩家对象列表
    fun refreshOnlinePlayers() {
        onlinePlayersCache = players.mapNotNull { Bukkit.getPlayer(it) }
    }

    // 获取在线玩家列表
    fun getOnlinePlayers(): List<Player> {
        return onlinePlayersCache
    }

    // 初始化玩家
    fun initPlayer(player: Player, cleanItem: Boolean = false) {
        // 设置冒险模式
        player.gameMode = GameMode.ADVENTURE
        // 清除玩家的药水效果
        player.activePotionEffects.forEach {
            player.removePotionEffect(it.type)
        }

        // 设置玩家的基础信息
        player.health = 20.0
        player.foodLevel = 20
        player.level = 0
        player.exp = 0f

        if (cleanItem) {
            player.inventory.clear() // 清空玩家的背包
        }
    }
}
