package top.ycmt.thetrackofshadow.game.flow.random

import org.bukkit.Sound
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.task.PotatoRandomTask
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 熟土豆随机事件
class PotatoRandom(private val game: Game) : RandomInterface {
    override fun exec() {
        // 开启熟土豆事件
        game.subTaskModule.submitTask(period = 5L, task = PotatoRandomTask(game, 20L))
        // 提示玩家
        game.playerModule.getOnlineUsers().forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "${LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#ffd685,fec147>熟土豆</#>§f散发出硬件的香味, 怎么..卡卡的? 持续<#ff9c9c,de4949>5秒</#>§f!".toGradientColor(),
                ""
            )
        }
    }

}