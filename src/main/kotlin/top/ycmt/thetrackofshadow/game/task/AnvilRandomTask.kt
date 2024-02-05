package top.ycmt.thetrackofshadow.game.task

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import top.ycmt.thetrackofshadow.game.Game

// 铁砧雨随机事件任务
class AnvilRandomTask(private val game: Game, private var count: Int) : TaskAbstract() {
    override fun run() {
        // 事件结束
        if (count <= 0) {
            game.playerModule.getOnlinePlayers().forEach {
                it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                it.sendMessage("", "§f§l随机事件 > §8铁砧雨§7现已结束在所有玩家上空§c落铁§7.", "")
            }
            this.cancel()
            return
        }
        // 执行事件
        game.playerModule.getAlivePlayers().forEach {
            val x = it.location.x + (-2..2).random()
            val y = it.location.y + (4..9).random()
            val z = it.location.z + (-2..2).random()
            val loc = Location(it.world, x, y, z)
            // 获取此位置的方块
            val block = it.world.getBlockAt(loc)
            // 确保要更改的方块为空气
            if (block.type == Material.AIR) {
                block.type = Material.ANVIL // 生成铁砧
            }
        }
        count-- // 剩余次数-1
    }

}