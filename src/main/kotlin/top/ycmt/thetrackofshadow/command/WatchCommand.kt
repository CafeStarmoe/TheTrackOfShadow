package top.ycmt.thetrackofshadow.command

import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendMsg

// 观看游戏命令
object WatchCommand {
    val command = subCommand {
        dynamic(optional = true) {
            suggestion<Player>(uncheck = true) { _, _ ->
                GameManager.getGameNames()
            }
            execute<Player> { player, _, argument ->
                GameManager.watchGame(argument, player)
            }
        }
        execute<Player> { player, context, argument ->
            player.sendMsg("§7用法: /${context.name} $argument <地图名>")
        }
    }

}
