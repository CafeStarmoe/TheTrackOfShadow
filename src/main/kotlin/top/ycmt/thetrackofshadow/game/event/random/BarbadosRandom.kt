package top.ycmt.thetrackofshadow.game.event.random

import org.bukkit.Sound
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 喝醉的巴巴托斯随机事件
class BarbadosRandom(private val game: Game) : RandomInterface {
    override fun exec() {
        game.playerModule.getAlivePlayers().forEach {
            // 给予玩家(漂浮，持续时间5s)
            it.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, 100, 15))
            // 提示玩家
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "${LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#b1ffb8,75db98>喝醉的巴巴托斯</#>§f已给予所有玩家获得效果<#ff9c9c,de4949>「漂浮」</#>§f!".toGradientColor(),
                ""
            )
        }
    }
}