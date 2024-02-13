package top.ycmt.thetrackofshadow.game.flow.random

import org.bukkit.Sound
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 相遇之缘随机事件
class MeetRandom(private val game: Game) : RandomInterface {
    override fun exec() {
        // 随机抽取两名玩家
        game.playerModule.getAlivePlayers().shuffled().take(2).forEach {
            it.teleport(game.setting.getRespawnLocation()) // 传送至重生点
        }
        // 提示玩家
        game.playerModule.getOnlineUsers().forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "${LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#ffbcf5,ff9ae3>相遇之缘</#>§f已随机挑选两位玩家至重生点!".toGradientColor(),
                ""
            )
        }
    }

}