package top.ycmt.thetrackofshadow.game.task

import org.bukkit.Sound
import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 开摆随机事件任务
class RottenRandomTask(private val game: Game, private var tick: Long) : TaskAbstract() {
    lateinit var player: Player // 选择的玩家

    init {
        // 随机选择一名玩家
        game.playerModule.getAlivePlayers().shuffled().take(1).forEach {
            player = it // 记录玩家
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
                    "${RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#beffeb,70e5bb>开摆</#>§f现已结束限制玩家无法离开<#ff9c9c,de4949>重生点</#>§f!".toGradientColor(),
                    ""
                )
            }
            this.cancel()
            return
        }
        // 执行事件
        val respawnLoc = game.setting.getRespawnLocation()
        if (player.location.distance(respawnLoc) > game.setting.respawnRange) {
            player.teleport(respawnLoc) // 传送至重生点
        }
        tick--
    }
}