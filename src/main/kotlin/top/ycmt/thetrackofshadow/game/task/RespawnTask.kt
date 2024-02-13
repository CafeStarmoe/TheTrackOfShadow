package top.ycmt.thetrackofshadow.game.task

import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendMsg

// 玩家重生任务
class RespawnTask(private val game: Game, private val player: Player, private var tick: Long) : TaskAbstract() {

    override fun run() {
        // 玩家不在线 或 玩家退出了游戏 则取消任务
        if (!player.isOnline || !game.playerModule.containsPlayer(player)) {
            this.cancel()
            return
        }

        // 时间到了则复活玩家
        if (tick <= 0) {
            // 重生完毕
            game.respawnModule.respawnFinish(player)
            this.cancel()
            return
        } else {
            player.sendTitle(
                "<#ff9c9c,de4949>你死了!</#>".toGradientColor(),
                "<#ffefbb,e3ce82>你将在</#><#ff9c9c,de4949>${tick}秒</#><#ffefbb,e3ce82>后重生!</#>".toGradientColor(),
                0,
                25,
                0
            )
            player.sendMsg("<#ffefbb,e3ce82>你将在</#><#ff9c9c,de4949>${tick}秒</#><#ffefbb,e3ce82>后重生!</#>".toGradientColor())
        }
        tick--
    }

    override fun cancel() {
        super.cancel()
        // 取消重生
        game.respawnModule.cancelRespawn(player)
    }

}