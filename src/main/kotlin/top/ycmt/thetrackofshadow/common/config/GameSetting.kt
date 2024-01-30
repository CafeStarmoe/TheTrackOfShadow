package top.ycmt.thetrackofshadow.common.config

// 游戏设置数据
data class GameSetting(
    // 游戏名称
    val gameName: String,
    // 开始游戏最少所需玩家数量
    val minPlayerCount: Int,
    // 最大玩家数量
    val maxPlayerCount: Int,
    // 大厅等待时间
    val lobbyWaitTick: Int
)