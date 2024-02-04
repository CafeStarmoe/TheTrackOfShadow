package top.ycmt.thetrackofshadow.pkg.hideplayer

import org.bukkit.entity.Player
import taboolib.platform.util.bukkitPlugin

// 隐藏玩家
fun org.bukkit.entity.Player.hidePlayers(players: List<Player>) {
    players.forEach {
        this.hidePlayer(bukkitPlugin, it)
    }
}

// 显示玩家
fun org.bukkit.entity.Player.showPlayers(players: List<Player>) {
    players.forEach {
        this.showPlayer(bukkitPlugin, it)
    }
}

