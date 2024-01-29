package top.ycmt.thetrackofshadow.game.runnable

import org.bukkit.scheduler.BukkitRunnable
import taboolib.platform.util.bukkitPlugin
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.logger.logger

// 大厅等待阶段定时器
class LobbyPhase(game: Game) : BukkitRunnable() {
    // 剩余等待时间
    var remainTime: UInt = game.gameSetting.lobbyWaitTime

    init {
        // 运行当前定时器
        this.runTaskTimer(bukkitPlugin, 0, 1 * 20L)
    }

    override fun run() {
        
    }

}