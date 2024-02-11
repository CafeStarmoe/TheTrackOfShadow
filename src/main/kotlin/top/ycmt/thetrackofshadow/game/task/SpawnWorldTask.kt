package top.ycmt.thetrackofshadow.game.task

import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import taboolib.module.effect.ParticleSpawner
import taboolib.module.effect.shape.Circle
import taboolib.platform.util.toProxyLocation
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor


// 出生点效果显示以及治疗任务
class SpawnWorldTask(private val game: Game) : TaskAbstract() {
    var tick: Long = 0 // tick数
    private val spawnLoc = game.setting.getRespawnLocation() // 出生点位置
    private var particle: Circle = Circle(
        spawnLoc.toProxyLocation(),
        game.setting.respawnRange,
        5.0,
        1 * 20L,
        SpawnParticle(game, this)
    ) // 粒子效果
    private var holoLine: TextHologramLine // 全息投影内容

    init {
        // 全息投影位置
        val loc = Location(spawnLoc.world, spawnLoc.x, spawnLoc.y + 3, spawnLoc.z)
        // 创建全息投影
        val hologram = game.hologramModule.createHologram(loc)
        hologram.lines.appendItem(ItemStack(Material.CLOCK))
        holoLine = hologram.lines.appendText("§e§l载入中...")
    }

    override fun run() {
        // 判断游戏是否进入了决赛阶段 不能重生就是决赛
        if (!game.respawnModule.isRespawnable()) {
            this.cancel()
            return
        }
        // 判断出生点保护是否启用
        if (game.spawnModule.enableRegain) {
            changeHologramLine() // 修改全息投影
            potionDistance() // 范围药水效果
            particle.show() // 渲染粒子特效
        }
        tick++
    }

    override fun cancel() {
        super.cancel()
        // 关闭粒子效果任务
        particle.turnOffTask()
    }

    // 修改全息投影内容
    private fun changeHologramLine() {
        // 判断出生点是否反转
        if (!game.spawnModule.reverse) {
            // 每30s给予1次
            if (tick % 30 == 0L) {
                holoLine.text = "<#deffd2,bee8ff>已治疗✔</#>".toGradientColor()
            } else {
                holoLine.text = "§f下次治疗: <#ffcbcb,ff7093>${30 - tick % 30}秒</#>".toGradientColor()
            }
        } else {
            holoLine.text = "§c§l已反转✘"
        }
    }

    // 给予范围内的玩家药水效果
    private fun potionDistance() {
        game.playerModule.getAlivePlayers().forEach {
            if (spawnLoc.distance(it.location) <= game.setting.respawnRange) {
                // 判断出生点是否反转
                if (!game.spawnModule.reverse) {
                    // 每30s给予1次
                    if (tick % 30 == 0L) {
                        // 给予玩家瞬间治疗药水效果
                        it.addPotionEffect(PotionEffect(PotionEffectType.HEAL, 30, 0))
                    }
                } else {
                    // 给予玩家瞬间伤害药水效果
                    it.addPotionEffect(PotionEffect(PotionEffectType.HARM, 30, 0))
                }
            }
        }
    }

    // 出生点粒子效果
    class SpawnParticle(private val game: Game, private val spawnWorldTask: SpawnWorldTask) : ParticleSpawner {
        override fun spawn(location: taboolib.common.util.Location) {
            val world = Bukkit.getWorld(game.setting.gameWorldName) ?: return

            // 判断重生点回复血量是否启用
            if (!game.spawnModule.enableRegain) {
                return
            }

            // 判断出生点是否反转
            if (!game.spawnModule.reverse) {
                // 每30s显示一次回血效果
                if (spawnWorldTask.tick % 30 == 0L) {
                    world.spawnParticle(Particle.GLOW_SQUID_INK, location.x, location.y, location.z, 3)
                }
            } else {
                // 每3s显示一次反转效果
                if (spawnWorldTask.tick % 3 == 0L) {
                    world.spawnParticle(Particle.SMOKE_LARGE, location.x, location.y, location.z, 3)
                }
            }
        }

    }

}