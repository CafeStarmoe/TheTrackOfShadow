package top.ycmt.thetrackofshadow.command

import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import top.ycmt.thetrackofshadow.game.GameManager

// 离开游戏命令
object QuitCommand {
    val command = subCommand {
        execute<Player> { player, _, _ ->
            GameManager.quitGame(player)
        }
    }

}
