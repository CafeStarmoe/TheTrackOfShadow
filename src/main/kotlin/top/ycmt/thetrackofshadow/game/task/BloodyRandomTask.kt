package top.ycmt.thetrackofshadow.game.task

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.state.CancelState
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 血色之夜随机事件任务
class BloodyRandomTask(private val game: Game, private var tick: Long) : TaskAbstract() {
    private val world = Bukkit.getWorld(game.setting.gameWorldName) // 世界
    private var worldTime: Long = world?.time ?: 0L // 保存修改之前的时间

    init {
        // 双倍伤害倍率
        game.damageModule.damageMultiple = 2.0
        // 关闭重生点回复血量
        game.spawnModule.enableRegain = false
        // 禁止打开新宝箱
        game.cancelModule.addGlobalCancelState(CancelState.CANCEL_FIND_CHEST)
        world?.time = 18000L // 修改世界时间为夜晚
        game.playerModule.getOnlineUsers().forEach {
            it.sendMessage(
                "",
                "§4§l血色之夜 > §f所有玩家<#ff9c9c,de4949>伤害翻倍</#>§f, 持续<#ffffd6>高亮</#>§f, 重生点<#deffd2,bee8ff>回复禁用</#>§f, 新宝箱<#f7c79c,ef987d>无法打开</#>§f!".toGradientColor(),
                ""
            )
        }
    }

    override fun run() {
        // 事件结束
        if (tick <= 0) {
            // 还原伤害倍率
            game.damageModule.damageMultiple = 1.0
            // 还原世界时间
            world?.time = worldTime
            // 开启重生点回复血量
            game.spawnModule.enableRegain = true
            // 允许打开新宝箱
            game.cancelModule.removeGlobalCancelState(CancelState.CANCEL_FIND_CHEST)
            // 提示玩家
            game.playerModule.getOnlineUsers().forEach {
                it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                it.sendMessage(
                    "",
                    "${LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT}§4血色之夜§f现已结束入侵<#ff9c9c,de4949>现实</#>§f!".toGradientColor(),
                    ""
                )
            }
            this.cancel()
            return
        }
        // 给予玩家发光效果
        game.playerModule.getAlivePlayers().forEach {
            it.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 30, 0))
        }
        tick--
    }
}