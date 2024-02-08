package top.ycmt.thetrackofshadow.game.task

import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.game.Game

// 玩家重生后保护任务
class RespawnProtectCleanTask(private val game: Game, private val player: Player, private var tick: Long) :
    TaskAbstract() {
    override fun run() {
        if (tick <= 0) {
            // 取消重生后保护
            game.respawnModule.removeProtect(player)
            this.cancel()
            return
        }
        tick--
    }
}