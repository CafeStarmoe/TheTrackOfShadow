package top.ycmt.thetrackofshadow.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.Bukkit
import org.bukkit.Location

// 游戏配置表
@Serializable
data class GameData(
    @SerialName("游戏名") val gameName: String,
    @SerialName("地图名") val mapName: String,
    @SerialName("所需玩家数") val minPlayerCount: Int,
    @SerialName("最大玩家数") val maxPlayerCount: Int,
    @SerialName("大厅等待时间") val lobbyWaitTick: Long,
    @SerialName("大厅世界名") val lobbyWorldName: String,
    @SerialName("大厅坐标X") val lobbyLocX: Double,
    @SerialName("大厅坐标Y") val lobbyLocY: Double,
    @SerialName("大厅坐标Z") val lobbyLocZ: Double,
    @SerialName("游戏世界名") val gameWorldName: String,
    @SerialName("游戏坐标地图1X") val gameMapLoc1X: Double,
    @SerialName("游戏地图坐标1Y") val gameMapLoc1Y: Double,
    @SerialName("游戏地图坐标1Z") val gameMapLoc1Z: Double,
    @SerialName("游戏地图坐标2X") val gameMapLoc2X: Double,
    @SerialName("游戏地图坐标2Y") val gameMapLoc2Y: Double,
    @SerialName("游戏地图坐标2Z") val gameMapLoc2Z: Double,
    @SerialName("重生点范围") val respawnRange: Double,
    @SerialName("重生点坐标X") val respawnLocX: Double,
    @SerialName("重生点坐标Y") val respawnLocY: Double,
    @SerialName("重生点坐标Z") val respawnLocZ: Double,
    @SerialName("退出游戏世界名") val quitWorldName: String,
    @SerialName("退出游戏坐标X") val quitLocX: Double,
    @SerialName("退出游戏坐标Y") val quitLocY: Double,
    @SerialName("退出游戏坐标Z") val quitLocZ: Double,
    @SerialName("宝箱数") val chestCount: Int,
) {
    // 获取大厅位置
    fun getLobbyLocation(): Location {
        return Location(Bukkit.getWorld(lobbyWorldName), lobbyLocX, lobbyLocY, lobbyLocZ)
    }

    // 获取游戏地图位置1
    fun getGameMapLocation1(): Location {
        return Location(Bukkit.getWorld(gameWorldName), gameMapLoc1X, gameMapLoc1Y, gameMapLoc1Z)
    }

    // 获取游戏地图位置2
    fun getGameMapLocation2(): Location {
        return Location(Bukkit.getWorld(gameWorldName), gameMapLoc2X, gameMapLoc2Y, gameMapLoc2Z)
    }

    // 获取离开游戏位置
    fun getQuitLocation(): Location {
        return Location(Bukkit.getWorld(quitWorldName), quitLocX, quitLocY, quitLocZ)
    }

    // 获取重生位置
    fun getRespawnLocation(): Location {
        return Location(Bukkit.getWorld(gameWorldName), respawnLocX, respawnLocY, respawnLocZ)
    }

}
