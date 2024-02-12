package top.ycmt.thetrackofshadow.game.flow.random

import org.bukkit.Sound
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 禁锢器随机事件
class ConfineRandom(private val game: Game) : RandomInterface {
    override fun exec() {
        // 随机抽取一名玩家给予(缓慢，持续时间5s)
        game.playerModule.getAlivePlayers().shuffled().take(1).forEach {
            it.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 5 * 20, 255))
        }
        // 提示玩家
        game.playerModule.getOnlineUsers().forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "${RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#a6daff,6dc2ff>禁锢器</#>§f已随机挑选玩家获得效果<#ff9c9c,de4949>「缓慢」</#>§f!".toGradientColor(),
                ""
            )
        }
    }

}