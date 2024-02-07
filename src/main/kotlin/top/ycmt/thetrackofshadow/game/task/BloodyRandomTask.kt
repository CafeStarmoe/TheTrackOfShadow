package top.ycmt.thetrackofshadow.game.task

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 血色之夜随机事件任务
class BloodyRandomTask(private val game: Game, private var tick: Long) : TaskAbstract() {
    private val world = Bukkit.getWorld(game.setting.gameMapWorld) // 世界
    private var worldTime: Long = world?.time ?: 0L // 保存修改之前的时间

    init {
        // 双倍伤害倍率
        game.damageModule.damageMultiple = 2.0
        // 关闭出生点保护
        game.spawnModule.enableProtect = false
//        game.modGame.playerState.add(PlayerState.CANCEL_CHEST) // 禁止开箱
        world?.time = 18000L // 修改世界时间为夜晚
        game.playerModule.getOnlineUsers().forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                "",
                "§4§l血色之夜 > §7所有玩家§c伤害翻倍§7, 持续§f高亮§7, 重生点§a保护禁用§7, 宝箱§e无法打开§7!",
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
            // 开启出生点保护
            game.spawnModule.enableProtect = true
//            game.modGame.playerState.remove(PlayerState.CANCEL_CHEST) // 允许开箱
            game.playerModule.getOnlineUsers().forEach {
                it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                it.sendMessage(
                    "",
                    "$RANDOM_EVENT_PREFIX_LEGACY_TEXT§4血色之夜§f现已结束入侵<#ff9c9c,de4949>现实</#>§f!".toGradientColor(),
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