package top.ycmt.thetrackofshadow.game.event.random

import org.bukkit.Sound
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.task.AnvilRandomTask

// 铁砧雨随机事件
class AnvilRandom(private val game: Game) : RandomInterface {
    override fun exec() {
        // 开启铁砧雨事件
        game.subTaskModule.submitTask(period = 5L, task = AnvilRandomTask(game, 40))
        // 提示玩家
        game.playerModule.getOnlinePlayers().forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage("", "§f§l随机事件 > §8铁砧雨§7已开始在所有玩家上空落铁, 持续§c10s§7.", "")
        }
    }
}