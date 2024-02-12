package top.ycmt.thetrackofshadow.game.task

import org.bukkit.Sound
import org.bukkit.entity.EntityType
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 天雷圣裁随机事件任务
class ThunderRandomTask(private val game: Game, private var tick: Long) : TaskAbstract() {
    override fun run() {
        // 事件结束
        if (tick <= 0) {
            // 提示玩家
            game.playerModule.getOnlineUsers().forEach {
                it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                it.sendMessage(
                    "",
                    "${RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#c2ffff,a1f4ff>天雷圣裁</#>§f现已结束随机挑选玩家<#ff9c9c,de4949>落雷</#>§f!".toGradientColor(),
                    ""
                )
            }
            this.cancel()
            return
        }
        // 执行事件
        game.playerModule.getAlivePlayers().shuffled().take(2).forEach {
            it.world.spawnEntity(it.location, EntityType.LIGHTNING) // 落雷
        }
        tick--
    }
}