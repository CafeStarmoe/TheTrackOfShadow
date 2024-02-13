package top.ycmt.thetrackofshadow.game.flow.random

import org.bukkit.Sound
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 猫尾特调随机事件
class CattailRandom(private val game: Game) : RandomInterface {

    override fun exec() {
        // 随机抽取一名玩家给予(反胃，持续时间10s)
        game.playerModule.getAlivePlayers().shuffled().take(1).forEach {
            it.addPotionEffect(PotionEffect(PotionEffectType.CONFUSION, 10 * 20, 1))
        }
        // 提示玩家
        game.playerModule.getOnlineUsers().forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "${LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#f7c79c,ef987d>猫尾特调</#>§f已随机挑选玩家获得效果<#ff9c9c,de4949>「反胃」</#>§f!".toGradientColor(),
                ""
            )
        }
    }

}