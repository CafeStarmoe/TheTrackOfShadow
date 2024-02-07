package top.ycmt.thetrackofshadow.game.state

// 游戏阶段状态
enum class PhaseState {
    INIT_PHASE, // 初始化阶段
    LOBBY_PHASE,// 大厅等待阶段
    RUNNING_PHASE, // 游戏运行阶段
    SETTLE_PHASE, // 结算阶段
}