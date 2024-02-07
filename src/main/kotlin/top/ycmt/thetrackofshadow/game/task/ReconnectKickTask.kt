package top.ycmt.thetrackofshadow.game.task

import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendMsg

// 玩家重连超时踢出任务
class ReconnectKickTask(private val game: Game, private val player: Player, private var tick: Long) : TaskAbstract() {
    override fun run() {
        // 倒计时到了则删除玩家
        if (tick <= 0) {
            game.playerModule.removePlayer(player)
            game.playerModule.getOnlineUsers().forEach {
                it.sendMsg("§c§l玩家${player.name}因长时间未重新连接已被踢出游戏!")
            }
        }
        // 如果玩家已重连那就取消任务
        if (tick <= 0 || player.isOnline) {
            // 取消重连踢出
            game.reconnectModule.removeReconnectKickPlayer(player)
            this.cancel()
            return
        }
        tick--
    }

}