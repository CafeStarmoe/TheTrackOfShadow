package top.ycmt.thetrackofshadow.pkg.hide

import org.bukkit.entity.Player
import taboolib.platform.util.bukkitPlugin

// 隐藏玩家
object HidePlayer {

    // 隐藏玩家
    fun Player.hidePlayers(players: List<Player>) {
        players.forEach {
            this.hidePlayer(bukkitPlugin, it)
        }
    }

    // 显示玩家
    fun Player.showPlayers(players: List<Player>) {
        players.forEach {
            this.showPlayer(bukkitPlugin, it)
        }
    }

}
