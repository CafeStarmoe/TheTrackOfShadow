package top.ycmt.thetrackofshadow.game.module

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI
import me.filoghost.holographicdisplays.api.hologram.Hologram
import org.bukkit.Location
import taboolib.platform.util.bukkitPlugin
import top.ycmt.thetrackofshadow.game.Game

// 全息投影管理模块
class HologramModule(private val game: Game) {
    private val hologramAPI = HolographicDisplaysAPI.get(bukkitPlugin) // 全息投影api
    private val holograms = mutableListOf<Hologram>() // 游戏创建的全息投影列表

    // 创建全息投影
    fun createHologram(loc: Location): Hologram {
        val hologram = hologramAPI.createHologram(loc)
        // 记录创建的全息投影
        holograms.add(hologram)
        return hologram
    }

    // 清除所有创建的全息投影
    fun deleteHolograms() {
        holograms.forEach {
            it.delete()
        }
    }
}