package top.ycmt.thetrackofshadow.game.flow.random

import org.bukkit.Sound
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 上天去吧随机事件
class HeavenRandom(private val game: Game) : RandomInterface {
    override fun exec() {
        // 随机抽取一名玩家给予(漂浮，持续时间10s)
        game.playerModule.getAlivePlayers().shuffled().take(1).forEach {
            it.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, 10 * 20, 0))
        }
        // 提示玩家
        game.playerModule.getOnlineUsers().forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "${LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#a2f8e9,73f8e8>上天去吧</#>§f已随机挑选玩家获得效果<#ff9c9c,de4949>「漂浮」</#>§f!".toGradientColor(),
                ""
            )
        }
    }

}