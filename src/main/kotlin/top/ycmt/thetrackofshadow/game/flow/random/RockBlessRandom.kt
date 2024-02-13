package top.ycmt.thetrackofshadow.game.flow.random

import org.bukkit.Sound
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 岩之祝福随机事件
class RockBlessRandom(private val game: Game) : RandomInterface {
    override fun exec() {
        // 随机抽取一名玩家给予(抗性提升II，持续时间3分钟)
        game.playerModule.getAlivePlayers().shuffled().take(1).forEach {
            it.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 180 * 30, 1))
        }
        // 提示玩家
        game.playerModule.getOnlineUsers().forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "${LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#ffe2a8,e5bc6a>岩之祝福</#>§f已随机挑选玩家获得效果<#ff9c9c,de4949>「固若金汤」</#>§f!".toGradientColor(),
                ""
            )
        }
    }

}