package top.ycmt.thetrackofshadow.game.task

import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 熟土豆随机事件任务
class PotatoRandomTask(private val game: Game, private var tick: Long) : TaskAbstract() {
    private var playerLocs: MutableMap<Player, Location> = mutableMapOf() // 玩家位置列表

    init {
        // 记录每位玩家位置
        game.playerModule.getAlivePlayers().forEach {
            playerLocs[it] = it.location // 记录位置
        }
    }

    override fun run() {
        // 事件结束
        if (tick <= 0) {
            // 提示玩家
            game.playerModule.getOnlineUsers().forEach {
                it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                it.sendMessage(
                    "",
                    "${LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#ffd685,fec147>熟土豆</#>§f现已结束发热降频, 真的会<#ff9c9c,de4949>流畅</#>§f吗?".toGradientColor(),
                    ""
                )
            }
            this.cancel()
            return
        }

        // 回溯位置
        playerLocs.forEach {
            it.key.teleport(it.value)
        }

        tick--
    }
}