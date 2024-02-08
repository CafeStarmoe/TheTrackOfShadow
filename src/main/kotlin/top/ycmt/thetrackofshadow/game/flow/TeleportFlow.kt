package top.ycmt.thetrackofshadow.game.flow

import org.bukkit.*
import top.ycmt.thetrackofshadow.constant.TeleportAnimationConst.ANIMATION_COUNT
import top.ycmt.thetrackofshadow.constant.TeleportAnimationConst.ANIMATION_HEIGHT
import top.ycmt.thetrackofshadow.constant.TeleportAnimationConst.ANIMATION_TICK
import top.ycmt.thetrackofshadow.constant.TeleportAnimationConst.DEFAULT_HEIGHT
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.state.CancelState
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor
import top.ycmt.thetrackofshadow.pkg.hide.HidePlayer.hidePlayers
import top.ycmt.thetrackofshadow.pkg.hide.HidePlayer.showPlayers
import top.ycmt.thetrackofshadow.pkg.logger.Logger
import java.util.*

// 随机传送事件
class TeleportFlow(private val game: Game) : FlowInterface {
    override val finishTick = ANIMATION_TICK * (ANIMATION_COUNT + 1) // 运行完毕的tick数
    override val eventMsg = "等待随机传送" // 事件消息

    private var nowCount = 0 // 现在执行传送动画的次数
    private val playerTeleportLocations = getPlayerTeleportLocations() // 玩家将会被传送的位置

    override fun exec(leftTick: Long) {
        when (leftTick) {
            ANIMATION_TICK * (ANIMATION_COUNT + 1) -> {
                // 设置玩家为传送动画状态
                game.playerModule.getAlivePlayers().forEach {
                    it.hidePlayers(game.playerModule.getAlivePlayers())
                    // 暂时允许飞行
                    it.allowFlight = true
                    it.isFlying = true
                    it.gameMode = GameMode.SPECTATOR
                }
                // 全局禁止移动位置和视角以及不允许受伤
                game.cancelModule.addGlobalCancelState(
                    CancelState.CANCEL_MOVE,
                    CancelState.CANCEL_MOVE_ANGLE,
                    CancelState.CANCEL_DAMAGE,
                )
                teleportPlayerAnimation()
            }

            in ANIMATION_TICK..ANIMATION_TICK * ANIMATION_COUNT step ANIMATION_TICK -> {
                teleportPlayerAnimation()
            }

            0L -> {
                // 取消玩家传送动画状态
                for (p in game.playerModule.getAlivePlayers()) {
                    // 获取将要传送到的位置
                    val loc = playerTeleportLocations[p.uniqueId]
                    if (loc == null) {
                        Logger.error("目标位置为空, uuid: ${p.uniqueId}, name: ${p.name}")
                        continue
                    }
                    // 传送玩家
                    p.teleport(loc)
                    p.sendTitle("<#deffd2,bee8ff>传送完毕</#>".toGradientColor(), "", 5, 10, 5)
                    p.playSound(p, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1f, 1f)
                }
                // 全局允许移动位置和视角以及允许受伤
                game.cancelModule.removeGlobalCancelState(
                    CancelState.CANCEL_MOVE,
                    CancelState.CANCEL_MOVE_ANGLE,
                    CancelState.CANCEL_DAMAGE,
                )
                game.playerModule.getAlivePlayers().forEach {
                    // 不允许飞行
                    it.allowFlight = false
                    it.isFlying = false
                    // 设置为冒险模式
                    it.gameMode = GameMode.ADVENTURE
                    it.showPlayers(game.playerModule.getAlivePlayers())
                }
            }
        }
    }

    // 传送玩家动画
    private fun teleportPlayerAnimation() {
        for (p in game.playerModule.getAlivePlayers()) {
            // 获取将要传送到的位置
            val loc = playerTeleportLocations[p.uniqueId]
            if (loc == null) {
                Logger.error("目标位置为空, uuid: ${p.uniqueId}, name: ${p.name}")
                continue
            }
            // 动画高度
            val animationY = loc.y + ANIMATION_HEIGHT
            // 计算每次下降的高度
            val height = (DEFAULT_HEIGHT - animationY) / ANIMATION_COUNT
            // 计算现在应该在的位置
            val y = animationY + height * (ANIMATION_COUNT - nowCount)
            val animationLoc = Location(loc.world, loc.x, y, loc.z, 0f, 90f)
            // 传送玩家
            p.teleport(animationLoc)
            // 提示玩家传送中
            p.sendTitle("<#ffefbb,e3ce82>传送中...</#>".toGradientColor(), "", 5, 40, 5)
            p.playSound(p, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1f, 1f)
        }
        nowCount++
    }

    // 获取在线玩家传送的位置
    private fun getPlayerTeleportLocations(): Map<UUID, Location> {
        val playerTeleportLocations: MutableMap<UUID, Location> = mutableMapOf()
        for (p in game.playerModule.getAlivePlayers()) {
            val loc = randomTeleportLocation()
            if (loc == null) {
                Logger.error("玩家安全位置随机失败, uuid: ${p.uniqueId}, name: ${p.name}")
                continue
            }
            playerTeleportLocations[p.uniqueId] = loc
        }
        return playerTeleportLocations
    }

    // 获取随机传送到游戏地图的位置
    private fun randomTeleportLocation(): Location? {
        val worldName = game.setting.gameMapWorld
        val vector1 = game.setting.gameMapVector1
        val vector2 = game.setting.gameMapVector2

        // 获取世界
        val world = Bukkit.getWorld(worldName)
        if (world == null) {
            Logger.error("世界为空, worldName: $worldName")
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
                        Logger.error("随机传送位置不安全, count: $i, x: $x, y: $y, z: $z")
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
                    Logger.error("随机传送高度不符合, count: $i, x: $x, y: $y, z: $z")
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