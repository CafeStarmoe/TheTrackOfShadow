package top.ycmt.thetrackofshadow.game.flow.impl

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.flow.FlowAbstract
import top.ycmt.thetrackofshadow.pkg.logger.logger
import java.util.*

// 随机传送流程
class TeleportFlow(override val game: Game) : FlowAbstract() {
    private var nowCount = 0 // 现在执行传送动画的次数
    private val playerTeleportLocations = getPlayerTeleportLocations() // 玩家将会被传送的位置
    private val animationCount = 2 // 传送动画的次数
    private val animationHeight = 6 // 动画距离地面的高度
    private val defaultHeight = 210.0 // 初始看云高度

    init {
        game.playerModule.onlinePLayers.forEach {
            game.hidePlayer(it)
            it.gameMode = GameMode.SPECTATOR
        }
        // 传送看云
        teleportPlayerAnimation()
    }

    override fun onTick() {
        // 判断传送动画次数是否符合
        if (nowCount >= animationCount + 1) {
            for (p in game.playerModule.onlinePLayers) {
                // 获取将要传送到的位置
                val loc = playerTeleportLocations[p.uniqueId]
                if (loc == null) {
                    logger.error("目标位置为空, uuid: ${p.uniqueId}, name: ${p.name}")
                    continue
                }
                // 传送玩家
                p.teleport(loc)
                p.gameMode = GameMode.ADVENTURE
                game.showPlayer(p)
            }
            // 此阶段结束
            this.Done()
            return
        }
        teleportPlayerAnimation()
    }

    // 传送玩家动画
    private fun teleportPlayerAnimation() {
        for (p in game.playerModule.onlinePLayers) {
            // 获取将要传送到的位置
            val loc = playerTeleportLocations[p.uniqueId]
            if (loc == null) {
                logger.error("目标位置为空, uuid: ${p.uniqueId}, name: ${p.name}")
                continue
            }
            // 动画高度
            val animationY = loc.y + animationHeight
            // 计算每次下降的高度
            val height = (defaultHeight - animationY) / animationCount
            // 计算现在应该在的位置
            val y = animationY + height * (animationCount - nowCount)
            val animationLoc = Location(loc.world, loc.x, y, loc.z, 0f, 90f)
            // 传送玩家
            p.teleport(animationLoc)
        }
        nowCount++
    }

    // 获取在线玩家传送的位置
    private fun getPlayerTeleportLocations(): Map<UUID, Location> {
        val playerTeleportLocations: MutableMap<UUID, Location> = mutableMapOf()
        for (p in game.playerModule.onlinePLayers) {
            val loc = randomTeleportLocation()
            if (loc == null) {
                logger.error("玩家安全位置随机失败, uuid: ${p.uniqueId}, name: ${p.name}")
                continue
            }
            playerTeleportLocations[p.uniqueId] = loc
        }
        return playerTeleportLocations
    }

    // 获取随机传送到游戏地图的位置
    private fun randomTeleportLocation(): Location? {
        val worldName = game.gameSetting.gameMapWorld
        val vector1 = game.gameSetting.gameMapVector1
        val vector2 = game.gameSetting.gameMapVector2

        // 获取世界
        val world = Bukkit.getWorld(worldName)
        if (world == null) {
            logger.error("世界为空, worldName: $worldName")
            return null
        }

        var x = 0
        var y = 0
        var z = 0

        // 最大随机次数 超出限制就不管了
        val maxRandomCount = 64
        for (i in 0 until maxRandomCount) {
            // 随机取地图内的坐标
            x = (vector1.x.coerceAtMost(vector2.x).toInt()..vector1.x.coerceAtLeast(vector2.x).toInt()).random()
            z = (vector1.z.coerceAtMost(vector2.z).toInt()..vector1.z.coerceAtLeast(vector2.z).toInt()).random()

            // 获取坐标最高的方块
            val highestBlock = world.getHighestBlockAt(x, z)
            // 方块不安全则重新随机位置
            when (highestBlock.type) {
                Material.AIR, Material.VOID_AIR, Material.WATER, Material.LAVA -> {
                    if (i >= maxRandomCount - 1) {
                        logger.error("随机传送位置不安全, count: $i, x: $x, y: $y, z: $z")
                        return null
                    }
                    continue
                }

                else -> {}
            }
            y = highestBlock.y
            // 确保高度符合
            if (y > vector1.y.coerceAtLeast(vector2.y)) {
                if (i >= maxRandomCount - 1) {
                    logger.error("随机传送高度不符合, count: $i, x: $x, y: $y, z: $z")
                    return null
                }
            } else {
                // 两个都符合就结束循环
                break
            }
        }

        // 最终将传送到的位置
        val loc = Location(world, x + 0.5, y + 1.0, z + 0.5)
        // 加载这个区块
        val chunk = loc.chunk
        if (!chunk.isLoaded) {
            chunk.load(true)
        }
        return loc
    }

}