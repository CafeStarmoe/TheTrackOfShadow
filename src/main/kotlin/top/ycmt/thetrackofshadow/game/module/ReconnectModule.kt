package top.ycmt.thetrackofshadow.game.module

import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.task.ReconnectKickTask
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendMsg
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendSuccMsg
import top.ycmt.thetrackofshadow.pkg.logger.Logger
import top.ycmt.thetrackofshadow.pkg.scoreboard.ScoreBoard
import java.util.*
import java.util.concurrent.TimeUnit

// 玩家重连管理模块
class ReconnectModule(private val game: Game) {
    private val reconnectKickPlayers = mutableMapOf<UUID, ReconnectKickTask>() // 等待重连踢出的玩家uuid列表
    private val playersInv = mutableMapOf<UUID, Array<ItemStack>>() // 玩家退出时的物品栏

    // 玩家断开连接
    fun disconnectPlayer(player: Player) {
        // 清空玩家的计分板
        ScoreBoard.removeScore(player)
        // 判断玩家是否正在重生状态
        if (reconnectKickPlayers.contains(player.uniqueId)) {
            // 取消正在运行的重生任务
            reconnectKickPlayers[player.uniqueId]?.cancel()
            reconnectKickPlayers.remove(player.uniqueId)
        }
        // 如果玩家的物品栏存在代表玩家上次的重连未完成 不覆盖
        if (!playersInv.contains(player.uniqueId)) {
            // 保存玩家的物品栏
            playersInv[player.uniqueId] = player.inventory.contents
        }
        // 重连超时踢出任务
        val task = ReconnectKickTask(game, player, TimeUnit.MINUTES.toSeconds(5))
        // 添加重连的玩家
        reconnectKickPlayers[player.uniqueId] = task
        // 提交玩家重连超时踢出任务
        game.subTaskModule.submitTask(period = 1 * 20L, task = task)
        // 提示所有玩家
        game.playerModule.getOnlineUsers().forEach {
            it.sendMsg("<#dcffcc,9adbb1>${player.name}</#><#f5ead0,eee6a1>断开连接!</#>".toGradientColor())
        }
    }

    // 玩家重新连接
    fun reconnectPlayer(player: Player) {
        // 还原玩家离线前的物品栏
        val playerInv = playersInv[player.uniqueId]
        // 确保物品栏获取成功
        if (playerInv == null) {
            Logger.error("记录的玩家物品栏为空, uuid: ${player.uniqueId}, name: ${player.name}")
            return
        }
        player.inventory.contents = playerInv
        // 删除物品栏记录
        playersInv.remove(player.uniqueId)
        // 判断是否可以重生
        if (game.respawnModule.isRespawnable()) {
            // 玩家重生
            game.respawnModule.respawnPlayer(player)
        } else {
            // 最终死亡那就是变成观察者
            game.playerModule.playerToWatcher(player)
        }
        // 提示所有玩家
        game.playerModule.getOnlineUsers().forEach {
            it.sendMsg("<#dcffcc,9adbb1>${player.name}</#><#f5ead0,eee6a1>重新连接!</#>".toGradientColor())
        }
    }

    // 观察者断开连接
    fun disconnectWatcher(player: Player) {
        // 清空玩家的计分板
        ScoreBoard.removeScore(player)
    }

    // 观察者重新连接
    fun reconnectWatcher(player: Player) {
        // 初始化玩家属性
        game.playerModule.initPlayer(player, gameMode = GameMode.SPECTATOR)
        // 传送至游戏地图重生点
        player.teleport(game.setting.getRespawnLocation())
        player.sendSuccMsg("你已观察者的身份重连游戏!")
    }

    // 取消重连踢出
    fun removeReconnectKickPlayer(player: Player) {
        reconnectKickPlayers.remove(player.uniqueId)
    }

}