package top.ycmt.thetrackofshadow.command

import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import top.ycmt.thetrackofshadow.game.GameManager

// 加入游戏命令
object JoinCommand {

    val command = subCommand {
        dynamic(optional = true) {
            suggestion<Player>(uncheck = true) { _, _ ->
                GameManager.getGameNames()
            }
            execute<Player> { player, _, argument ->
                GameManager.joinGame(argument, player)
            }
        }
        execute<Player> { player, context, argument ->
            player.sendMessage("§7不会使用? /${context.name} $argument <地图名>")
        }
    }

}
