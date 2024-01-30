package top.ycmt.thetrackofshadow.game.module

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.logger.logger
import top.ycmt.thetrackofshadow.pkg.message.sendFailedMessage
import top.ycmt.thetrackofshadow.pkg.message.sendSuccessMessage
import java.util.*

// 游戏玩家模块
class PlayerModule(private val game: Game) {
    // 玩家列表 记录uuid
    private val playerList: MutableList<UUID> = mutableListOf()

    // 添加玩家
    fun addPlayer(player: Player) {
        // 校验玩家是否已经加入游戏
        if (playerList.contains(player.uniqueId)) {
            logger.error("玩家列表已存在该玩家, uuid: ${player.uniqueId}, name: ${player.name}, gameName: ${game.gameSetting.gameName}")
            player.sendFailedMessage("你已经加入了游戏${game.gameSetting.gameName}")
            return
        }
        // 校验是否已满人
        if (playerList.size >= game.gameSetting.maxPlayerCount) {
            logger.error("玩家列表已满人, size: ${playerList.size}, gameName: ${game.gameSetting.gameName}")
            player.sendFailedMessage("游戏${game.gameSetting.gameName}已满人")
            return
        }
        // 玩家列表添加玩家
        playerList.add(player.uniqueId)
        player.sendSuccessMessage("你加入了游戏${game.gameSetting.gameName}")
    }

    // 获取在线游戏玩家列表
    fun getOnlinePlayerList(): List<Player> {
        return playerList.mapNotNull { Bukkit.getPlayer(it) }
    }
}
