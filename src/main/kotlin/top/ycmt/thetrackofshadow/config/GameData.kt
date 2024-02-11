package top.ycmt.thetrackofshadow.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.Bukkit
import org.bukkit.Location

// 游戏配置表
@Serializable
data class GameData(
    // 游戏名称
    @SerialName("游戏名") val gameName: String,
    // 地图名称
    @SerialName("地图名") val mapName: String,
    // 开始游戏所需玩家数量
    @SerialName("所需玩家数") val minPlayerCount: Int,
    // 最大玩家数量
    @SerialName("最大玩家数") val maxPlayerCount: Int,
    // 大厅等待时间 单位秒
    @SerialName("大厅等待时间") val lobbyWaitTick: Long,
    // 大厅世界名
    @SerialName("大厅世界名") val lobbyWorldName: String,
    // 大厅坐标X
    @SerialName("大厅坐标X") val lobbyLocX: Double,
    // 大厅坐标Y
    @SerialName("大厅坐标Y") val lobbyLocY: Double,
    // 大厅坐标Z
    @SerialName("大厅坐标Z") val lobbyLocZ: Double,
    // 游戏世界名
    @SerialName("游戏世界名") val gameWorldName: String,
    // 游戏地图坐标1X
    @SerialName("游戏坐标地图1X") val gameMapLoc1X: Double,
    // 游戏地图坐标1Y
    @SerialName("游戏地图坐标1Y") val gameMapLoc1Y: Double,
    // 游戏地图坐标1Z
    @SerialName("游戏地图坐标1Z") val gameMapLoc1Z: Double,
    // 游戏地图坐标2X
    @SerialName("游戏地图坐标2X") val gameMapLoc2X: Double,
    // 游戏地图坐标2Y
    @SerialName("游戏地图坐标2Y") val gameMapLoc2Y: Double,
    // 游戏地图坐标2Z
    @SerialName("游戏地图坐标2Z") val gameMapLoc2Z: Double,
    // 重生点范围
    @SerialName("重生点范围") val respawnRange: Double,
    // 重生点坐标X
    @SerialName("重生点坐标X") val respawnLocX: Double,
    // 重生点坐标Y
    @SerialName("重生点坐标Y") val respawnLocY: Double,
    // 重生点坐标Z
    @SerialName("重生点坐标Z") val respawnLocZ: Double,
    // 退出游戏世界名
    @SerialName("退出游戏世界名") val quitWorldName: String,
    // 退出游戏坐标X
    @SerialName("退出游戏坐标X") val quitLocX: Double,
    // 退出游戏坐标Y
    @SerialName("退出游戏坐标Y") val quitLocY: Double,
    // 退出游戏坐标Z
    @SerialName("退出游戏坐标Z") val quitLocZ: Double,
    // 宝箱数量
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
