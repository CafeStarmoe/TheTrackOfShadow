package top.ycmt.thetrackofshadow.game.particle

import org.bukkit.Bukkit
import org.bukkit.Particle
import taboolib.common.util.Location
import taboolib.module.effect.ParticleSpawner
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.task.SpawnWorldTask

// 出生点粒子效果
class SpawnParticle(private val game: Game, private val spawnWorldTask: SpawnWorldTask) : ParticleSpawner {
    override fun spawn(location: Location) {
        val world = Bukkit.getWorld(game.setting.gameMapWorld) ?: return

        // 判断出生点保护是否启用
        if (!game.spawnModule.enableProtect) {
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