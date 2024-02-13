package top.ycmt.thetrackofshadow.game.task

import org.bukkit.Sound
import taboolib.module.chat.impl.DefaultComponent
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 地脉喷涌随机事件任务
class LeyLineRandomTask(private val game: Game, private var tick: Long) : TaskAbstract() {
    init {
        // 获得积分翻倍
        game.scoreModule.scoreMultiple = 2.0
        // 被击杀不扣除积分
        game.scoreModule.deductScore = false
        // 地脉喷涌前缀
        val prefixText = DefaultComponent()
            .append("<#cfffcf,9ce3a7>地脉喷涌 > </#>".toGradientColor()).bold()
            .toLegacyText()
        // 提示玩家
        game.playerModule.getOnlineUsers().forEach {
            it.sendMessage(
                "",
                "$prefixText§f所有玩家<#ff9c9c,de4949>获得积分翻倍</#>§f, 被击杀时<#ff9c9c,de4949>不扣除积分</#>§f!".toGradientColor(),
                ""
            )
        }
    }

    override fun run() {
        // 事件结束
        if (tick <= 0) {
            // 还原积分倍率
            game.scoreModule.scoreMultiple = 1.0
            // 还原扣除积分
            game.scoreModule.deductScore = true
            // 提示玩家
            game.playerModule.getOnlineUsers().forEach {
                it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                it.sendMessage(
                    "",
                    "${LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#cfffcf,9ce3a7>地脉喷涌</#>§f现已结束给予所有玩家<#ff9c9c,de4949>馈赠</#>§f!".toGradientColor(),
                    ""
                )
            }
            this.cancel()
            return
        }
        tick--
    }

}