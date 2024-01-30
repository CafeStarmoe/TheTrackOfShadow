package top.ycmt.thetrackofshadow.conf

// 游戏设置数据
data class GameSetting(
    // 游戏名称
    val gameName: String,
    // 玩家数量
    val playerCount: UInt,
    // 大厅等待时间
    val lobbyWaitTime: UInt
)