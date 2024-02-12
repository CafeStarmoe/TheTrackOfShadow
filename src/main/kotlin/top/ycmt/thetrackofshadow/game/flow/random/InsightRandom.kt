package top.ycmt.thetrackofshadow.game.flow.random

import org.bukkit.Sound
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 洞察一切随机事件
class InsightRandom(private val game: Game) : RandomInterface {
    override fun exec() {
        game.playerModule.getAlivePlayers().forEach {
            // 给予玩家(发光，持续时间30s)
            it.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 30 * 20, 0))
            // 提示玩家
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "${RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#ffffcf,e6f8b3>洞察一切</#>§f已给予所有玩家获得效果<#ff9c9c,de4949>「发光」</#>§f!".toGradientColor(),
                ""
            )
        }
    }

}