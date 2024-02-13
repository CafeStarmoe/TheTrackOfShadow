package top.ycmt.thetrackofshadow.game.task

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import taboolib.platform.util.setMeta
import top.ycmt.thetrackofshadow.constant.LegacyTextConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 铁砧雨随机事件任务
class AnvilRandomTask(private val game: Game, private var tick: Int) : TaskAbstract() {
    override fun run() {
        // 事件结束
        if (tick <= 0) {
            game.playerModule.getOnlineUsers().forEach {
                it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                it.sendMessage(
                    "",
                    "${LegacyTextConst.RANDOM_EVENT_PREFIX_LEGACY_TEXT}<#989898,777777>铁砧雨</#>§f现已结束在所有玩家上空<#ff9c9c,de4949>落铁</#>§f!".toGradientColor(),
                    ""
                )
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
                // 生成铁砧下落方块
                val fallingBlock = it.world.spawnFallingBlock(loc, Material.ANVIL.createBlockData())
                fallingBlock.dropItem = false // 不掉落物品
                fallingBlock.setHurtEntities(true) // 允许伤害实体
                fallingBlock.setMeta("game", game) // 设置游戏属性
            }
        }
        tick-- // 剩余次数-1
    }

}