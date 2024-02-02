package top.ycmt.thetrackofshadow.game.module

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.logger.logger
import top.ycmt.thetrackofshadow.pkg.message.sendFailMsg
import top.ycmt.thetrackofshadow.pkg.message.sendSuccMsg
import java.util.*

// 游戏玩家模块
class PlayerModule(private val game: Game) {
    // 玩家uuid列表 记录uuid
    private val playerUUIDs: MutableList<UUID> = mutableListOf()

    // 在线玩家对象列表
    var onlinePLayers: List<Player> = listOf()

    // 添加玩家
    fun addPlayer(player: Player) {
        // 校验玩家是否已经加入游戏
        if (playerUUIDs.contains(player.uniqueId)) {
            logger.error("玩家列表已存在该玩家, uuid: ${player.uniqueId}, name: ${player.name}, gameName: ${game.gameSetting.gameName}")
            player.sendFailMsg("你已经加入了游戏${game.gameSetting.gameName}")
            return
        }
        // 校验是否已满人
        if (playerUUIDs.size >= game.gameSetting.maxPlayerCount) {
            logger.error("玩家列表已满人, size: ${playerUUIDs.size}, gameName: ${game.gameSetting.gameName}")
            player.sendFailMsg("游戏${game.gameSetting.gameName}已满人")
            return
        }
        // 玩家列表添加玩家
        playerUUIDs.add(player.uniqueId)
        player.sendSuccMsg("你加入了游戏${game.gameSetting.gameName}")
    }

    // 刷新在线玩家对象列表
    fun refreshOnlinePlayers() {
        onlinePLayers = playerUUIDs.mapNotNull { Bukkit.getPlayer(it) }
    }
}
