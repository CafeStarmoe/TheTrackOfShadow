package top.ycmt.thetrackofshadow.cmd

import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import top.ycmt.thetrackofshadow.game.GameManager

// 加入游戏命令
val joinCommand = subCommand {
    dynamic(optional = true) {
        suggestion<Player>(uncheck = true) { _, _ ->
            GameManager.gameMap.map { it.value.gameSetting.gameName }
        }
        execute<Player> { player, _, argument ->
            GameManager.joinGame(argument, player)
        }
    }
    execute<Player> { player, context, argument ->
        player.sendMessage("§7不会使用? /${context.name} $argument <地图名>")
    }
}